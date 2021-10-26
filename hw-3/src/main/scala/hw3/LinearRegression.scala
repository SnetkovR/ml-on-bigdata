package hw3

import java.util.logging.Logger

import breeze.linalg.{DenseMatrix, DenseVector}

import hw3.Utility.mae

class LinearRegression(lrLogger: Logger) {
  var w: DenseVector[Double] = DenseVector.fill(1)(0.2);
  var logger: Logger = lrLogger
  val loggerStep = 50

  def fit(X: DenseMatrix[Double], y: DenseVector[Double], epochs: Int = 100, learningRate: Double = 0.0001): Unit = {
    val ones = DenseMatrix.fill[Double](X.rows, 1)(1)
    // Добавляем bias
    val X_ = DenseMatrix.horzcat(ones, X)
    w = DenseVector.fill(X_.cols)(0.15)
    for (epoch <- 0 until epochs) {
      for (i <- 0 until X_.rows) {
        val grad = X_(i, ::) * (X_(i, ::) * w - y(i))
        w = w - learningRate * grad.t
      }
      val score = mae(y, X_ * w)
      if (epoch % loggerStep == 0) {
        logger.info(f"Epoch: $epoch, MAE=$score%.2f")
      }
    }
  }

  def predict(X: DenseMatrix[Double]): DenseVector[Double] = {
    val ones = DenseMatrix.fill[Double](X.rows, 1)(1)
    val X_ = DenseMatrix.horzcat(ones, X)
    X_ * w
  }


}
