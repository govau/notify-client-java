# GOV.UK Notify - notifications-java-client

This is the Java client for the GOV.UK Notify API.

It provides client calls, response marshalling and authentication for the GOV.UK Notify API.

This application is built using Java 8.

[![Build Status](https://travis-ci.org/alphagov/notifications-java-client.svg?branch=master)](https://travis-ci.org/alphagov/notifications-java-client)

## Usage

Before usage, create a service through the Notify admin console. This provides access to the API credentials for your application. There are normal, test and team API keys available.
Read the [API documentation](https://www.notifications.service.gov.uk/documentation) for more information.

The notifications-java-client has been deployed to [bintray](https://bintray.com). You will need to add the following snippet to your maven settings.xml file.
```
<?xml version='1.0' encoding='UTF-8'?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd' xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
<profiles>
	<profile>
		<repositories>
			<repository>
				<snapshots>
					<enabled>false</enabled>
				</snapshots>
				<id>bintray-gov-uk-notify-maven</id>
				<name>bintray</name>
				<url>http://dl.bintray.com/gov-uk-notify/maven</url>
			</repository>
		</repositories>
		<pluginRepositories>
			<pluginRepository>
				<snapshots>
					<enabled>false</enabled>
				</snapshots>
				<id>bintray-gov-uk-notify-maven</id>
				<name>bintray-plugins</name>
				<url>http://dl.bintray.com/gov-uk-notify/maven</url>
			</pluginRepository>
		</pluginRepositories>
		<id>bintray</id>
	</profile>
</profiles>
<activeProfiles>
	<activeProfile>bintray</activeProfile>
</activeProfiles>
</settings>
```
Then you can add the Maven dependency to your project.
```
    <dependency>
        <groupId>uk.gov.service.notify</groupId>
        <artifactId>notifications-java-client</artifactId>
        <version>2.0.0-RELEASE</version>
    </dependency>

```
If you are using Gradle, Artifactory, or Nexus you can click 'set me up!' on https://bintray.com/gov-uk-notify/maven/notifications-java-client for instructions.

**Import the `NotificationClient`**

```
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.Notification;
import uk.gov.service.notify.NotificationList;
import uk.gov.service.notify.NotificationResponse;
```

**Create a new instance of `NotificationClient` and objects returned by the client**

`NotificationClient client = new NotificationClient(secret, serviceId, "https://api.notifications.service.gov.uk");`

**Send an email or text message**

`NotificationResponse response = client.sendEmail(templateId, emailAddress, personalisation);`

or

`NotificationResponse response client.sendSms(templateId, mobileNumber, personalisation);`

* `mobileNumber` is the mobile phone number for the notification
    * must be a UK mobile number
    * must start with +44
    * must not have a leading zero
    * must not have any whitespaces or punctuation
    * valid format is +447777111222
* `emailAddress` is the email address for the notification
* `templateId` is the template to send
    * must be a universally unique identifier (UUID) that identifies a valid template  - templates are created in the admin tools
* `personalisation` is the placeholders to send 
    * must be a HashMap<String, String> which contains the key value pairs for the placeholders. 

**Fetch notification by Id**

`Notification notification = client.getNotificationById(notificationId);`

* `notificationId` is the Id of the notification - the Id is part of the notification object returned when `sendEmail` or `sendSms` is called
 
**Fetch all notifications for your service**:

`Notification notification = client.getNotification(status, notificationType);`

* `status` is a string that represents the status of the notifications you want returned; options for `status`:
    * `null` (for all status types)
    * `delivered` 
    * `failed`
    * `sending`
* `notificationType` is the type of notification to return; options for `notificaitonType`:
    * `null` (for both types)
    * `email` 
    * `sms`


### Testing

There is a main class that can be used to test the integration. It is also useful to read this class to see how to integrate with the notification client.

On a command line build the project with Maven:

`> mvn test`

Then run Java via the Maven exec command with the following arguments:

`>  mvn exec:java -Dexec.mainClass=TestNotificationClient -Dexec.args="api-key-id service-id https://api.notifications.service.gov.uk"`

You will be prompted to select an option to submit the API call.
