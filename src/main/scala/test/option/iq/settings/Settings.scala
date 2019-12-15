package test.option.iq.settings

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{LocalFileSystem, Path}
import org.apache.hadoop.hdfs.DistributedFileSystem

trait Settings {
  def buildHdfsConfiguration(): Configuration
  def buildHdfsFilePath(): Path
  def config(): Config
}

class AppSettings extends Settings {

  override def buildHdfsConfiguration(): Configuration = {
    val conf = new Configuration()
    val url = "hdfs://172.16.0.2:9000/"
    conf.set("fs.defaultFS", url)
    conf.set("fs.hdfs.impl", classOf[DistributedFileSystem].getName)
    conf.set("fs.file.impl", classOf[LocalFileSystem].getName)

    conf
  }

  override def buildHdfsFilePath(): Path = {
    new Path("/data.csv")
  }

  override def config(): Config = new AppConfig()
}
