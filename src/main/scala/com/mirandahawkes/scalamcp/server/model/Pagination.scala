package com.mirandahawkes.scalamcp.server.model

import sttp.tapir.Schema.annotations.*
import zio.json.{JsonDecoder, JsonEncoder}

case class Pagination(
    @description("Page number") @default(Pagination.DEFAULT_PAGE) page: Int =
      Pagination.DEFAULT_PAGE,
    @description("Page size") @default(
      Pagination.DEFAULT_PER_PAGE
    ) perPage: Int = Pagination.DEFAULT_PER_PAGE
) derives JsonDecoder,
      JsonEncoder:

  def toMap: Map[String, Int] =
    this.productElementNames
      .zip(this.productIterator)
      .collect { case (name, value: Int) =>
        name -> value
      }
      .toMap

object Pagination:
  val DEFAULT_PAGE: Int = 1
  val DEFAULT_PER_PAGE: Int = 100
