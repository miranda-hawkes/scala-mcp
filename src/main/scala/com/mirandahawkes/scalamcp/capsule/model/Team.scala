package com.mirandahawkes.scalamcp.capsule.model

import zio.json.*

case class Team(
    id: Long,
    name: Option[String]
)

object Team:
  implicit val decoder: JsonDecoder[Team] = DeriveJsonDecoder.gen[Team]
  implicit val encoder: JsonEncoder[Team] =
    DeriveJsonEncoder.gen[Team]