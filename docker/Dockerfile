FROM maven:3-jdk-8

ARG APT_HTTP_PROXY

RUN \
    echo "Install Debian packages" \
    && ([ -z "$APT_HTTP_PROXY" ] || echo "Acquire::http::Proxy \"${APT_HTTP_PROXY}\";\n" > /etc/apt/apt.conf.d/99HttpProxy) \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        make \
        curl \
        git \

    && echo "Clean up" \
    && rm -rf /var/lib/apt/lists/* /tmp/*

WORKDIR /var/project

COPY entrypoint.sh /usr/local/bin/docker-entrypoint

ENTRYPOINT ["/usr/local/bin/docker-entrypoint"]
