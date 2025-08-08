package com.mirandahawkes.scalamcp.capsule.model

import zio.json.*

case class Meta(totalCount: Long)

object Meta:
  implicit val decoder: JsonDecoder[Meta] =
    DeriveJsonDecoder.gen[Meta]
  implicit val encode: JsonEncoder[Meta] =
    DeriveJsonEncoder.gen[Meta]
