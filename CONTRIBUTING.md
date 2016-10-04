# Contributing

Pull requests are welcome.


## Tests

There's a main class that can be used to test the integration. It's also useful to read this class to see how to integrate with the notification client.

On a command line build the project with Maven:

```shell
> mvn test
```

Then run Java via the Maven exec command with the following arguments:

```shell
> mvn exec:java -Dexec.mainClass=TestNotificationClient -Dexec.args="api-key-id service-id https://api.notifications.service.gov.uk"
```

You'll be prompted to select an option to submit the API call.
