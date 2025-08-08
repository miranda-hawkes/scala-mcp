package com.mirandahawkes.scalamcp.capsule.model

import zio.json.*

enum PartyType:
  case person, organisation

@jsonDiscriminator("type")
sealed trait Party:
  val id: Long
  val `type`: String
  val about: Option[String]
  val createdAt: Option[String]
  val updatedAt: Option[String]
  val lastContactedAt: Option[String]
  val addresses: Option[List[Address]]
  val phoneNumbers: Option[List[PhoneNumber]]
  val websites: Option[List[Website]]
  val emailAddresses: Option[List[EmailAddress]]
  val pictureURL: Option[String]
  val tags: Option[List[Tag]]
  val fields: Option[List[FieldValue]]
  val owner: Option[User]
  val team: Option[Team]
  val missingImportantFields: Option[Boolean]

object Party:
  implicit val decoder: JsonDecoder[Party] = DeriveJsonDecoder.gen[Party]
  implicit val encode: JsonEncoder[Party] = DeriveJsonEncoder.gen[Party]

@jsonHint(PartyType.person.toString)
case class Person(
    id: Long,
    `type`: String = PartyType.person.toString,
    firstName: Option[String],
    lastName: Option[String],
    title: Option[String],
    jobTitle: Option[String],
    organisation: Option[Organisation],
    about: Option[String],
    createdAt: Option[String],
    updatedAt: Option[String],
    lastContactedAt: Option[String],
    addresses: Option[List[Address]],
    phoneNumbers: Option[List[PhoneNumber]],
    websites: Option[List[Website]],
    emailAddresses: Option[List[EmailAddress]],
    pictureURL: Option[String],
    tags: Option[List[Tag]],
    fields: Option[List[FieldValue]],
    owner: Option[User],
    team: Option[Team],
    missingImportantFields: Option[Boolean]
) extends Party

object Person:
  implicit val decoder: JsonDecoder[Person] = DeriveJsonDecoder.gen[Person]
  implicit val encode: JsonEncoder[Person] = DeriveJsonEncoder.gen[Person]

@jsonHint(PartyType.organisation.toString)
case class Organisation(
    id: Long,
    `type`: String = PartyType.organisation.toString,
    name: Option[String],
    about: Option[String],
    createdAt: Option[String],
    updatedAt: Option[String],
    lastContactedAt: Option[String],
    addresses: Option[List[Address]],
    phoneNumbers: Option[List[PhoneNumber]],
    websites: Option[List[Website]],
    emailAddresses: Option[List[EmailAddress]],
    pictureURL: Option[String],
    tags: Option[List[Tag]],
    fields: Option[List[FieldValue]],
    owner: Option[User],
    team: Option[Team],
    missingImportantFields: Option[Boolean]
) extends Party

object Organisation:
  implicit val decoder: JsonDecoder[Organisation] =
    DeriveJsonDecoder.gen[Organisation]
  implicit val encode: JsonEncoder[Organisation] =
    DeriveJsonEncoder.gen[Organisation]
