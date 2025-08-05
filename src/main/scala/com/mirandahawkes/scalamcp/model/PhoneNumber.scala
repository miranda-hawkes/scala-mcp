package com.mirandahawkes.scalamcp.model

import zio.json.*

case class PhoneNumber(
    id: Long,
    `type`: Option[String],
    number: String
)

object PhoneNumber:
  implicit val decoder: JsonDecoder[PhoneNumber] =
    DeriveJsonDecoder.gen[PhoneNumber]
  implicit val encoder: JsonEncoder[PhoneNumber] =
    DeriveJsonEncoder.gen[PhoneNumber]