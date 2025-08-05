package com.mirandahawkes.scalamcp.server

import com.mirandahawkes.scalamcp.client.SttpHttpClient
import com.mirandahawkes.scalamcp.client.SttpHttpClient.*
import com.mirandahawkes.scalamcp.util.FileLogger
import com.mirandahawkes.scalamcp.util.FileLogging
import com.mirandahawkes.scalamcp.model.{Party, PartyListWrapper}
import com.tjclp.fastmcp.core.{Prompt, PromptParam, Resource, Tool, ToolParam}
import com.tjclp.fastmcp.server.FastMcpServer
import com.tjclp.fastmcp.macros.RegistrationMacro.*
import zio.*

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
        // This macro finds all methods with @Tool annotations in CrmMcpServer
        // and registers them with the server
        server.scanAnnotations[CrmMcpServer.type]
      }
      // Run the server
      _ <- server.runStdio()
    yield ()

object CrmMcpServer:

  @Tool(name = Some("list_contacts"))
  def listParties(): PartyListWrapper =
    getRequest[PartyListWrapper]("parties", Map("perPage" -> 2))
