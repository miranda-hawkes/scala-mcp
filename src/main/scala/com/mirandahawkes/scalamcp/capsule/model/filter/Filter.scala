package com.mirandahawkes.scalamcp.capsule.model.filter

import com.mirandahawkes.scalamcp.capsule.model.*
import sttp.tapir.Schema.annotations.*
import sttp.tapir.Validator
import zio.json.*
import zio.json.*
import zio.json.ast.*
import sttp.tapir.Schema
import sttp.tapir.*
import sttp.tapir.SchemaType
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

import scala.math.BigDecimal.javaBigDecimal2bigDecimal
import scala.util.Try
import java.math.BigDecimal
import sttp.tapir.generic.auto.*

case class Filter(conditions: List[SimpleCondition])
    derives JsonDecoder,
      JsonEncoder

case class FilterRequestWrapper(filter: Filter) derives JsonDecoder, JsonEncoder
