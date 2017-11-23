# Contributing

Pull requests are welcome.

## Unit tests

On a command line build the project with Maven to run the unit tests and integration tests:

```shell
> mvn clean test
```


## Integration Tests for Notify dev team

You will need to source an environment.sh file. The contents of that file are explained below.

```
export NOTIFY_API_URL=https://api.notify.works 
export API_KEY= api key for Client integration service on preview (found in Jenkins vault)
export FUNCTIONAL_TEST_EMAIL=notify-tests-preview+client_funct_tests@digital.cabinet-office.gov.uk
export FUNCTIONAL_TEST_NUMBER=+447481358476 # Twilio number
export EMAIL_TEMPLATE_ID=f0bb62f7-5ddb-4bf8-aac7-c1e6aefd1524
export SMS_TEMPLATE_ID=31046c06-418a-49bf-86de-706b68415b47
export LETTER_TEMPLATE_ID=de1252c4-d8c3-4435-92fb-02f136778b2b
export EMAIL_REPLY_TO_ID=db8d1a9d-41ef-43cd-a04a-ed7d95214d95
export SMS_SENDER_ID=e9355456-c52f-4648-abb6-5fc192b29195
export INBOUND_SMS_QUERY_KEY= # team key for retrieving inbound sms messages (found in Jenkins vault)
export API_SENDING_KEY= # team key for sending a real message to the inbound number to create an inbound sms (found in Jenkins vault)
```


## Update version
Increment the version in the /src/resources/application.properties file.
Run `./update_version.sh` to update the version number in the pom file and README document.

## Deploying

[For internal notify use only]
Make sure your `~/.m2/settings.xml` file is up to date with the file found at `credentials/bintray/settings.xml`

Then, from the notifications-java-client directory, run

```shell
./update_version.sh
./deploy.sh
```
