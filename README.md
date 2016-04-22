# notifications-java-client
Java client for GOV.UK Notify

## Prerequisites
- JDK 7+

## CLI application

To consume Gov Notify API from command line, a small CLI application has been included in the distribution.
Currently, it is possible to:
- send SMS
- send email
- check the status of the request for above

```
./gov-notify-cli -h
```

## Development notes

Production code uses pure Java, however, unit tests are written in Groovy using Spock framework for better readability. The client library uses Gradle as a built tool with wrapper so installing standalone Gradle is not necessary. Also Groovy is bundled using Gradle, so to run tests, local Groovy installation is not required.

To run tests:
```
./gradlew test
```
