#!/usr/bin/env bash

set -eo pipefail

function prop {
    grep "${1}" src/main/resources/application.properties|cut -d'=' -f2
}

version=$(prop 'project.version')


echo $version

sed -e "s/{version}/${version}/" README.md-tpl > README.md

mvn versions:set -DnewVersion=${version}
