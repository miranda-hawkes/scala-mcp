package com.mirandahawkes.scalamcp.model

import zio.json.*

case class Team(
    id: Long,
    name: Option[String]
)

object Team:
  implicit val decoder: JsonDecoder[Team] = DeriveJsonDecoder.gen[Team]
  implicit val encoder: JsonEncoder[Team] =
    DeriveJsonEncoder.gen[Team]