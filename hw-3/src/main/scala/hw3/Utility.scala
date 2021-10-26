package hw3

import breeze.linalg.{DenseMatrix, DenseVector, csvread}
import breeze.numerics.abs
import breeze.stats.{mean, meanAndVariance}

import java.io.{File, PrintWriter}
import java.util.logging.{FileHandler, Logger, SimpleFormatter}

object Utility {
  def mae(y: DenseVector[Double], y_hat: DenseVector[Double]): Double = {
    mean(abs(y_hat - y))
  }

  def getXyFromCsvPath(path: String, targetCol: Int): Tuple2[DenseMatrix[Double], DenseVector[Double]] = {
    val csvFile: File = new File(path)
    val data: DenseMatrix[Double] = csvread(csvFile, skipLines = 1)
    val maxColNum = data.cols - 1
    val cols = for (i <- 0 to maxColNum if i != targetCol) yield i
    val X = data(::, cols).toDenseMatrix
    val y: DenseVector[Double] = data(::, targetCol)
    (X, y)
  }

  def crossValidation(X: DenseMatrix[Double], y: DenseVector[Double], folds: Int = 5, logger: Logger): Unit = {
    val unionMatrix = DenseMatrix.horzcat(X, new DenseMatrix(y.length,1, y.toArray))
    val indexes = scala.util.Random.shuffle((0 until unionMatrix.rows).toList)
    val shuffled = unionMatrix(scala.util.Random.shuffle(indexes), ::).toDenseMatrix

    var prev_size = 0
    var fold = 1

    for (size <- shuffled.rows / 5 to shuffled.rows by shuffled.rows / folds) {
      logger.info(s"Current fold $fold")
      val sliceMatrixTest = shuffled(prev_size until size, ::)
      val sliceMatrixTrain = shuffled((0 to prev_size).toList ::: (size until shuffled.rows).toList, ::).toDenseMatrix
      val featuresTest = sliceMatrixTest(::, 0 until unionMatrix.cols - 1)
      val targetTest = sliceMatrixTest(::, - 1)

      val featuresTrain = sliceMatrixTrain(::, 0 until unionMatrix.cols - 1)
      val targetTrain = sliceMatrixTrain(::, - 1)

      for (idx <- 0 until featuresTrain.cols) {
        val stats = meanAndVariance(featuresTrain(::, idx))
        featuresTrain(::, idx) := featuresTrain(::, idx).toDenseVector.map(el => (el - stats.mean) / stats.variance)
        featuresTest(::, idx) := featuresTest(::, idx).toDenseVector.map(el => (el - stats.mean) / stats.variance)
      }
      val lr = new LinearRegression(logger)
      lr.fit(featuresTrain, targetTrain, epochs = 250, learningRate = 0.001)
      val maeTrain = mae(lr.predict(featuresTrain), targetTrain)
      logger.info(f"MAE train: $maeTrain%.2f on fold $fold")
      val maeTest = mae(lr.predict(featuresTest), targetTest)
      logger.info(f"MAE test: $maeTest%.2f on fold $fold")


      prev_size = size
      fold += 1
    }
  }

  def writeResultsToFile(path: String, iterator: DenseVector[Double]): Unit = {
    val writer = new PrintWriter(new File(path))

    for (el <- iterator) {
      writer.write(f"$el\n")
    }
    writer.close()
  }

  def getLogger(name: String, outputPath: String): Logger = {
    System.setProperty(
      "java.util.logging.SimpleFormatter.format",
      "%1$tF %1$tT %4$s %5$s%6$s%n"
    )

    val logger = Logger.getLogger(name)
    val handler = new FileHandler(outputPath)
    val formatter = new SimpleFormatter()
    handler.setFormatter(formatter)
    logger.addHandler(handler)
    logger
  }
}
