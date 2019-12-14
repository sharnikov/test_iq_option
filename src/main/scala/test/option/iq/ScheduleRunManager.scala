package test.option.iq

import test.option.iq.FetchTaskFactory.FetchTask

trait ScheduleRunManager {
  def runTask(task: FetchTask): Unit
}

class SimpleScheduleRunManager extends ScheduleRunManager {

  private val executor = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

  override def runTask(task: FetchTask): Unit = {
    executor.scheduleAtFixedRate(
      task.task,
      task.firstDelayTime,
      task.repeateRate,
      task.timeUnit
    )
  }
}
