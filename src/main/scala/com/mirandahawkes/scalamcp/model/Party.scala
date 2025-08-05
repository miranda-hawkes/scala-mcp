package com.mirandahawkes.scalamcp.model

import zio.json.*

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

@jsonHint("person")
case class Person(
    id: Long,
    `type`: String = "person",
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

@jsonHint("organisation")
case class Organisation(
    id: Long,
    `type`: String = "organisation",
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

// Nested supporting classes
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

case class PhoneNumber(
    id: Long,
    `type`: Option[String],
    number: String
)

object PhoneNumber:
  implicit val decoder: JsonDecoder[PhoneNumber] =
    DeriveJsonDecoder.gen[PhoneNumber]

case class Website(
    id: Long,
    service: String,
    address: String,
    `type`: Option[String],
    url: String
)

object Website:
  implicit val decoder: JsonDecoder[Website] = DeriveJsonDecoder.gen[Website]

case class EmailAddress(
    id: Long,
    `type`: Option[String],
    address: String
)
object EmailAddress:
  implicit val decoder: JsonDecoder[EmailAddress] =
    DeriveJsonDecoder.gen[EmailAddress]

// End nested supporting classes

case class Tag(
    id: Long,
    name: String,
    description: Option[String]
)

object Tag:
  implicit val decoder: JsonDecoder[Tag] = DeriveJsonDecoder.gen[Tag]

case class FieldDefinition(
    id: String,
    name: String
)

object FieldDefinition:
  implicit val decoder: JsonDecoder[FieldDefinition] =
    DeriveJsonDecoder.gen[FieldDefinition]

case class FieldValue(
    id: Long,
    value: String,
    definition: FieldDefinition
)

object FieldValue:
  implicit val decoder: JsonDecoder[FieldValue] =
    DeriveJsonDecoder.gen[FieldValue]

case class User(
    id: Long,
    username: String,
    name: String
)

object User:
  implicit val decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]

case class Team(
    id: Long,
    name: Option[String]
)

object Team:
  implicit val decoder: JsonDecoder[Team] = DeriveJsonDecoder.gen[Team]

case class PartyListWrapper(parties: List[Party])

object PartyListWrapper:
  implicit val decoder: JsonDecoder[PartyListWrapper] =
    DeriveJsonDecoder.gen[PartyListWrapper]
