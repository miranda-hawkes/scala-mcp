package com.mirandahawkes.scalamcp.model

import zio.json.*

case class Tag(
    id: Long,
    name: String,
    description: Option[String]
)

object Tag:
  implicit val decoder: JsonDecoder[Tag] = DeriveJsonDecoder.gen[Tag]
  implicit val encoder: JsonEncoder[Tag] = DeriveJsonEncoder.gen[Tag]