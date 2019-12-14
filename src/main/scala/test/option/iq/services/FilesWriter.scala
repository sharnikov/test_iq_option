package test.option.iq.services

import java.io.{BufferedWriter, File, FileWriter}
import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

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
      writer.close()
      output.close()
      hdfs.close()
    }
  }

  def writeFile(filename: String,
                linesToWrite: Seq[String]): Unit = {
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))

    try {
      linesToWrite.foreach(bw.write)
    } finally {
      bw.close()
    }
  }
}
