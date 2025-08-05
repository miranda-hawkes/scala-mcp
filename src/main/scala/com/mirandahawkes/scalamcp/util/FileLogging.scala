package com.mirandahawkes.scalamcp.util

trait FileLogging:
  @transient
  protected lazy val logger: FileLogger = new FileLogger(
    this.getClass.getSimpleName.stripSuffix("$")
  )
