package test.option.iq

import test.option.iq.services.{SimpleScheduleRunManager, VacanciesFetchTaskFactory}
import test.option.iq.settings.{AppSettings, Settings}

//0. set system property
//1. Add config
//2. Close all what should be closed
//3. Logging
//4. retry and circute breaker
//5. smart fetching
//6. exceptions
object Fetcher extends App {

  System.setProperty("HADOOP_USER_NAME", "root")
  System.setProperty("hadoop.home.dir", "/")

  val settings: Settings = new AppSettings()
  val fetchTaskFactory = new VacanciesFetchTaskFactory(settings)
  val task = fetchTaskFactory.getFetchTask()

  val scheduleRunManager = new SimpleScheduleRunManager()
  scheduleRunManager.runTask(task)

}