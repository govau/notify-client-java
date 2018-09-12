# Contributing

Pull requests are welcome.

## Unit tests

On a command line build the project with Maven to run the unit tests and integration tests:

```shell
> ./gradlew clean test
```


## Integration Tests for Notify dev team

You will need to source an environment.sh file. The contents of that file are explained below.

```
export NOTIFY_API_URL=https://api.notify.works 
export API_KEY= api key for Client integration service on preview (found in Jenkins vault)
export FUNCTIONAL_TEST_EMAIL=sample-email@email.com
export FUNCTIONAL_TEST_NUMBER=+447481358476 # Twilio number
export EMAIL_TEMPLATE_ID=f0bb62f7-5ddb-4bf8-aac7-c1e6aefd1524   # Will need a ((name)) substitutable variable in the template
export SMS_TEMPLATE_ID=31046c06-418a-49bf-86de-706b68415b47     # Will need a ((name)) substitutable variable in the template
export API_SENDING_KEY= # team key for sending a real message to the inbound number to create an inbound sms (found in Jenkins vault)
```


## Update version
Increment the version in the /src/resources/application.properties file.

