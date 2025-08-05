#!/bin/bash
echo "CAPSULE_API_TOKEN in wrapper: $CAPSULE_API_TOKEN" >&2

cd /Users/mirandahawkes/projects/scala-mcp

# Force Scala CLI to use a local build dir
export SCALA_CLI_HOME=/Users/mirandahawkes/projects/scala-mcp/.scala-cli
export SCALA_CLI_WORKSPACE=/Users/mirandahawkes/projects/scala-mcp/.scala-build

mkdir -p "$SCALA_CLI_HOME" "$SCALA_CLI_WORKSPACE"

# Forward all args to scala-cli
exec /opt/homebrew/bin/scala-cli run . "$@"