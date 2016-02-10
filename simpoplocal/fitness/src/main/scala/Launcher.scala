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

package fr.geocites.simpoplocal.exploration

import scala.util.Random

object Launcher extends App {

  val replications = 100

  val m = new SimpopLocal {
    override def pCreation: Double = 0.000591716

    override def distanceDecay: Double = 3.2952594879

    override def maxInnovation: Double = 10000

    override def rMax: Double = 2

    override def innovationImpact: Double = 0.0164608187
    override def pDiffusion: Double = 0

    override def innovationLife = 690
  }

 /* val m = new SimpopLocal {
    def distanceDecay: Double = 0.6948037684879382
    def innovationImpact: Double = 0.008514548363730353
    def maxInnovation: Double = 10000
    def pCreation: Double = 8.672482701792705E-7
    def pDiffusion: Double = 8.672482701792705E-7
    def rMax: Double = 10586
  }*/

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

  val s = Seq(distribution / 200.0, population / 10000, duration/ 4000)
  println(s)
  println(s.max)

}
