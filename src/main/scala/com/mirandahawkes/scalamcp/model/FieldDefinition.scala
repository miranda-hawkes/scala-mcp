package com.mirandahawkes.scalamcp.model

import zio.json.*

case class FieldDefinition(
    id: String,
    name: String
)

object FieldDefinition:
  implicit val decoder: JsonDecoder[FieldDefinition] =
    DeriveJsonDecoder.gen[FieldDefinition]
  implicit val encoder: JsonEncoder[FieldDefinition] =
    DeriveJsonEncoder.gen[FieldDefinition]
