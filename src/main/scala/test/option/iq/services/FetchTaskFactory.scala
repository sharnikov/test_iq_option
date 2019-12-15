package test.option.iq.services

import java.util.concurrent.TimeUnit
import spray.json._
import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.client.{Response, basicRequest, _}
import test.option.iq.services.FetchTaskFactory.FetchTask
import test.option.iq.settings.{MainContext, Settings}
import test.option.iq.utils.CsvConverter
import test.option.iq.utils.JsonParsers._
import test.option.iq.Vacancies

import scala.concurrent.Future

trait FetchTaskFactory {
    def getFetchTask(): FetchTask
}

class VacanciesFetchTaskFactory(settings: Settings) extends FetchTaskFactory
    with MainContext
    with CsvConverter
    with FilesWriter {

  private implicit val sttpBackend = AsyncHttpClientFutureBackend()

  override def getFetchTask(): FetchTask = {

    val task = new Runnable {
      override def run(): Unit = {
        println("Start fetching")
        Future.sequence((0 to 19).map(makeRequest)).map { requestsResult =>
          val items = extractItems(requestsResult)
          println(items)
          val csvLines = itemsToCsv(items)
          writeToHdfs(
            linesToWrite=csvLines,
            path=settings.buildHdfsFilePath(),
            configuration=settings.buildHdfsConfiguration()
          )

//          writeFile("data.csv", csvLines)
          println("File is written")
        }
      }
    }

    FetchTask(
      task = task,
      firstDelayTime = 0,
      repeateRate = 20,
      TimeUnit.SECONDS
    )

  }

  private def makeRequest(page: Int) = {
    basicRequest
      .get(uri"https://api.hh.ru/vacancies?area=2&vacancy_search_order=publication_time&per_page=100&page=$page")
      .header("User-Agent", settings.config().hh().userAgentHeader())
      .send()
  }

  private def extractItems(requestsResult: Seq[Response[Either[String, String]]]) = {
    requestsResult.map(_.body).flatMap {
      case Right(result) =>
        result.parseJson.convertTo[Vacancies].items

      case Left(shit) =>
        println(shit)
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