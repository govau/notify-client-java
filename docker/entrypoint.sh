#!/usr/bin/env bash

set -eo pipefail; [[ "$TRACE" ]] && set -x

if [[ "$(id -u)" -ne 0 ]]; then
    echo 'docker-entrypoint requires root' >&2
    exit 1
fi

if [ -n "$HTTP_PROXY" ]; then
  MVN_NO_PROXY=`echo $NO_PROXY | sed -E "s/,/\|/g" | sed -E "s/(^|\|)\./\1\*\./g"`
  IFS=':' read -r -a http_proxy_parts <<< "$HTTP_PROXY"
  MVN_PROXY_PROTOCOL="${http_proxy_parts[0]}"
  MVN_PROXY_HOST=`echo "${http_proxy_parts[0]}:${http_proxy_parts[1]}" | sed -E "s/https?:\/\///g"`
  MVN_PROXY_PORT="${http_proxy_parts[2]}"

  MVN_OPTS="-Dhttp.proxyHost=$MVN_PROXY_HOST -Dhttp.proxyPort=$MVN_PROXY_PORT -Dhttps.proxyHost=$MVN_PROXY_HOST -Dhttps.proxyPort=$MVN_PROXY_PORT -Dhttp.nonProxyHosts=\"$MVN_NO_PROXY\""

  mkdir -p /root/.m2
  cat << EOF > /root/.m2/settings.xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <proxies>
      <proxy>
          <id>proxy-http</id>
          <active>true</active>
          <protocol>$MVN_PROXY_PROTOCOL</protocol>
          <host>$MVN_PROXY_HOST</host>
          <port>$MVN_PROXY_PORT</port>
          <nonProxyHosts>
              $MVN_NO_PROXY
          </nonProxyHosts>
      </proxy>
  </proxies>

  <profiles>
    <profile>
      <id>inject-properties</id>
      <properties>
        <http-proxy-host>$MVN_PROXY_HOST</http-proxy-host>
        <http-proxy-port>$MVN_PROXY_PORT</http-proxy-port>
        <non-proxy-hosts>$MVN_NO_PROXY</non-proxy-hosts>
      </properties>
    </profile>
  </profiles>

  <activeProfiles>
    <activeProfile>inject-properties</activeProfile>
  </activeProfiles>
</settings>
EOF
fi


exec env MVN_OPTS="$MVN_OPTS" "$@"
