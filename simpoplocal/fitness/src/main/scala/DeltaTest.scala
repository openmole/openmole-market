/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.geocites.simpoplocal.exploration

object DeltaTest {
  def delta(simulValue: Array[Double], theoricalValue: Double): Double = math.abs(simulValue.max - theoricalValue)
  def delta(simulValue: Double, theoricalValue: Double): Double = math.abs(simulValue - theoricalValue)
}
