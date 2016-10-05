#!/usr/bin/env bash

set -eo pipefail


read -p "What is the next release number: " version

echo $version

sed -e "s/{version}/${version}/" README.md-tpl > README.md

mvn versions:set -DnewVersion=${version}

source environment.sh

mvn deploy