#!/usr/bin/env bash

set -eo pipefail

source environment.sh

mvn clean javadoc:jar source:jar deploy
