package test.option.iq

import java.io.{BufferedWriter, File, FileWriter}
import java.net.URI

import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.client._
import spray.json._
import JsonParsers._
import Utils.getCvsHeader
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

  implicit val sttpBackend = AsyncHttpClientFutureBackend()

  implicit val context = ExecutionContext.fromExecutor(java.util.concurrent.Executors.newWorkStealingPool())

  def writeFile(filename: String, lines: Seq[String]): Unit = {
    val header = getCvsHeader().mkString(";")
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))

    try {
      bw.write(header)
      lines.foreach(bw.write)
    } finally {
      bw.close()
    }
  }

  def itemsToCsv(items: Seq[Items]): Seq[String] = {

    implicit class DefValue[T](value: Option[T]) {
      def dv = value.getOrElse("")
    }

    items.map { item =>
      new StringBuilder(f"${item.id};")
        .append(f"${item.premium};")
        .append(f"${item.name};")
        .append(f"${item.department.map(_.id).dv};")
        .append(f"${item.department.map(_.name).dv};")
        .append(f"${item.has_test};")
        .append(f"${item.response_letter_required};")
        .append(f"${item.area.id};")
        .append(f"${item.area.name};")
        .append(f"${item.salary.flatMap(_.from).dv};")
        .append(f"${item.salary.flatMap(_.to).dv};")
        .append(f"${item.salary.map(_.currency).dv};")
        .append(f"${item.salary.flatMap(_.gross).dv};")

        .append(f"${item.address.flatMap(_.street).dv};")
        .append(f"${item.address.flatMap(_.building).dv};")
        .append(f"${item.address.flatMap(_.description).dv};")
        .append(f"${item.address.flatMap(_.lat).dv};")
        .append(f"${item.address.flatMap(_.lng).dv};")
        .append(f"${item.address.flatMap(_.raw).dv};")
        .append(f"${item.address.flatMap(_.id).dv};")

        .append(f"${item.employer.id.dv};")
        .append(f"${item.employer.name.dv};")
        .append(f"${item.employer.url.dv};")
        .append(f"${item.employer.vacancies_url.dv};")
        .append(f"${item.employer.trusted.dv};")

        .append(f"${item.created_at};")
        .append(f"${item.url};")
        .append(f"${item.alternate_url};")
        .append(f"${item.snippet.requirement.dv};")
        .append(f"${item.snippet.responsibility.dv}")
        .append("\n").toString()
    }
  }

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

  def writeToHdfs(filename: String, lines: Seq[String]) = {
    val conf = new Configuration()
    val url = s"hdfs://172.17.0.2:9000/"
    conf.set("fs.defaultFS", url)
    conf.set("fs.hdfs.impl", classOf[DistributedFileSystem].getName)
    conf.set("fs.file.impl", classOf[LocalFileSystem].getName)
    val fs = FileSystem.get(URI.create(url), conf)

    System.setProperty("HADOOP_USER_NAME", "root")
    System.setProperty("hadoop.home.dir", "/")

    val path = new Path(s"/$filename")
    val output = fs.create(path)
    val writer = new java.io.PrintWriter(output)

    val header = getCvsHeader().mkString(";")

    try {
      writer.write(header)
      lines.foreach(writer.write)
    } finally {
      writer.close()
      fs.close()
    }

  }

  Future.sequence((0 to 19).map(makeRequest)).map { requestsResult =>
    val items = getAllItems(requestsResult)
    println("Got items")
    val parsedStrings = itemsToCsv(items)
    writeFile("data.csv", parsedStrings)
    println("File is written")
  }
}