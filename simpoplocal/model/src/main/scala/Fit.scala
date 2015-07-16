/*
 * Copyright (C) 25/04/13 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.geocite.simpoplocal.exploration

object Fit {

  def apply(state: SimpopLocal#SimpopLocalState): Fit = apply(ModelResult(state))

  def apply(result: ModelResult, populationObj: Int = 10000, timeObj: Int = 4000): Fit =
    Fit(
      ks = LogNormalKSTest.test(result.population).count(_ == false).toDouble,
      deltaPop = DeltaTest.delta(result.population, populationObj),
      deltaTime = DeltaTest.delta(result.time, timeObj)
    )

}

case class Fit(ks: Double, deltaPop: Double, deltaTime: Double)