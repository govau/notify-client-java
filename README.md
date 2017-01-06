# GOV.UK Notify Java client

## Installation

### Maven

The notifications-java-client is deployed to [Bintray](https://bintray.com/gov-uk-notify/maven/notifications-java-client). Add this snippet to your Maven `settings.xml` file.
```xml
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
Then add the Maven dependency to your project.
```xml
    <dependency>
        <groupId>uk.gov.service.notify</groupId>
        <artifactId>notifications-java-client</artifactId>
        <version>3.0.0-RELEASE</version>
    </dependency>

```

### Gradle
```
repositories {
    mavenCentral()
    maven {
        url  "http://dl.bintray.com/gov-uk-notify/maven"
    }
}

dependencies {
    compile('uk.gov.service.notify:notifications-java-client:3.0.0-RELEASE')
}
```

### Artifactory or Nexus

Click 'set me up!' on https://bintray.com/gov-uk-notify/maven/notifications-java-client for instructions.

## Getting started


```java
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.Notification;
import uk.gov.service.notify.NotificationList;
import uk.gov.service.notify.SendEmailResponse;
import uk.gov.service.notify.SendSmsResponse;

NotificationClient client = new NotificationClient(apiKey);
```

Generate an API key by signing in to
[GOV.UK Notify](https://www.notifications.service.gov.uk) and going to
the **API integration** page.

## Send messages
### Text message

```java
Map<String, String> personalisation = new HashMap<>();
personalisation.put("name", "Jo");
personalisation.put("reference_number", "13566");
SendSmsResponse response = client.sendSms(templateId, mobileNumber, personalisation, "yourReferenceString");
```

<details>
<summary>
SendSmsResponse
</summary>

If the request is successful, the SendSmsResponse is returned from the client. Attributes of the SendSmsResponse are listed below.

```java
    UUID notificationId;
    Optional<String> reference;
    UUID templateId;
    int templateVersion;
    String templateUri;
    String body;
    Optional<String> fromNumber;

```

Otherwise the client will raise a `NotificationClientException`:

<table>
<thead>
<tr>
<th>message</th>
</tr>
</thead>
<tbody>
<tr>
<td>
<pre>
Status code: 400 {
"errors":
[{
    "error": "TooManyRequestsError",
    "message": "Exceeded send limits (50) for today"
}]
}
</pre>
</td>
</tr>
<tr>
<td>
<pre>
Status code 400: {
"errors":
[{
    "error": "BadRequestError",
    "message": "Can"t send to this recipient using a team-only API key"
]}
}
</pre>
</td>
</tr>
<tr>
<td>
<pre>
Status code: 400 {
"errors":
[{
    "error": "BadRequestError",
    "message": "Can"t send to this recipient when service is in trial mode
                - see https://www.notifications.service.gov.uk/trial-mode"
}]
}
</pre>
</td>
</tr>
<tr>
</tbody>
</table>
</details>


### Email:

```java
HashMap<String, String> personalisation = new HashMap<>();
personalisation.put("name", "Jo");
personalisation.put("reference_number", "13566");
SendEmailResponse response = client.sendEmail(templateId, mobileNumber, personalisation, "yourReferenceString");
```


<details>
<summary>
SendEmailResponse
</summary>

If the request is successful, the SendEmailResponse is returned from the client. Attributes of the SendEmailResponse are listed below.

```java
	UUID notificationId;
	Optional<String> reference;
	UUID templateId;
	int templateVersion;
	String templateUri;
	String body;
	String subject;
	Optional<String> fromEmail;

```

Otherwise the client will raise a `NotificationClientException`:

<table>
<thead>
<tr>
<th>message</th>
</tr>
</thead>
<tbody>
<tr>
<td>
<pre>
Status code: 400 {
"errors":
[{
    "error": "TooManyRequestsError",
    "message": "Exceeded send limits (50) for today"
}]
}
</pre>
</td>
</tr>
<tr>
<td>
<pre>
Status code 400: {
"errors":
[{
    "error": "BadRequestError",
    "message": "Can"t send to this recipient using a team-only API key"
]}
}
</pre>
</td>
</tr>
<tr>
<td>
<pre>
Status code: 400 {
"errors":
[{
    "error": "BadRequestError",
    "message": "Can"t send to this recipient when service is in trial mode
                - see https://www.notifications.service.gov.uk/trial-mode"
}]
}
</pre>
</td>
</tr>
<tr>
</tbody>
</table>
</details>

### Arguments
#### `phoneNumber`
The mobile number the SMS notification is sent to.

#### `emailAddress`
The email address the email notification is sent to.

#### `templateId`

Find by clicking **API info** for the template you want to send.

#### `personalisation`
If a template has placeholders, you need to provide their values. `personalisation` can be an empty or null in which case no placeholders are provided for the notification.

#### `reference`
An optional unique identifier for the notification or an identifier for a batch of notifications. `reference` can be an empty string or null.

## Get the status of one message

```java
Notification notification = client.getNotificationById(notificationId);
```


<details>
<summary>
Notification
</summary>

If successful a `notification` is returned. Below is a list of attributes in a `notification`.
```java
    UUID id;
    Optional<String> reference;
    Optional<String> emailAddress;
    Optional<String> phoneNumber;
    Optional<String> line1;
    Optional<String> line2;
    Optional<String> line3;
    Optional<String> line4;
    Optional<String> line5;
    Optional<String> line6;
    Optional<String> postcode;
    String notificationType;
    String status;
    UUID templateId;
    int templateVersion;
    String templateUri;
    String body;
    Optional<String subject;
    DateTime createdAt;
    Optional<DateTime> sentAt;
    Optional<DateTime> completedAt;
```

Otherwise the client will raise a `NotificationClientException`.

<table>
<thead>
<tr>
<th>message</th>
</tr>
</thead>
<tbody>
<tr>
<td>
<pre>
Status code: 404 {
"errors":
[{
    "error": "NoResultFound",
    "message": "No result found"
}]
}
</pre>
<pre>
Status code: 400 {
"errors":
[{
    "error": "ValidationError",
    "message": "id is not a valid UUID"
}]
}
</pre>
</tbody>
</table>
</details>

## Get the status of all messages

```java
NotificationList notification = client.getNotifications(status, notificationType, reference, otherThanId);
```

<details>
<summary>
NotificationList
</summary>
If successful a `NotificationList` is returned. Below is a list of attributes in a`NotificationList`.
```java
    List<Notification> notifications;
    String currentPageLink;
    Optional<String> nextPageLink;
```

Otherwise the client will raise a NotificationClientException.

<table>
<thead>
<tr>
<th>message</th>
</tr>
</thead>
<tbody>
<tr>
<td>
<pre>
Status code: 404 {
"errors":
[{
	'error': 'ValidationError',
    'message': 'bad status is not one of [created, sending, delivered, pending, failed, technical-failure, temporary-failure, permanent-failure]'
}]
}
</pre>
<pre>
Status code: 400 {
"errors":
[{
    "error": "ValidationError",
    "message": "Apple is not one of [sms, email, letter]"
}]
}
</pre>
</tbody>
</table>
</details>

### Arguments

#### `status`

You can filter the notifications by the following options:

* `sending` - the message is queued to be sent by the provider.
* `delivered` - the message was successfully delivered.
* `failed` - this will return all failure statuses `permanent-failure`, `temporary-failure` and `technical-failure`.
* `permanent-failure` - the provider was unable to deliver message, email or phone number does not exist; remove this recipient from your list.
* `temporary-failure` - the provider was unable to deliver message, email box was full or the phone was turned off; you can try to send the message again.
* `technical-failure` - Notify had a technical failure; you can try to send the message again.

You can pass an empty string or null to ignore the filter.

#### `notificationType`

You can filter the notifications by the following options:

* `email`
* `sms`
* `letter`
You can also pass in an empty string or null to ignore the filter.

#### `reference`
This is the `reference` you gave at the time of sending the notification. The `reference` can be a unique identifier for the notification or an identifier for a batch of notifications.
You can pass an empty string or null to ignore the filter.

#### `olderThanId`
You can get the notifications older than a given `Notification.notificationId`.
You can pass an empty string or null to ignore the filter
