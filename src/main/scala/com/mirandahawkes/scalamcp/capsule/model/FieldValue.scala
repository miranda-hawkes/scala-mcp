package com.mirandahawkes.scalamcp.capsule.model

import zio.json.*

case class FieldValue(
    id: Long,
    value: String,
    definition: FieldDefinition
)

object FieldValue:
  implicit val decoder: JsonDecoder[FieldValue] =
    DeriveJsonDecoder.gen[FieldValue]
  implicit val encoder: JsonEncoder[FieldValue] =
    DeriveJsonEncoder.gen[FieldValue]
