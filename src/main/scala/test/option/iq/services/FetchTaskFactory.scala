package test.option.iq.services

import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.LazyLogging
import spray.json._
import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.client.{Response, basicRequest, _}
import test.option.iq.services.FetchTaskFactory.FetchTask
import test.option.iq.settings.{AppConfig, MainContext}
import test.option.iq.utils.CsvConverter
import test.option.iq.utils.JsonParsers._
import test.option.iq.Vacancies

import scala.concurrent.Future

trait FetchTaskFactory {
    def getFetchTask(): FetchTask
}

class VacanciesFetchTaskFactory(config: AppConfig) extends FetchTaskFactory
    with MainContext
    with CsvConverter
    with FilesWriter
    with LazyLogging {

  private implicit val sttpBackend = AsyncHttpClientFutureBackend()

  override def getFetchTask(): FetchTask = {

    val task = new Runnable {
      override def run(): Unit = {
        logger.info("Start fetching data")
        val pagesInterval = 0 to config.hh().pagesAmount()
        Future.sequence(pagesInterval.map(makeRequest)).map { requestsResult =>
          val items = extractItems(requestsResult)
          val csvLines = itemsToCsv(items)
          writeToHdfs(
            linesToWrite=csvLines,
            path=config.hdfs().path(),
            configuration=config.hdfs().configuration()
          )

          logger.info("File is written time to hdfs")
        }
      }
    }

    FetchTask(
      task = task,
      firstDelayTime = 30,
      repeateRate = 30,
      TimeUnit.SECONDS
    )

  }

  private def makeRequest(page: Int) = {
    basicRequest
      .get(uri"${config.hh().restUrl()}$page")
      .header("User-Agent", config.hh().userAgentHeader())
      .send()
  }

  private def extractItems(requestsResult: Seq[Response[Either[String, String]]]) = {
    requestsResult.map(_.body).flatMap {
      case Right(result) =>
        result.parseJson.convertTo[Vacancies].items

      case Left(error) =>
        logger.error(error)
        Nil
    }
  }
}

object FetchTaskFactory {

  case class FetchTask(task: Runnable,
                       firstDelayTime: Long,
                       repeateRate: Long,
                       timeUnit: TimeUnit)
}