/*
 * Copyright (C) 2011 srey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.geocite.simpoplocal.exploration.writer

import java.io.{ File, FileWriter, BufferedWriter, Writer }
import fr.geocite.simpoplocal.exploration._
import scala.util.Random

class CSVWriter(path: String, idExp: Int, seed: Long, each: Int = 0) {

  def apply(s: SimpopLocal)(implicit aprng: Random) = {
    val writer = new BufferedWriter(new FileWriter(new File(path)))
    try {
      writer.append("v_idn, v_exp, v_ticks, v_seed, v_pop, v_icreated, v_x, v_y" + "\n")
      s.states.foreach(stepWriter(_, writer, each))
    } finally writer.close
  }

  def stepWriter(dataToWrite: SimpopLocal#SimpopLocalState, writer: Writer, each: Int) = {
    writer.synchronized {
      val cities = dataToWrite.settlements
      val year = dataToWrite.step
      if ((each == 0) || ((year - 1) % each) == 0) {
        cities.map {
          c => writer.append(List[Any](c.id, idExp, year, seed, c.population, c.innovations.size, c.x, c.y).map { _.toString }.mkString(",") + "\n")
        }
      }
    }
  }

}
