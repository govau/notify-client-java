FROM maven:3-jdk-8

RUN \
    echo "Install Debian packages" \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        make \
        curl \
        git \

    && echo "Clean up" \
    && rm -rf /var/lib/apt/lists/* /tmp/*

WORKDIR /var/project