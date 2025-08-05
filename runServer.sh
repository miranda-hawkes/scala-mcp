#!/bin/bash
# Force change directories so that the client does not attempt to run `scala-cli` command in the wrong or
# un-writeable directory
cd $WORKSPACE

exec scala-cli run .