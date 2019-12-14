package test.option.iq.services

import java.io.{BufferedWriter, File, FileWriter}
import java.net.URI
import java.util.concurrent.TimeUnit

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, LocalFileSystem, Path}
import org.apache.hadoop.hdfs.DistributedFileSystem
import spray.json._
import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.client.{Response, basicRequest, _}
import test.option.iq.services.FetchTaskFactory.FetchTask
import test.option.iq.settings.{MainContext, Settings}
import test.option.iq.utils.CsvConverter
import test.option.iq.utils.JsonParsers._
import test.option.iq.{Items, Vacancies}

import scala.concurrent.Future

trait FetchTaskFactory {
    def getFetchTask(): FetchTask
}

class VacanciesFetchTaskFactory(setting: Settings) extends FetchTaskFactory
    with MainContext
    with CsvConverter
    with FilesWriter {

  private implicit val sttpBackend = AsyncHttpClientFutureBackend()

  override def getFetchTask(): FetchTask = {

    val task = new Runnable {
      override def run(): Unit = {

        def makeRequest(page: Int) = {
          basicRequest
            .get(uri"https://api.hh.ru/vacancies?area=2&vacancy_search_order=publication_time&per_page=100&page=$page")
            .header("User-Agent", "IQ-Option-Test-App/1.0 (oleg.sharnikov@outlook.com)")
            .send()
        }

        def getAllItems(requestsResult: Seq[Response[Either[String, String]]]) = {
          requestsResult.map(_.body).flatMap {
            case Right(result) =>
              result.parseJson.convertTo[Vacancies].items

            case Left(shit) =>
              println(shit)
              Nil
          }
        }

        Future.sequence((0 to 19).map(makeRequest)).map { requestsResult =>
          val items = getAllItems(requestsResult)
          println("Got items")
          val csvLines = itemsToCsv(items)
//          writeToHdfs(
//            linesToWrite=csvLines,
//            path=setting.buildHdfsFilePath(),
//            configuration=setting.buildHdfsConfiguration()
//          )

          writeFile("data.csv", csvLines)
          println("File is written")
        }
      }
    }

    FetchTask(
      task = task,
      firstDelayTime = 0,
      repeateRate = 1,
      TimeUnit.MINUTES
    )

  }
}

object FetchTaskFactory {

  case class FetchTask(task: Runnable,
                       firstDelayTime: Long,
                       repeateRate: Long,
                       timeUnit: TimeUnit)
}