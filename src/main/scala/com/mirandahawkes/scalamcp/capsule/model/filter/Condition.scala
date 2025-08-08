package com.mirandahawkes.scalamcp.capsule.model.filter

import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import sttp.tapir.Schema.annotations.description
import zio.json.ast.Json
import zio.json.{JsonDecoder, JsonEncoder, jsonHint}

import scala.util.Try

// Jackson-style deserialisation needed for the under the hood @ToolParam binders in fast-mcp-scala
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(
      value = classOf[SimpleStringCondition],
      name = "string"
    ),
    new JsonSubTypes.Type(
      value = classOf[SimpleNumberCondition],
      name = "number"
    ),
    new JsonSubTypes.Type(
      value = classOf[SimpleDateCondition],
      name = "date"
    ),
    new JsonSubTypes.Type(
      value = classOf[SimpleBooleanCondition],
      name = "boolean"
    )
  )
)
sealed trait SimpleCondition:
  @description("the name of the field to filter on") val field: String
  @description(
    "the operator to use (must be valid for the field type)"
  ) val operator: String
  @description("the type of the field value") val `type`: String

object SimpleCondition:
  implicit val encoder: JsonEncoder[SimpleCondition] =
    JsonEncoder[Json].contramap { condition =>
      val valueFieldValue = condition match {
        case SimpleStringCondition(_, _, _, value) =>
          Json.Str(value)
        case SimpleDateCondition(_, _, _, value) =>
          Json.Str(value)
        case SimpleNumberCondition(_, _, _, value) =>
          Json.Num(value)
        case SimpleBooleanCondition(_, _, _, value) =>
          Json.Bool(value)
      }

      Json.Obj(
        "field" -> Json.Str(condition.field),
        "operator" -> Json.Str(condition.operator),
        "value" -> valueFieldValue
      )
    }
  // Custom decoder to automatically convert value to the correct type
  implicit val decoder: JsonDecoder[SimpleCondition] =
    JsonDecoder[Json.Obj].mapOrFail { jsonObj =>
      for {
        fieldNameJson <- jsonObj.get("field").toRight("missing field")
        operatorJson <- jsonObj.get("operator").toRight("missing operator")
        fieldValueJson <- jsonObj
          .get("value")
          .toRight("missing value")

        fieldName <- fieldNameJson
          .as[String]
          .left
          .map(_ => "field must be a string")
        operator <- operatorJson
          .as[String]
          .left
          .map(_ => "operator must be a string")

        condition <- fieldValueJson match
          case Json.Str(s) =>
            Right(
              SimpleStringCondition(
                field = fieldName,
                operator = operator,
                value = s
              )
            )
          case Json.Num(n) =>
            Try(n.longValue()).toOption
              .map(l =>
                SimpleNumberCondition(
                  field = fieldName,
                  operator = operator,
                  value = l
                )
              )
              .toRight("value is not a valid number")
          case Json.Bool(b) =>
            Right(
              SimpleBooleanCondition(
                field = fieldName,
                operator = operator,
                value = b
              )
            )
          case _ => Left("unsupported value type")
      } yield condition
    }

@jsonHint("string")
final case class SimpleStringCondition(
    `type`: String = "string",
    field: String,
    operator: String,
    @description(
      "the field value to filter on (must be valid for the field type)"
    ) value: String
) extends SimpleCondition derives JsonDecoder, JsonEncoder

@jsonHint("date")
final case class SimpleDateCondition(
    `type`: String = "date",
    field: String,
    operator: String,
    @description(
      "the field value to filter on (must be valid for the field type)"
    ) value: String
) extends SimpleCondition derives JsonDecoder, JsonEncoder

@jsonHint("number")
final case class SimpleNumberCondition(
    `type`: String = "number",
    field: String,
    operator: String,
    @description(
      "the field value to filter on (must be valid for the field type)"
    ) value: Long
) extends SimpleCondition derives JsonDecoder, JsonEncoder

@jsonHint("boolean")
final case class SimpleBooleanCondition(
    `type`: String = "boolean",
    field: String,
    operator: String,
    @description(
      "the field value to filter on (must be valid for the field type)"
    ) value: Boolean
) extends SimpleCondition derives JsonDecoder, JsonEncoder
