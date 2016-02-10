/*
 * Copyright (C) 2011 Sebastien Rey
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

package fr.geocites.simpoplocal.exploration

import java.util.Random

object Model {

  def run(
    _rMax: Double,
    _innovationImpact: Double,
    _distanceDecay: Double,
    _pCreation: Double,
    _pDiffusion: Double,
    _maxInnovation: Double,
    _innovationLife: Int)(implicit rng: Random): ModelResult = {
    val simu =
      new SimpopLocal {
        def distanceDecay = _distanceDecay
        def pDiffusion = _pDiffusion
        def pCreation = _pCreation
        def innovationImpact = _innovationImpact
        def maxInnovation = _maxInnovation
        def rMax = _rMax
        override def innovationLife = _innovationLife
      }
    ModelResult(simu.run(new util.Random(rng)))
  }

}
