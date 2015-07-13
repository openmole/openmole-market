package hellotask

import org.openmole.core.dsl._
import org.openmole.core.workflow.data._
import org.openmole.plugin.task.scala._

object HelloTask {
  def apply(name: Prototype[String], output: Prototype[String]) =
    ScalaTask(
      """|val output = s"Hello ${name}!"
         |""".stripMargin) set (
        inputs += name,
        outputs += output
      )
}

