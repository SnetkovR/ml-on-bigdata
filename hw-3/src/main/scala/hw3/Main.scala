package hw3

import hw3.Utility.{crossValidation, getLogger, getXyFromCsvPath, mae, writeResultsToFile}
import breeze.stats.meanAndVariance

object Main {

  def main(args: Array[String]): Unit = {

    val logger = getLogger(name = "linear_regression", outputPath = "linear_regression.log")

    var dataTrain = ""
    var dataTest = ""
    var targetCol = 0
    var outTrain = ""
    var outTest = ""
    args.sliding(2, 2).toList.collect {
      case Array("--data-train", argDataTrain: String) => dataTrain = argDataTrain
      case Array("--data-test", argDataTest: String) => dataTest = argDataTest
      case Array("--target-column", argTargetCol: String) => targetCol = argTargetCol.toInt
      case Array("--out-train", argOutTrain: String) => outTrain = argOutTrain
      case Array("--out-test", argOutTest: String) => outTest = argOutTest
    }
    logger.info(s"Loading train data: $dataTrain")

    var tupleDataTrain = getXyFromCsvPath(path = dataTrain, targetCol = targetCol)
    var trainFeatures = tupleDataTrain._1
    var trainTarget = tupleDataTrain._2

    logger.info("Start cross validation")
    crossValidation(trainFeatures, trainTarget, folds = 3, logger)
    logger.info("End cross validation")

    tupleDataTrain = getXyFromCsvPath(path = dataTrain, targetCol = targetCol)
    trainFeatures = tupleDataTrain._1
    trainTarget = tupleDataTrain._2

    logger.info(s"Loading validation data: $dataTest")
    val tupleDataTest = getXyFromCsvPath(dataTest, targetCol)
    val testFeatures= tupleDataTest._1
    val testTarget = tupleDataTest._2

    logger.info(s"Scaling data")
    for (idx <- 0 until trainFeatures.cols) {
      val stats = meanAndVariance(trainFeatures(::, idx))
      trainFeatures(::, idx) := trainFeatures(::, idx).toDenseVector.map(el => (el - stats.mean) / stats.variance)
      testFeatures(::, idx) := testFeatures(::, idx).toDenseVector.map(el => (el - stats.mean) / stats.variance)
    }

    logger.info(f"Start training")
    val lr = new LinearRegression(logger)
    lr.fit(trainFeatures, trainTarget, epochs = 251)
    val predictTrain = lr.predict(trainFeatures.toDenseMatrix)
    val maeTrain = mae(predictTrain, trainTarget)
    logger.info(f"MAE train: $maeTrain%.2f")
    writeResultsToFile(outTrain, predictTrain)

    val predictTest = lr.predict(testFeatures)
    val maeTest = mae(predictTest, testTarget)
    logger.info(f"MAE test: $maeTest%.2f")
    writeResultsToFile(outTest, predictTest)
  }
}

