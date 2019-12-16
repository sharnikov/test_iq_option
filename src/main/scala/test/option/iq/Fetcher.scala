package test.option.iq
import java.io.File

import com.typesafe.config.ConfigFactory
import test.option.iq.services.{SimpleScheduleRunManager, VacanciesFetchTaskFactory}
import test.option.iq.settings.{AppConfig, AppConfigImpl}

object Fetcher extends App {

  System.setProperty("HADOOP_USER_NAME", "root")
  System.setProperty("hadoop.home.dir", "/")

  val config: AppConfig = new AppConfigImpl(ConfigFactory.parseFile(new File("src/main/resources/app.conf")))
  val fetchTaskFactory = new VacanciesFetchTaskFactory(config)
  val task = fetchTaskFactory.getFetchTask()

  val scheduleRunManager = new SimpleScheduleRunManager()
  scheduleRunManager.runTask(task)
}