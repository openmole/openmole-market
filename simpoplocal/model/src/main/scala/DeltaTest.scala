/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.geocite.simpoplocal.exploration

object DeltaTest {


  def delta(simulValue: Array[Double], theoricalValue: Double): Double = {
    val res = math.abs(simulValue.max - theoricalValue)
    if(res.isNaN) Double.PositiveInfinity
    else res
  }
  def delta(simulValue: Double, theoricalValue: Double): Double = {
    val res = math.abs(simulValue - theoricalValue)
    if(res.isNaN) Double.PositiveInfinity
    else res
  }
}
