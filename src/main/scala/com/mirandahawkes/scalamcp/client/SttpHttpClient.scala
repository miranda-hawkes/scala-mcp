package com.mirandahawkes.scalamcp.client

import com.mirandahawkes.scalamcp.util.FileLogging
import sttp.client3.*
import zio.json.*
import sttp.model.HeaderNames

import scala.reflect.ClassTag

object SttpHttpClient extends FileLogging:
  private val backend = HttpClientSyncBackend()
  private val BASE_URL: String =
    sys.env.getOrElse("CAPSULE_BASE_URL", "https://api.capsulecrm.com")
  private val API_TOKEN: String = sys.env("CAPSULE_API_TOKEN")
  private val MAX_PAGE_SIZE: Int = 100

  private def capsuleRequest: RequestT[Empty, Either[String, String], Any] =
    basicRequest
      .header(HeaderNames.Authorization, s"Bearer $API_TOKEN")
      .header(HeaderNames.UserAgent, "mcp-server")
      .header(HeaderNames.ContentType, "application/json")

  def getRequest[T: {JsonDecoder, ClassTag}](
      path: String,
      queryParams: Map[String, Any] = Map.empty
  ): T =
    val firstPage = Map("perPage" -> MAX_PAGE_SIZE)
    val uriWithParams = uri"$BASE_URL/api/v2/$path?${queryParams ++ firstPage}"

    logger.info(s"GET ${uriWithParams.toString}")

    val request = capsuleRequest
      .get(uriWithParams)
      .response(asStringAlways)

    val response = request.send(backend)

    if (response.code.isSuccess) {
      response.body.fromJson[T] match {
        case Right(result) =>
          logger.info(s"Response: ${response.statusText}")
          result
        case Left(error) =>
          logger.error(
            s"Could not deserialize response to JSON: $error\nBody: ${response.body}"
          )
          throw new RuntimeException(
            s"Error reading response from Capsule: $error"
          )
      }
    } else {
      logger.error(s"API request failed: ${response.code} - ${response.body}")
      throw new RuntimeException(
        s"Capsule API error: ${response.code}\nBody: ${response.body}"
      )
    }
