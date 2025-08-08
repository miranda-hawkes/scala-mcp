package com.mirandahawkes.scalamcp.capsule.model

import zio.json.*

case class Address(
    id: Long,
    `type`: String,
    street: Option[String],
    city: Option[String],
    state: Option[String],
    country: Option[String],
    zip: Option[String]
)

object Address:
  implicit val decoder: JsonDecoder[Address] = DeriveJsonDecoder.gen[Address]
  implicit val encoder: JsonEncoder[Address] = DeriveJsonEncoder.gen[Address]
