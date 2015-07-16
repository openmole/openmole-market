/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.geocite.simpoplocal.exploration

import cern.colt.list.DoubleArrayList
import umontreal.iro.lecuyer.probdist.LognormalDist
import umontreal.iro.lecuyer.probdist._
import umontreal.iro.lecuyer.gof.GofStat._
import java.util.logging.{ Level, Logger }

object LogNormalKSTest {

  def evalKs(sample: Array[Double], dist: LognormalDist) = {
    val data = new DoubleArrayList(sample)
    val dataUnif = unifTransform(data, dist)
    dataUnif.quickSortFromTo(0, dataUnif.size - 1)
    kolmogorovSmirnov(dataUnif)(2)
  }

  def computeMLE(sizeSample: Int, sample: Array[Double]): Array[Double] = LognormalDist.getMLE(sample, sizeSample)

  def test(samples: Array[Double]): Array[Boolean] = {
    try {
      val dist = {
        val mle = computeMLE(samples.size, samples)
        new LognormalDist(mle(0), mle(1))
      }

      val res = evalKs(samples, dist)

      val resultTest: Array[Boolean] = Array.fill(2) { true }
      if (res > getDCritical(samples.size, samples.size)) resultTest(0) = false
      if (pValue(samples.size, res) < 0.05) resultTest(1) = false

      return resultTest
    } catch {
      case e: IllegalArgumentException => Logger.getLogger(LogNormalKSTest.getClass.getName).log(Level.WARNING, "Numeric instability problem with log computation into MLE ssj function : " + samples.mkString(","), e)
      case e: Throwable => Logger.getLogger(LogNormalKSTest.getClass.getName).log(Level.WARNING, "Another unknown error, try and fail to resolve 42 :/ ", e)
    }
    Array(false, false)
  }

  def getDCritical(sizeSampleA: Double, sizeSampleB: Double): Double = {
    val alpha005 = 1.36
    val dcritique = alpha005 * math.sqrt((sizeSampleA + sizeSampleB) / (sizeSampleA * sizeSampleB))
    dcritique
  }

  def pValue(n: Int, v: Double): Double = KolmogorovSmirnovDistQuick.barF(n, v)

}
