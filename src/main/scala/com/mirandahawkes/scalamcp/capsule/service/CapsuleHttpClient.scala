package com.mirandahawkes.scalamcp.capsule.service

import com.mirandahawkes.scalamcp.capsule.model.*
import com.mirandahawkes.scalamcp.capsule.model.filter.{
  SimpleCondition,
  Filter,
  FilterRequestWrapper
}
import com.mirandahawkes.scalamcp.common.service.HttpClient
import com.mirandahawkes.scalamcp.server.model.Pagination
import com.mirandahawkes.scalamcp.util.FileLogging
import sttp.client3.*
import sttp.client3.ziojson.*
import sttp.model.HeaderNames
import zio.json.*

import scala.reflect.ClassTag

object CapsuleHttpClient extends HttpClient:
  private val DEFAULT_CAPSULE_HOST = "https://api.capsulecrm.com"
  private val API_TOKEN: String = sys.env("CAPSULE_API_TOKEN")
  private def requestWithBearer: RequestT[Empty, Either[String, String], Any] =
    baseRequest
      .header(HeaderNames.Authorization, s"Bearer $API_TOKEN")

  override protected val baseUrl: String =
    s"${sys.env.getOrElse("CAPSULE_BASE_URL", DEFAULT_CAPSULE_HOST)}/api/v2"

  def getRequest[T: {JsonDecoder, ClassTag}](
      path: String,
      pagination: Pagination = Pagination(),
      queryParams: Map[String, Any] = Map.empty
  ): T =
    val embed = Map("embed" -> "meta")
    val uri = constructUri(baseUrl, path, pagination, queryParams ++ embed)

    logger.info(s"GET ${uri.toString}")

    val response = requestWithBearer
      .get(uri)
      .response(asStringAlways)
      .send(backend)

    handleResponseAsJson[T](response)

  def filterRequest[T: {JsonDecoder, ClassTag}](
      path: String,
      filter: Filter,
      pagination: Pagination = Pagination(),
      queryParams: Map[String, Any] = Map.empty
  ): T =
    val embed = Map("embed" -> "meta")
    val uri = constructUri(baseUrl, path, pagination, queryParams ++ embed)
    val body = FilterRequestWrapper(filter)

    logger.info(s"POST ${uri.toString}\nBody: ${body.toJson}")

    val response = requestWithBearer
      .post(uri)
      .body(body)
      .response(asStringAlways)
      .send(backend)

    handleResponseAsJson[T](response)
