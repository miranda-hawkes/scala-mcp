# Scala MCP Server
Work in progress! 👷‍♀️🔨

Scala-based MCP server implementation to connect to your Capsule CRM data

## Quick Start
### 1. Get Your Capsule API Token
1. Log into your Capsule CRM account
2. Go to **My Preferences → API Authentication**
3. Create a new API token and copy it

### 2. Install & Configure

#### macOS Setup

```bash
# Install jenv & java 17

# Install scala-cli
```

#### Linux/Windows Setup
Coming soon!

### 3. Connect to Your AI Assistant

#### Claude Desktop

Add this to your Claude Desktop config file:

**Config Location:**
- **macOS:** `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows:** `%APPDATA%/Claude/claude_desktop_config.json`

```json
{
  "mcpServers": {
    "scala-mcp": {
      "command": "/path/to/your/scala-mcp/run.sh",
      "args": [],
      "transport": "stdio",
      "env": {
        "CAPSULE_API_TOKEN": "your-api-token"
      }
    }
  }
}
```

You can also optionally override the `CAPSULE_BASE_URL` environment variable to point to a different Capsule CRM instance.

#### Example
```json
{
  "mcpServers": {
    "scala-mcp": {
      "command": "/Users/mirandahawkes/projects/scala-mcp/run.sh",
      "args": [],
      "transport": "stdio",
      "env": {
        "CAPSULE_API_TOKEN": "abc123",
        "CAPSULE_BASE_URL": "https://api.capsule.run"
      }
    }
  }
}
```