package test.option.iq.services

import java.io.{BufferedWriter, File, FileWriter}
import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.IOUtils

trait FilesWriter {

  def writeToHdfs(linesToWrite: Seq[String],
                  path: Path,
                  configuration: Configuration): Unit = {

    val url = configuration.get("fs.defaultFS")

    val hdfs = FileSystem.get(URI.create(url), configuration)
    val output = hdfs.create(path)
    val writer = new java.io.PrintWriter(output)

    try {
      linesToWrite.foreach(writer.write)
    } finally {
      IOUtils.closeStream(writer)
      IOUtils.closeStream(output)
      IOUtils.closeStream(hdfs)
    }
  }

  def writeFile(filename: String,
                linesToWrite: Seq[String]): Unit = {
    val file = new File(filename)
    val bufferedWriter = new BufferedWriter(new FileWriter(file))

    try {
      linesToWrite.foreach(bufferedWriter.write)
    } finally {
      IOUtils.closeStream(bufferedWriter)
    }
  }
}
