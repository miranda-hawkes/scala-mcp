package com.mirandahawkes.scalamcp.util

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.time.Instant

class FileLogger(className: String):

  import FileLogger._

  def info(msg: String): Unit = log("INFO", msg)

  def warn(msg: String): Unit = log("WARN", msg)

  def error(msg: String): Unit = log("ERROR", msg)

  private def log(level: String, msg: String): Unit =
    val timestamp = Instant.now().toString // ISO-8601 UTC timestamp
    val line = s"$timestamp $level [${className}] $msg\n"
    Files.write(
      logPath,
      line.getBytes(StandardCharsets.UTF_8),
      StandardOpenOption.CREATE,
      StandardOpenOption.APPEND
    )

object FileLogger:
  private val logPath = Paths.get("scala-mcp.log")

  // Initialise log file on startup (create or truncate)
  def init(): Unit =
    Files.write(
      logPath,
      Array.emptyByteArray,
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING
    )

  def apply(name: String): FileLogger = new FileLogger(name)
