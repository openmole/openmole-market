package hellotask

import org.openmole.core.dsl._
import org.openmole.core.workflow.data._
import org.openmole.core.workflow.task._

object HelloTask {
  def apply(name: Prototype[String], output: Prototype[String]) =
    ClosureTask("HelloTask") { (ctx, _, _) =>
      Context(output -> s"Hello ${ctx(name)}!")
    } set (
      inputs += name,
      outputs += output
    )
}

