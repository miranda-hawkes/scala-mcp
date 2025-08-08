package com.mirandahawkes.scalamcp.capsule.model

import zio.json.*

case class PartyResponse(parties: List[Party], meta: Meta)

object PartyResponse:
  implicit val decoder: JsonDecoder[PartyResponse] =
    DeriveJsonDecoder.gen[PartyResponse]

  implicit val encoder: JsonEncoder[PartyResponse] =
    DeriveJsonEncoder.gen[PartyResponse]
