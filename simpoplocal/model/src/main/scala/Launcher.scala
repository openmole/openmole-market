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

import scala.util.Random

object Launcher extends App {

  val replications = 100

  val m = new SimpopLocal {
    def distanceDecay: Double = 0.6882107473716844
    def innovationImpact: Double = 0.007879556611500305
    def maxInnovation: Double = 10000
    def pCreation: Double = 1.2022185310640896E-6
    def pDiffusion: Double = 7.405303653131592E-7
    def rMax: Double = 10259.331894632433
  }

  val rng = new Random(42)

  val fitnesses = Iterator.continually(rng.nextLong).take(replications).toSeq.par.map {
    seed =>
      implicit val threadRng = new Random(seed)
      Fit(m.run)
  }

  val distribution = fitnesses.map { case Fit(d, _, _) => d }.sum
  val population = Median(fitnesses.map { case Fit(_, p, _) => p }.toArray)
  val duration = Median(fitnesses.map { case Fit(_, _, d) => d }.toArray)

  println(s"Fitness: distribution = $distribution, population = $population, duration = $duration")

}
