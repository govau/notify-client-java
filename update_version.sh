#!/usr/bin/env bash

set -eo pipefail

function prop {
    grep "${1}" src/main/resources/application.properties|cut -d'=' -f2
}

version=$(prop 'project.version')


echo $version

sed -i '' "s|<version>.*</version>|<version>${version}</version>|g" README.md
sed -i '' "s|compile('uk.gov.service.notify:notifications-java-client:.*')|compile('uk.gov.service.notify:notifications-java-client:${version}')|g" README.md

mvn versions:set -DnewVersion=${version}
