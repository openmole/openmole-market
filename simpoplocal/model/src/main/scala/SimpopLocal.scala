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

import scala.collection.immutable.TreeSet
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Random
import math._

trait SimpopLocal {

  /// The average annual growth on the settlements in inhabitants per step
  def populationRate = 0.02

  /// The probability that an innovation emerges from the interaction between two individuals of the same settlement
  def pCreation: Double

  /// The probability that an innovation is transmitted between two individuals of different settlements
  def pDiffusion: Double

  /// The deterrent effect of the of distance on diffusion
  def distanceDecay: Double

  /// The impact of the acquisition of an innovation on the growth of a settlement
  def innovationImpact: Double

  /// The maximum carrying capacity of the landscape of each settlement (measured in number of inhabitants)
  def rMax: Double

  /// Length of time during which an innovation can be diffused from a settlement: passed this length of time this innovation becomes obsolete.
  def innovationLife: Int = 4000

  /// Max number of simulation step (years)
  def maxDate = 4000

  /// Max number of innovations that can be considered in the simulation
  def maxInnovation: Double

  /**
   *
   * @param step Simulation step.
   * @param settlements Set of settlement entities.
   * @param currentInnovationId Next id available for innovation creation.
   */
  case class SimpopLocalState(step: Int, settlements: Seq[Settlement], currentInnovationId: Int = 0)

  /**
   *
   * Class representing a settlement
   *
   * @param id Id of the settlement in the original data file.
   * @param x Horizontal coordinate.
   * @param y Vertical coordinate.
   * @param population Population of the settlement.
   * @param availableResource Amount of available resource: counted in number of inhabitants that this amount can sustain.
   * @param innovations Set of innovation entities acquired by the city.
   */
  case class Settlement(
    id: Int,
    x: Double,
    y: Double,
    population: Double,
    availableResource: Double,
    innovations: Set[Innovation])

  /**
   * Class representing innovations
   *
   * @param step Date of acquisition
   * @param rootId Id of the original created innovation from which this is copied.
   * @param id Id of the innovation. Equal to id if this innovation if it is a created (root) innovation.
   */
  case class Innovation(step: Int, rootId: Int, id: Int)

  /**
   * Order function of innovation. In each settlement innovations are kept ordered by rootId to speed
   * up the computation of the differences between sets of innovations acquired by 2 settlements.
   *
   * By design, each settlements cannot acquire innovations with the same rootId.
   */
  implicit lazy val innovationOrdering = Ordering.by((_: Innovation).rootId)

  /**
   * Compute the initial state
   * @param rng a random number generator
   * @return the initial state of the simulation
   */
  def initial(implicit rng: Random) =
    SimpopLocalState(0, readSettlements.map(_.toSettlement))

  /**
   *
   * A settlement as described in the initialisation file.
   *
   * For attributes documentation see @Settlement class.
   *
   *  @param settlementClass Settlements are distributed in 3 classes depending on their population size: 1 bigger, 2 medium, 3 smaller.
   */
  case class ReadSettlement(
      id: Int,
      x: Double,
      y: Double,
      population: Double,
      availableResource: Double,
      settlementClass: Int) {

    /// @return the range of interaction of this settlement.
    def radius =
      settlementClass match {
        case 1 => 20.0
        case 2 => 10.0
        case 3 => 5.0
        case _ => sys.error(s"Invalid settlement class $settlementClass")
      }

    /// @return an initial settlement for the simulation state
    def toSettlement = Settlement(id, x, y, population, availableResource, TreeSet.empty)
  }

  /// Read settlements from the file
  lazy val readSettlements = {
    val input =
      Source.fromInputStream(this.getClass.getClassLoader.getResourceAsStream("init-situation.txt"))

    /* Read File to create settlements with the matching attributes */
    input.getLines.map {
      line =>
        val parsed = line.split(",")
        ReadSettlement(
          id = parsed(0).toInt,
          x = parsed(1).toDouble,
          y = parsed(2).toDouble,
          population = parsed(3).toDouble,
          availableResource = parsed(4).toDouble,
          settlementClass = parsed(5).toInt)
    }.toArray.sortBy(_.id).toIndexedSeq
  }

  /**
   * Store a neighbour and it's distance to the original object
   * @param neighbour the neighbour
   * @param distance the distance to the original object
   * @tparam T the type of neighbour considered (in this simulation settlements)
   */
  case class Neighbour[T](neighbour: T, distance: Double)

  /**
   * Compute the euclidean distance between two settlements
   * @param p1 settlement 1
   * @param p2 settlement 2
   * @return the euclidean distance between settlement 1 and 2
   */
  def distance(p1: ReadSettlement, p2: ReadSettlement): Double =
    sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2))

  /**
   * Find all the neighbours, i.e settlements at a distance smaller than the radius
   * of a settlement.
   * @param all all the settlements
   * @param center the settlement at the center
   * @return the settlements at a distance smaller than the radius of the center settlement
   */
  def neighbours(all: Seq[ReadSettlement], center: ReadSettlement) =
    all.map {
      c => Neighbour(c, distance(center, c))
    }.filter {
      n => n.distance < center.radius && center.id != n.neighbour.id
    }

  /**
   * Settlements interaction network definition.
   */
  lazy val network = {
    val settlements = readSettlements

    // Partition settlements by class
    val settlementsClass1 = settlements.filter { _.settlementClass == 1 }
    val settlementsClass2 = settlements.filter { _.settlementClass == 2 }
    val settlementsClass3 = settlements.filter { _.settlementClass == 3 }

    /*
     * Compute the network of settlement possible interactions:
     * for each settlement it computes a list of all the settlements it can interact with and the distance to this settlement
     */
    settlements.map {
      settlement =>
        settlement.settlementClass match {
          case 1 =>
            //All settlements of class 1 are connected to all neighbouring settlements of class 1
            neighbours(settlementsClass1, settlement)
          case 2 =>
            //All settlements of class 2 are connected to all neighbouring settlements (class 3 to 1)
            neighbours(settlementsClass1, settlement) ++
              neighbours(settlementsClass2, settlement) ++
              neighbours(settlementsClass3, settlement)
          case 3 =>
            //All settlements of class 3 are connected to all neighbouring settlements (class 3 to 1)
            neighbours(settlementsClass1, settlement) ++
              neighbours(settlementsClass2, settlement) ++
              neighbours(settlementsClass3, settlement)
        }
    }
  }

  /**
   * Build an iterator which lazily compute the state at each step of the
   * simulation when traversed.
   * @param rng a random number generator
   * @return an iterator over the states of the simulation
   */
  def states(implicit rng: Random) =
    Iterator.iterate(initial)(step).takeWhile(!ended(_))

  /**
   * Compute the last state of the simulation
   * @param rng a random number generator
   * @return the last state of the simulation
   */
  def run(implicit rng: Random) = {
    def last(i: Iterator[SimpopLocalState]): SimpopLocalState = {
      val e = i.next
      if (i.hasNext) last(i)
      else e
    }
    last(states)
  }

  /**
   * Compute the end of the simulation
   * @param state the current state of the simulation
   * @return true if the simulation is completed
   */
  def ended(state: SimpopLocalState) =
    state.step >= maxDate || state.currentInnovationId > maxInnovation

  /**
   * Compute one step of the simulation
   * @param state current state
   * @param rng a random number generator
   * @return the next state
   */
  def step(state: SimpopLocalState)(implicit rng: Random) = {
    import state.settlements

    var currentInnovationId = state.currentInnovationId
    val newSettlements = ListBuffer[Settlement]()

    for {
      settlement <- settlements
    } {
      val (newSettlement, newId) = evolveSettlement(settlement, settlements, state.step, currentInnovationId)
      currentInnovationId = newId
      newSettlements += newSettlement
    }

    SimpopLocalState(state.step + 1, settlements = newSettlements, currentInnovationId)
  }

  /**
   *
   * Evolve a settlement (compute its new state).
   *
   * @param settlement The concerned settlement.
   * @param state Current state of the simulation.
   * @param step Current step of the simulation.
   * @return The new state of the settlement and the new value for next innovation id.
   */
  def evolveSettlement(settlement: Settlement, state: Seq[Settlement], step: Int, currentInnovationId: Int)(implicit rng: Random) = {
    val grownSettlement = growPopulation(settlement)
    val (settlementAfterDiffusion, newInnovationId) = diffusion(grownSettlement, state, step, currentInnovationId)
    creation(settlementAfterDiffusion, state, step, newInnovationId)
  }

  /**
   * Filters obsolete innovations from a set of innovations.
   * @param innovations The set innovations.
   * @param step The current step of the simulation.
   * @return The set of filtered innovations.
   */
  def filterObsolete(innovations: Iterable[Innovation], step: Int) =
    innovations.filter(step - _.step <= innovationLife)

  /**
   *
   * Computes the process of diffusion of innovations for a settlement.
   *
   * @param settlement The concerned settlement.
   * @param state The current state of the simulation.
   * @param step The current step of the simulation.
   * @param nextInnovationId Next id available for innovation creation.
   * @param rng The random number generator.
   * @return The new state of the settlement after the diffusion process and the new value for next innovation id.
   */
  def diffusion(settlement: Settlement, state: Seq[Settlement], step: Int, nextInnovationId: Int)(implicit rng: Random) = {
    val localNetwork = network(settlement.id)

    // Recovers all neighbours settlements (and their innovations) which succeed in the diffusion process test.
    val innovationPoolByCity =
      localNetwork.filter {
        neighbour =>
          settlement.innovations.size > 0 && diffuse(state(settlement.id).population, state(neighbour.neighbour.id).population, neighbour.distance)
      }

    /**
     *
     * Filters the set of exchangeable innovations: the set of innovations from a neighbour that are not already present in the concerned settlement and not obsolete.
     *
     * @param from The settlement from which the innovation are diffused.
     * @return The set of exchangeable innovations.
     */
    def exchangeableInnovations(from: Settlement) = filterObsolete(from.innovations &~ settlement.innovations, step)

    // Randomly draws an innovation for each the neighbour selected for diffusion.
    val innovationsFromNeighbours =
      innovationPoolByCity.flatMap {
        neighbour => randomElement(exchangeableInnovations(state(neighbour.neighbour.id)).toSeq)
      }

    // Randomly draws an innovation in case the same innovation (same rootId) is proposed by different neighbours.
    val distinctInnovations =
      innovationsFromNeighbours.groupBy(_.rootId).map {
        case (k, v) => randomElement(v).get
      }.toList

    // Copy the innovations acquired by diffusion.
    val copyOfInnovations =
      distinctInnovations.zipWithIndex.map {
        case (innovation, index) => Innovation(step = step, rootId = innovation.id, id = nextInnovationId + index)
      }

    (acquireInnovations(settlement, step, copyOfInnovations), nextInnovationId + copyOfInnovations.size)
  }

  /**
   *
   * Computes the process of creation of innovations for a settlement.
   *
   * @param settlement The concerned settlement.
   * @param state The current state of simulation.
   * @param step The current step of the simulation.
   * @param nextInnovationId Next id available for innovation creation.
   * @param rng The random number generator.
   * @return The new state of the settlement after the creation process and the new value for next innovation id.
   */
  def creation(settlement: Settlement, state: Seq[Settlement], step: Int, nextInnovationId: Int)(implicit rng: Random) =
    if (create(state(settlement.id).population)) {
      val innovation = Innovation(step = step, rootId = nextInnovationId, id = nextInnovationId)
      (acquireInnovations(settlement, step, Seq(innovation)), nextInnovationId + 1)
    } else (settlement, nextInnovationId)

  /**
   *
   * Randomly draws whether a creation of innovation occurs between two settlements.
   *
   * @param population1 The population of the first interacting settlement.
   * @param population2 The population of the second interacting settlement.
   * @param distance The distance between the two settlements.
   * @param rng The random number generator.
   * @return true if the diffusion occurs.
   */
  def diffuse(population1: Double, population2: Double, distance: Double)(implicit rng: Random) = {
    val population = population1 * population2
    val numberOfDistantInteractions = population / math.pow(distance, distanceDecay)
    val pBinomial = binomial(numberOfDistantInteractions, pDiffusion)
    rng.nextDouble <= pBinomial
  }

  /**
   *
   * Randomly draws whether a creation of innovation occurs in a settlement of a given population.
   *
   * @param population The population the settlement.
   * @param rng The random number generator.
   * @return true if the creation occurs.
   */
  def create(population: Double)(implicit rng: Random): Boolean = {
    val numberOfLocalInteractions = (1.0 / 2.0) * (population * (population - 1.0))
    val pBinomial = binomial(numberOfLocalInteractions, pCreation)
    rng.nextDouble <= pBinomial
  }

  /**
   *
   * Compute
   *
   * @param settlement The concerned settlement.
   * @return A new settlement state after evolution of it's population.
   */
  def growPopulation(settlement: Settlement) =
    settlement.copy(
      population =
        math.max(
          0.0,
          settlement.population + settlement.population * populationRate * (1.0 - settlement.population / settlement.availableResource)
        )
    )

  /**
   *
   * Computes the new state of a settlement after the acquisition of innovations. This method also impact the available resource of the settlement.
   *
   * @param settlement The concerned settlement.
   * @param step The current step of the simulation.
   * @param innovations The acquired innovations during the step.
   * @return The new state of the settlement
   */
  def acquireInnovations(settlement: Settlement, step: Int, innovations: Seq[Innovation]) =
    settlement.copy(
      availableResource = impactResource(settlement, innovations),
      innovations = settlement.innovations ++ innovations
    )

  /**
   *
   * Computes the effect of the acquisition of innovations on the available resource.
   *
   * @param settlement The concerned settlement.
   * @param innovations The acquired innovations.
   * @return The new amount of the available resource.
   */
  def impactResource(settlement: Settlement, innovations: Seq[Innovation]): Double =
    innovations.foldLeft(settlement.availableResource) {
      (resource, _) => resource * (1 + innovationImpact * (1 - resource / rMax))
    }

  /**
   *
   * Compute the probability of at least one success of a boolean random draw of probability of success p after n draws.
   *
   * @param n Number of draws.
   * @param p Probability of success of a single draw.
   * @return The probability of at least one success after n draws.
   */
  def binomial(n: Double, p: Double): Double = 1.0 - math.pow(1 - p, n)

  /**
   *
   * Draw a random element in a sequence.
   *
   * @param seq A sequence of elements.
   * @param rng The random number generator.
   * @tparam T The type of the elements.
   * @return A randomly drawn element.
   */
  def randomElement[T](seq: Seq[T])(implicit rng: Random) = if (seq.isEmpty) None else Some(seq(rng.nextInt(seq.size)))

}

