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

import fr.geocite.simpoplocal.exploration.writer.CSVWriter
import scala.util.Random
import java.io.{ File, FileWriter, BufferedWriter }

object WriteResultGraph2 extends App {

  val replications = 100
  val folderPath = "/tmp/"
  val each = 50

  val m = new SimpopLocal {
    def distanceDecay: Double = 0.6882107473716844
    def innovationImpact: Double = 0.007879556611500305
    def maxInnovation: Double = 10000
    def pCreation: Double = 1.2022185310640896E-6
    def pDiffusion: Double = 7.405303653131592E-7
    def rMax: Double = 10259.331894632433
  }

  val rng = new Random(42)

  val simulation = Iterator.continually(rng.nextLong).take(replications).toSeq.par.map {
    seed =>
      val file = folderPath + "slocal_" + seed.toString() + ".csv"
      implicit val threadRng = new Random(seed)
      new CSVWriter(file, 0, seed, each).apply(m)(threadRng)
  }

}
