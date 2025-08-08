package com.mirandahawkes.scalamcp.capsule.model

import zio.json.*

case class Website(
    id: Long,
    service: String,
    address: String,
    `type`: Option[String],
    url: String
)

object Website:
  implicit val decoder: JsonDecoder[Website] = DeriveJsonDecoder.gen[Website]
  implicit val encoder: JsonEncoder[Website] = DeriveJsonEncoder.gen[Website]
