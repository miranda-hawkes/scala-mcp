package com.mirandahawkes.scalamcp.model

import zio.json.*

case class PartyListWrapper(parties: List[Party])

object PartyListWrapper:
  implicit val decoder: JsonDecoder[PartyListWrapper] =
    DeriveJsonDecoder.gen[PartyListWrapper]

  implicit val encoder: JsonEncoder[PartyListWrapper] =
    DeriveJsonEncoder.gen[PartyListWrapper]
