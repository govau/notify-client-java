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
> mvn exec:java -Dexec.mainClass=TestNotificationClient -Dexec.args="api-key-id https://api.notifications.service.gov.uk"
```

You'll be prompted to select an option to submit the API call.


## Readme updates
Don't update README.md - when you run `./update_version.sh` you'll lose any changes you made there - instead make
changes in README-tpl.md before you call `./update_version.sh`


## Deploying

[For internal notify use only]
Make sure your `~/.m2/settings.xml` file is up to date with the file found at `credentials/bintray/settings.xml`

Then, from the notifications-java-client directory, run

```shell
./update_version.sh
./deploy.sh
```
