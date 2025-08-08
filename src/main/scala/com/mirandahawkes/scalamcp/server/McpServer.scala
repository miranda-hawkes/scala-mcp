package com.mirandahawkes.scalamcp.server

import com.mirandahawkes.scalamcp.capsule.service.CapsuleHttpClient
import com.mirandahawkes.scalamcp.capsule.model.{
  Party,
  PartyResponse,
  PartyType
}
import CapsuleHttpClient.*
import com.mirandahawkes.scalamcp.capsule.model.filter.Filter
import com.mirandahawkes.scalamcp.capsule.model.filter.*
import com.mirandahawkes.scalamcp.capsule.model.filter.SimpleCondition.*
import com.mirandahawkes.scalamcp.server.model.Pagination
import com.mirandahawkes.scalamcp.util.{FileLogger, FileLogging}
import com.tjclp.fastmcp.core.{Tool, ToolParam}
import com.tjclp.fastmcp.macros.RegistrationMacro.*
import com.tjclp.fastmcp.server.FastMcpServer
import zio.*
import zio.json.*
import sttp.tapir.generic.auto.*

object McpServer extends ZIOAppDefault with FileLogging:
  override def run =
    for
      _ <- ZIO.attempt {
        FileLogger.init()
        logger.info("MCP Server starting...")
      }
      // Create server instance
      server <- ZIO.succeed(FastMcpServer("McpServer"))
      // Process tools using the scanAnnotations macro extension method
      _ <- ZIO.attempt {
        // This macro finds all methods with @Tool, @Prompt or @Resource annotations
        // and registers them with the server
        server.scanAnnotations[CrmMcpServer.type]
      }
      // Run the server
      _ <- server.runStdio()
    yield ()

object CrmMcpServer extends FileLogging:

  // [{"field": "name","value": "Miranda","operator": "contains"}]
  // {"conditions": [{"type": "string", "field": "name","value": "Miranda","operator": "contains"}]}

  @Tool(
    name = Some("search_contacts"),
    description = Some(
      "Perform a search of contacts. Refer to `describe_search_contacts` for tool description and usage"
    )
  )
  def searchParties(
      @ToolParam(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @ToolParam("array of zero or more conditions") filter: Filter
  ): String = {
    filterRequest[PartyResponse](
      "parties/filters/results",
      filter,
      pagination
    ).toJson
  }

  @Tool(
    name = Some("describe_search_contacts"),
    description = Some(
      "Returns a detailed description of how to use the `search_contacts` tool."
    )
  )
  def describeSearchContacts(): String =
    """
      |The `search_contacts` tool allows you to filter contacts using a list of structured conditions. You can include as
      |many conditions as needed.
      |
      |Each `condition` must specify:
      |  - `field`: the contact field to filter on (e.g. `name`, `id`, `addedOn`)
      |  - `operator`: the type of comparison (e.g. `is`, `is not`, `contains`, `is after`)
      |  - `value`: the value to match against
      |  - `type`: the type of the field value
      |
      |You can also optionally apply pagination:
      |  - `page`: the page number to return (default: 1)
      |  - `perPage`: the number of results per page (default: 100)
      |
      |Example:
      |{
      |  "conditions": [
      |    {
      |      "fieldName": "name",
      |      "operator": "contains",
      |      "fieldValue": "John"
      |    }
      |  ],
      |  "pagination": {
      |    "page": 1,
      |    "perPage": 100
      |  }
      |}
      |
      | Operator Reference:
      | Operator	      Supported Field Types	         Type of Value
      | `is`	          Boolean, date, number, string	 As field type
      | `is not`	      Boolean, date, number, string	 As field type
      | `starts with`	  String	                     String
      | `ends with`	      String	                     String
      | `contains`	      String	                     String
      | `is greater than` Number	                     Number
      | `is less than`	  Number	                     Number
      | `is after`	      Date	                         Date
      | `is before`       Date	                         Date
      | `is older than`	  Date	                         Number (of days)
      | `is within last`  Date	                         Number (of days)
      | `is within next`  Date	                         Number (of days)
      |
      | Contact Field Reference:
      | Field	                     Type
      | `id`	                     Number
      | `name`                       String
      | `jobTitle`	                 String
      | `email`	                     String
      | `phone`	                     String
      | `city` 	                     String
      | `zip`	                     String
      | `state`	                     String
      | `country`	                 String
      | `type`	                     String
      | `tag`	                     Number, string
      | `owner`	                     Number
      | `team`	                     Number
      | `hasEmailAddress`	         Boolean
      | `hasPhoneNumber`	         Boolean
      | `hasAddress`	             Boolean
      | `hasPeople`	                 Boolean
      | `hasTags`	                 Boolean
      | `addedOn`	                 Date
      | `updatedOn`	                 Date
      | `lastContactedOn`	         Date
      | `custom:{customFieldId}`	 Boolean, date, number, string
      | `org.custom:{customFieldId}` Boolean, date, number, string
      | `org.name`	                 String
      | `org.tag`	                 Number, string
      |""".stripMargin

  @Tool(
    name = Some("list_contact_custom_fields"),
    description = Some(
      "List custom fields in CRM account (useful for looking up a custom field's ID to use in a `search_contacts` request)"
    )
  )
  def listPartyCustomFields(
      @ToolParam(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[PartyResponse]("parties/fields/definitions", pagination).toJson

  /*
   * TODO: Work in progress around allowing client to specify field selection
   * */
  @Tool(
    name = Some("describe_contacts"),
    description = Some(
      "Returns the available fields that can be requested for contacts in CRM account"
    )
  )
  def describeParties(): String =
    val availableFields = EmbeddableFields(
      List(
        EmbeddableField(
          "id",
          includedByDefault = true
        ),
        EmbeddableField(
          "type",
          description = Some("Contact type: either 'person' or 'organisation'"),
          includedByDefault = true
        ),
        EmbeddableField("about"),
        EmbeddableField("createdAt"),
        EmbeddableField("updatedAt"),
        EmbeddableField("lastContactedAt"),
        EmbeddableField("addresses"),
        EmbeddableField("phoneNumbers"),
        EmbeddableField("websites"),
        EmbeddableField("emailAddresses"),
        EmbeddableField("pictureURL"),
        EmbeddableField("tags"),
        EmbeddableField(
          "fields",
          description =
            Some("An array of custom fields that are defined for this contact")
        ),
        EmbeddableField(
          "owner",
          description = Some("The user this contact is assigned to")
        ),
        EmbeddableField(
          "team",
          description = Some("The team this contact is assigned to.")
        ),
        EmbeddableField(
          "missingImportantFields",
          description = Some(
            "Indicates if this contact has any important custom fields that are missing a value"
          )
        ),
        EmbeddableField(
          "firstName",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "lastName",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "title",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "jobTitle",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "organisation",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "name",
          appliesTo = Seq(PartyType.organisation.toString)
        )
      )
    )
    availableFields.toJson

case class EmbeddableField(
    fieldName: String,
    description: Option[String] = None,
    includedByDefault: Boolean = false,
    appliesTo: Seq[String] =
      Seq(PartyType.person.toString, PartyType.organisation.toString)
)

object EmbeddableField:
  implicit val decoder: JsonDecoder[EmbeddableField] =
    DeriveJsonDecoder.gen[EmbeddableField]
  implicit val encoder: JsonEncoder[EmbeddableField] =
    DeriveJsonEncoder.gen[EmbeddableField]

case class EmbeddableFields(fields: List[EmbeddableField])

object EmbeddableFields:
  implicit val decoder: JsonDecoder[EmbeddableFields] =
    DeriveJsonDecoder.gen[EmbeddableFields]
  implicit val encoder: JsonEncoder[EmbeddableFields] =
    DeriveJsonEncoder.gen[EmbeddableFields]
