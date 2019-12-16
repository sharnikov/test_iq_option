package test.option.iq.settings

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{LocalFileSystem, Path}
import org.apache.hadoop.hdfs.DistributedFileSystem

import scala.concurrent.duration.Duration
import test.option.iq.settings.AppConfig._

object AppConfig {
  implicit class duration(duration: java.time.Duration) {
    def toScalaDuration() = Duration.fromNanos(duration.toNanos)
  }
}

trait AppConfig {
  def hh(): HH
  def hdfs(): Hdfs
  def scheduler(): Scheduler
}

class AppConfigImpl(config: Config) extends AppConfig {
  override def hh(): HH = new HHImpl(config.as[Config]("hh"))
  override def hdfs(): Hdfs = new HdfsImpl(config.as[Config]("hdfs"))
  override def scheduler(): Scheduler = new SchedulerImpl(config.as[Config]("scheduler"))
}

trait Scheduler {
  def delay(): Duration
  def repeatTime(): Duration
}

class SchedulerImpl(config: Config) extends Scheduler {
  override def delay(): Duration = config.getDuration("delay").toScalaDuration()
  override def repeatTime(): Duration = config.getDuration("repeat.interval").toScalaDuration()
}

trait HH {
  def userAgentHeader(): String
  def restUrl(): String
  def pagesAmount(): Int
}

class HHImpl(config: Config) extends HH {
  override def userAgentHeader(): String = config.getString("user-agent-header")
  override def restUrl(): String = config.getString("rest-url")
  override def pagesAmount(): Int = config.getInt("pages-amount")
}

trait Hdfs {
  def path(): Path
  def configuration(): Configuration
}

class HdfsImpl(config: Config) extends Hdfs {
  override def path(): Path = new Path(config.getString("path"))

  override def configuration(): Configuration = {
    val conf = new Configuration()
    val url = config.getString("address")
    conf.set("fs.defaultFS", url)
    conf.set("fs.hdfs.impl", classOf[DistributedFileSystem].getName)
    conf.set("fs.file.impl", classOf[LocalFileSystem].getName)

    conf
  }
}