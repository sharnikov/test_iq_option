package test.option.iq

import java.io.{BufferedWriter, File, FileWriter}
import java.net.URI
import java.util.concurrent.TimeUnit

import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.client._
import spray.json._
import JsonParsers._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, LocalFileSystem, Path}
import org.apache.hadoop.hdfs.DistributedFileSystem

import scala.concurrent.{ExecutionContext, Future}

//1. Add config
//2. Separate all the shit
//3. Close all what should be closed
//4. Run every "period of time"
//5. Logging
//6. Maybe testing
//7. refactor sbt
//8. retry and circute breaker
//9. smart fetching
object Fetcher extends App {

  val fetchTaskFactory = new VacanciesFetchTaskFactory()
  val task = fetchTaskFactory.getFetchTask()

  val scheduleRunManager = new SimpleScheduleRunManager()
  scheduleRunManager.runTask(task)

}