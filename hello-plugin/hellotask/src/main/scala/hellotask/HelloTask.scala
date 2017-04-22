package hellotask

import org.openmole.core.dsl._
import org.openmole.core.workflow.task._
import org.openmole.core.context.Context

object HelloTask {
  def apply(name: Val[String], output: Val[String]) =
    ClosureTask("HelloTask") { (ctx, _, _) =>
      Context(output -> s"Hello ${ctx(name)}!")
    } set (
      inputs += name,
      outputs += output
    )
}

