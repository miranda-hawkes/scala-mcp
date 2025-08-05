package com.mirandahawkes.scalamcp.model

import zio.json.*

case class EmailAddress(
    id: Long,
    `type`: Option[String],
    address: String
)

object EmailAddress:
  implicit val decoder: JsonDecoder[EmailAddress] =
    DeriveJsonDecoder.gen[EmailAddress]
  implicit val encoder: JsonEncoder[EmailAddress] =
    DeriveJsonEncoder.gen[EmailAddress]
