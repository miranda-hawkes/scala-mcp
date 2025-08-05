package com.mirandahawkes.scalamcp.server

import com.mirandahawkes.scalamcp.client.SttpHttpClient
import com.mirandahawkes.scalamcp.client.SttpHttpClient.*
import com.mirandahawkes.scalamcp.model.PartyListWrapper
import com.mirandahawkes.scalamcp.util.{FileLogger, FileLogging}
import com.tjclp.fastmcp.core.Tool
import com.tjclp.fastmcp.macros.RegistrationMacro.*
import com.tjclp.fastmcp.server.FastMcpServer
import zio.*
import zio.json.*

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

object CrmMcpServer:

  @Tool(
    name = Some("list_contacts"),
    description = Some("List contacts in CRM account")
  )
  def listParties(): String =
    getRequest[PartyListWrapper]("parties").toJson
