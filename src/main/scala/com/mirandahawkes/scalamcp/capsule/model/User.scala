package com.mirandahawkes.scalamcp.capsule.model

import zio.json.*

case class User(
    id: Long,
    username: String,
    name: String
)

object User:
  implicit val decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
  implicit val encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]