# Java client documentation

This documentation is for developers interested in using this Java client to integrate their government service with GOV.UK Notify.

# Set up the client

## Install the client

The notifications-java-client deploys to Bintray.

Go to the [GOV.UK Notify Java client page on Bintray](https://bintray.com/gov-uk-notify/maven/notifications-java-client):

1. Select __Set me up!__ and use the appropriate download instructions.
1. Go to the Maven build settings section of the page and copy the appropriate dependency code snippet.

Refer to the [client change log](https://github.com/alphagov/notifications-java-client/blob/master/CHANGELOG.md) for the version number and the latest updates.

## Create a new instance of the client

Add this code to your application:

```java
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.Notification;
import uk.gov.service.notify.NotificationList;
import uk.gov.service.notify.SendEmailResponse;
import uk.gov.service.notify.SendSmsResponse;

NotificationClient client = new NotificationClient(apiKey);
```

To get an API key, [sign in to GOV.UK Notify](https://www.notifications.service.gov.uk/) and go to the __API integration__ page. You can find more information in the [API keys](/#api-keys) section.

# Send a message

You can use GOV.UK Notify to send text messages, emails and letters.

## Send a text message

### Method

```java
SendSmsResponse response = client.sendSms(mobileNumber, templateId, personalisation, reference, smsSenderId);
```

### Arguments

#### mobileNumber (required)

The phone number of the recipient of the text message. This number can be UK or international.

```
String mobileNumber="+447900900123";
```

#### templateId (required)

The ID of the template. You can find this by logging into [GOV.UK Notify](https://www.notifications.service.gov.uk/) and going to the __Templates__ page.

```
String templateId="f33517ff-2a88-4f6e-b855-c550268ce08a";
```

#### personalisation (optional)

If a template has placeholder fields for personalised information such as name or reference number, you must provide their values in a map. For example:

```java
Map<String, String> personalisation = new HashMap<>();
personalisation.put("first_name", "Amala");
personalisation.put("application_date", "2018-01-01");
```
If a template does not have any placeholder fields for personalised information, you must pass in an empty map or `null`.

#### reference (optional)

A unique identifier. This reference can identify a single unique notification or a batch of multiple notifications. If you do not have a reference, you must pass in an empty string or `null`.

```
String reference='STRING';
```

#### smsSenderId (optional)

A unique identifier of the sender of the text message notification. To set this up:

1. Log into your GOV.UK Notify account.
1. Go to __Settings__.
1. Check that you are in the correct service. If you are not, select __Switch service__ in the top right corner of the screen and select the correct one.
1. Go to the __Text Messages__ section and select __Manage__ on the "Text Message sender" row.
1. You can do one of the following:
  - copy the ID of the sender you want to use and paste it into the method
  - select __Change__ to change the default sender that the service will use, and select __Save__

```
String smsSenderId='8e222534-7f05-4972-86e3-17c5d9f894e2'
```

If you do not have have an `smsSenderId`, you must pass in an empty string or `null`.

### Response

If the request to the client is successful, you will receive the following `SendSmsResponse`:


```java
UUID notificationId;
Optional<String> reference;
UUID templateId;
int templateVersion;
String templateUri;
String body;
Optional<String> fromNumber;
```

If you are using the [test API key](/#test), all your messages will come back as delivered.

All successfully delivered messages will appear on your dashboard.

### Error codes

If the request is not successful, the client will raise an `HTTPError`:

|`httpResult`|`message`|How to fix|
|:---|:---|:---|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Can't send to this recipient using a team-only API key"`<br>`]}`|Use the correct type of API key. Refer to [API keys](/#api-keys) for more information|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Can't send to this recipient when service is in trial mode - see https://www.notifications.service.gov.uk/trial-mode"`<br>`}]`|Refer to [trial mode](https://www.notifications.service.gov.uk/features/using-notify#trial-mode) for more information|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|
|`429`|`[{`<br>`"error": "RateLimitError",`<br>`"message": "Exceeded rate limit for key type TEAM/TEST/LIVE of 3000 requests per 60 seconds"`<br>`}]`|Refer to [API rate limits](/#api-rate-limits) for more information|
|`429`|`[{`<br>`"error": "TooManyRequestsError",`<br>`"message": "Exceeded send limits (LIMIT NUMBER) for today"`<br>`}]`|Refer to [service limits](/#service-limits) for the limit number|
|`500`|`[{`<br>`"error": "Exception",`<br>`"message": "Internal server error"`<br>`}]`|Notify was unable to process the request, resend your notification.|

## Send an email

### Method

```java
SendEmailResponse response = client.sendEmail(emailAddress, templateId, personalisation, reference, emailReplyToId);
```

### Arguments

#### emailAddress (required)

The email address of the recipient.

```
String emailAddress='sender@something.com';
```

#### templateId (required)

The ID of the template. You can find this by logging into GOV.UK Notify and going to the __Templates__ page.

```
String templateId="f33517ff-2a88-4f6e-b855-c550268ce08a";
```

#### personalisation (optional)

If a template has placeholder fields for personalised information such as name or application date, you need to provide their values in a map. For example:

```java
Map<String, String> personalisation = new HashMap<>();
personalisation.put("first_name", "Amala");
personalisation.put("application_date", "2018-01-01");
```
If a template does not have any placeholder fields for personalised information, you must pass in an empty map or `null`.

#### reference (optional)

A unique identifier. This reference can identify a single unique notification or a batch of multiple notifications. If you do not have a reference, you must pass in an empty string or `null`.

```
String reference='STRING';
```

#### emailReplyToId (optional)

This is an email reply-to address specified by you to receive replies from your users. Your service cannot go live until at least one email address has been set up for this. To set up:

1. Log into your GOV.UK Notify account.
1. Go to __Settings__.
1. Check that you are in the correct service. If you are not, select __Switch service__ in the top right corner of the screen and select the correct one.
1. Go to the Email section and select __Manage__ on the "Email reply to addresses" row.
1. Select __Change__ to specify the email address to receive replies, and select __Save__.

```
String emailReplyToId='8e222534-7f05-4972-86e3-17c5d9f894e2'
```

If you do not have have an `emailReplyToId`, you must pass in an empty string or `null`.

### Response

If the request to the client is successful, you will receive the following `SendEmailResponse`:

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

### Error codes

If the request is not successful, the client will raise an `HTTPError`:

|`httpResult`|`message`|How to fix|
|:---|:---|:---|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Can't send to this recipient using a team-only API key"`<br>`]}`|Use the correct type of API key. Refer to [API keys](/#api-keys) for more information|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Can't send to this recipient when service is in trial mode - see https://www.notifications.service.gov.uk/trial-mode"`<br>`}]`|Refer to [trial mode](https://www.notifications.service.gov.uk/features/using-notify#trial-mode) for more information|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|
|`429`|`[{`<br>`"error": "RateLimitError",`<br>`"message": "Exceeded rate limit for key type TEAM/TEST/LIVE of 3000 requests per 60 seconds"`<br>`}]`|Refer to [API rate limits](/#api-rate-limits) for more information|
|`429`|`[{`<br>`"error": "TooManyRequestsError",`<br>`"message": "Exceeded send limits (LIMIT NUMBER) for today"`<br>`}]`|Refer to [service limits](/#service-limits) for the limit number|
|`500`|`[{`<br>`"error": "Exception",`<br>`"message": "Internal server error"`<br>`}]`|Notify was unable to process the request, resend your notification.|

## Send a letter

When your service first signs up to GOV.UK Notify, you’ll start in trial mode. You can only send letters in live mode.

### Method

```java
SendLetterResponse response = client.sendLetter(templateId, personalisation, reference);
```

### Arguments

#### templateId (required)

The ID of the template. You can find this by logging into GOV.UK Notify and going to the __Templates__ page.

```
String templateId="f33517ff-2a88-4f6e-b855-c550268ce08a";
```

#### personalisation (required)

The personalisation argument always contains the following parameters for the letter recipient's address:

- `address_line_1`
- `address_line_2`
- `postcode`

Any other placeholder fields included in the letter template also count as required parameters. You need to provide their values in a map. For example:

```java
HashMap<String, String> personalisation = new HashMap<>();
personalisation.put("address_line_1", "The Occupier"); // mandatory address field
personalisation.put("address_line_2", "Flat 2"); // mandatory address field
personalisation.put("postcode", "SW14 6BH"); // mandatory address field
personalisation.put("first_name", "Amala"); // field from template
personalisation.put("application_date", "2018-01-01"); // field from template
```

If a template does not have any placeholder fields for personalised information, you must pass in an empty map or `null`.

#### personalisation (optional)

The following parameters in the letter recipient's address are optional:

```java
personalisation.put("address_line_3", "123 High Street"); // optional address field
personalisation.put("address_line_4", "Richmond upon Thames"); // optional address field
personalisation.put("address_line_5", "London"); // optional address field
personalisation.put("address_line_6", "Middlesex"); // optional address field
```

#### reference (optional)

A unique identifier. This reference can identify a single unique notification or a batch of multiple notifications. If you do not have a reference, you must pass in an empty string or `null`.

```
String reference='STRING';
```

### Response

If the request to the client is successful, you will receive a `SendLetterResponse`:

```java
UUID notificationId;
Optional<String> reference;
UUID templateId;
int templateVersion;
String templateUri;
String body;
String subject;
```

### Error codes

If the request is not successful, the client will raise an `HTTPError`:

|`httpResult`|`message`|How to fix|
|:---|:---|:---|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Cannot send letters with a team api key"`<br>`]}`|Use the correct type of API key. Refer to [API keys](/#api-keys) for more information|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Cannot send letters when service is in trial mode - see https://www.notifications.service.gov.uk/trial-mode"`<br>`}]`|Refer to [trial mode](https://www.notifications.service.gov.uk/features/using-notify#trial-mode) for more information|
|`400`|`[{`<br>`"error": "ValidationError",`<br>`"message": "personalisation address_line_1 is a required property"`<br>`}]`|Ensure that your template has a field for the first line of the address, check [personlisation](/#send-a-letter-required-arguments-personalisation) for more information.|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|
|`429`|`[{`<br>`"error": "RateLimitError",`<br>`"message": "Exceeded rate limit for key type TEAM/TEST/LIVE of 3000 requests per 60 seconds"`<br>`}]`|Refer to [API rate limits](/#api-rate-limits) for more information|
|`429`|`[{`<br>`"error": "TooManyRequestsError",`<br>`"message": "Exceeded send limits (LIMIT NUMBER) for today"`<br>`}]`|Refer to [service limits](/#service-limits) for the limit number|
|`500`|`[{`<br>`"error": "Exception",`<br>`"message": "Internal server error"`<br>`}]`|Notify was unable to process the request, resend your notification.|


## Send a precompiled letter

This is an invitation only feature. Contact the GOV.UK Notify team on the [support page](https://www.notifications.service.gov.uk/support) or through the [slack channel](https://govuk.slack.com/messages/C0AC2LX7E) for more information.

### Method

```java
LetterResponse response = client.sendPrecompiledLetter(reference, precompiledPDF);
```

### Arguments

#### reference (required)

You create this unique identifier. This reference can identify a single unique notification or a batch of multiple notifications. It must not contain any personal information.

```
String reference="STRING";
```

#### precompiledPDF (required)

The precompiled letter must be a PDF file. This argument adds the precompiled letter PDF file to a Java file object. The method sends this Java file object to GOV.UK Notify.

```java
File precompiledPDF = new File("<PDF file path>");
```

### Response

If the request to the client is successful, you will receive a `LetterResponse` from the client:

```java
UUID notificationId;
<String> reference;
```

### Error codes

If the request is not successful, the client will raise a `NotificationClientException`.

|`httpResult`|`message`|How to fix|
|:---|:---|:---|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Cannot send letters with a team api key"`<br>`]}`|Use the correct type of API key. Refer to [API keys](/#api-keys) for more information|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Cannot send letters when service is in trial mode - see https://www.notifications.service.gov.uk/trial-mode"`<br>`}]`|Refer to [trial mode](https://www.notifications.service.gov.uk/features/using-notify#trial-mode) for more information|
|`400`|`[{`<br>`"error": "ValidationError",`<br>`"message": "personalisation address_line_1 is a required property"`<br>`}]`|Send a valid PDF file|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|
|`429`|`[{`<br>`"error": "RateLimitError",`<br>`"message": "Exceeded rate limit for key type TEAM/TEST/LIVE of 3000 requests per 60 seconds"`<br>`}]`|Refer to [API rate limits](/#api-rate-limits) for more information|
|`429`|`[{`<br>`"error": "TooManyRequestsError",`<br>`"message": "Exceeded send limits (LIMIT NUMBER) for today"`<br>`}]`|Refer to [service limits](/#service-limits) for the limit number|
|N/A|`"message":"precompiledPDF must be a valid PDF file"`|Send a valid PDF file|
|N/A|`"message":"reference cannot be null or empty"`|Populate the reference parameter|
|N/A|`"message":"precompiledPDF cannot be null or empty"`|Send a PDF file with data in it|

# Get message status

The possible status of a message depends on the message type.

## Status - text and email

### Created

The message is queued to be sent to the provider. The notification usually remains in this state for a few seconds.

### Sending

The message is queued to be sent by the provider to the recipient, and we are waiting for delivery information.

### Delivered

The message was successfully delivered.

### Sent (text message only)

The text message was delivered internationally. We may not receive additional status updates depending on the recipient's country and telecoms provider.

### Failed

This covers all failure statuses:

- `permanent-failure` - "The provider was unable to deliver message, email or phone number does not exist; remove this recipient from your list"
- `temporary-failure` - "The provider was unable to deliver message, email inbox was full or phone was turned off; you can try to send the message again"
- `technical-failure` - "Notify had a technical failure; you can try to send the message again"

## Status - letter

### Failed

The only failure status that applies to letters is `technical-failure` - Notify had an unexpected error while sending to our printing provider.

### Accepted

Notify is printing and posting the letter.

## Get the status of one message

### Method

```java
Notification notification = client.getNotificationById(notificationId);
```

### Arguments

#### notificationId (required)

The ID of the notification.

### Response

If the request to the client is successful, you will receive the following `notification` response:

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
Optional<DateTime> estimatedDelivery;
```

### Error codes

If the request is not successful, the client will raise a `NotificationClientException`:

|`httpResult`|`message`|How to fix|
|:---|:---|:---|
|`400`|`[{`<br>`"error": "ValidationError",`<br>`"message": "id is not a valid UUID"`<br>`}]`|Check the notification ID|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|
|`404`|`[{`<br>`"error": "NoResultFound",`<br>`"message": "No result found"`<br>`}]`|Check the notification ID|


## Get the status of multiple messages

This API call will return one page of up to 250 messages and statuses. You can get either the most recent messages, or get older messages by specifying a particular notification ID in the [`olderThanId`](/#olderthanid) argument.

### Method

```java
NotificationList notification = client.getNotifications(status, notificationType, reference, olderThanId);
```

To get the most recent messages, you must pass in an empty `olderThanId` argument or `null`.

To get older messages, pass the ID of an older notification into the `olderThanId` argument. This will return the next oldest messages from the specified notification ID.

### Arguments

You can pass in empty arguments or `null` to ignore these filters.

#### status (optional)

| status | description | text | email | letter |
|:--- |:--- |:--- |:--- |:--- |
|`created` |The message is queued to be sent to the provider|Yes|Yes||
|`sending` |The message is queued to be sent by the provider to the recipient|Yes|Yes||
|`delivered`|The message was successfully delivered|Yes|Yes||
|`sent`|The text message was delivered internationally|Yes|Yes||
|`failed`|This will return all failure statuses:<br>- `permanent-failure`<br>- `temporary-failure`<br>- `technical-failure`|Yes|Yes||
|`permanent-failure`|The provider was unable to deliver message, email or phone number does not exist; remove this recipient from your list|Yes|Yes||
|`temporary-failure`|The provider was unable to deliver message, email inbox was full or phone was turned off; you can try to send the message again|Yes|Yes||
|`technical-failure`|Email / Text: Notify had a technical failure; you can try to send the message again. <br><br>Letter: Notify had an unexpected error while sending to our printing provider. <br><br>You can omit this argument to ignore this filter.|Yes|Yes||
|`accepted`|Notify is printing and posting the letter|||Yes|

#### notificationType (optional)

You can filter by:

* `email`
* `sms`
* `letter`

#### reference (optional)

A unique identifier. This reference can identify a single unique notification or a batch of multiple notifications.

```
String reference='STRING';
```

#### olderThanId (optional)

Input the ID of a notification into this argument. If you use this argument, the next 250 received notifications older than the given ID are returned.

```
String olderThanId='8e222534-7f05-4972-86e3-17c5d9f894e2'
```

If you pass in an empty argument or `null`, the most recent 250 notifications are returned.

### Response

If the request to the client is successful, you will receive a `NotificationList` response.

```java
List<Notification> notifications;
String currentPageLink;
Optional<String> nextPageLink;
```

### Error codes

If the request is not successful, the client will raise a `NotificationClientException`:

|`httpResult`|`message`||
|:---|:---|:---|
|`400`|`[{`<br>`"error": "ValidationError",`<br>`"message": "bad status is not one of [created, sending, sent, delivered, pending, failed, technical-failure, temporary-failure, permanent-failure, accepted, received]"`<br>`}]`|
|`400`|`[{`<br>`"error": "ValidationError",`<br>`"message": "Apple is not one of [sms, email, letter]"`<br>`}]`|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|


# Get a template

## Get a template by ID

### Method

This will return the latest version of the template.

```java
Template template = client.getTemplateById(templateId);
```

### Arguments

#### templateId (required)

The ID of the template. You can find this by logging into GOV.UK Notify and going to the __Templates__ page.

```
String templateId='f33517ff-2a88-4f6e-b855-c550268ce08a';
```

### Response

If the request to the client is successful, you will receive a `Template` response.

```java
UUID id;
String templateType;
DateTime createdAt;
Optional<DateTime> updatedAt;
String createdBy;
int version;
String body;
Optional<String> subject;
Optional<Map<String, Object>> personalisation;
```

### Error codes

If the request is not successful, the client will raise a `NotificationClientException`:

|`httpResult`|`message`|How to fix|
|:---|:---|:---|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|
|`404`|`[{`<br>`"error": "NoResultFound",`<br>`"message": "No Result Found"`<br>`}]`|Check your [template ID](/#arguments-template-id-required)|


## Get a template by ID and version

### Method

This will return the latest version of the template.

```java
Template template = client.getTemplateVersion(templateId, version);
```

### Arguments

#### templateId (required)

The ID of the template. You can find this by logging into GOV.UK Notify and going to the __Templates__ page.

```
String templateId='f33517ff-2a88-4f6e-b855-c550268ce08a';
```

#### version (required)

The version number of the template.

### Response

If the request to the client is successful, you will receive a `Template` response.

```Java
UUID id;
String templateType;
DateTime createdAt;
Optional<DateTime> updatedAt;
String createdBy;
int version;
String body;
Optional<String> subject;
Optional<Map<String, Object>> personalisation;
```

### Error codes

If the request is not successful, the client will raise an `NotificationClientException`:

|`httpResult`|`message`|How to fix|
|:---|:---|:---|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|
|`404`|`[{`<br>`"error": "NoResultFound",`<br>`"message": "No Result Found"`<br>`}]`|Check your [template ID](/#get-a-template-by-id-and-version-arguments-template-id-required) and [version](/#version)|


## Get all templates

### Method

This will return the latest version of all templates.

```java
TemplateList templates = client.getAllTemplates(templateType);
```

### Arguments

#### templateType (optional)

If omitted all templates are returned. Otherwise you can filter by:

- `email`
- `sms`
- `letter`

### Response

If the request to the client is successful, you will receive a `TemplateList` response.

```java
List<Template> templates;
```

If no templates exist for a template type or there no templates for a service, the templates list will be empty.

## Generate a preview template

### Method

This will generate a preview version of a template.

```java
TemplatePreview templatePreview = client.getTemplatePreview(templateId, personalisation);
```

The parameters in the personalisation argument must match the placeholder fields in the actual template. The API notification client will ignore any extra fields in the method.

### Arguments

#### templateId (required)

The ID of the template. You can find this by logging into GOV.UK Notify and going to the __Templates__ page.

```
String templateId='f33517ff-2a88-4f6e-b855-c550268ce08a';
```

#### personalisation (required)

If a template has placeholder fields for personalised information such as name or application date, you need to provide their values in a map. For example:

```java
Map<String, String> personalisation = new HashMap<>();
personalisation.put("first_name", "Amala");
personalisation.put("application_date", "2018-01-01");
```
If a template does not have any placeholder fields for personalised information, you must pass in an empty map or `null`.

### Response

If the request to the client is successful, you will receive a `TemplatePreview` response.

```java
UUID id;
String templateType;
int version;
String body;
Optional<String> subject;
```

### Error codes

If the request is not successful, the client will raise an `NotificationClientException`:

|`httpResult`|`message`|How to fix|
|:---|:---|:---|
|`400`|`[{`<br>`"error": "BadRequestError",`<br>`"message": "Missing personalisation: [PERSONALISATION FIELD]"`<br>`}]`|Check that the personalisation arguments in the method match the placeholder fields in the template|
|`400`|`[{`<br>`"error": "NoResultFound",`<br>`"message": "No result found"`<br>`}]`|Check the [template ID](/#generate-a-preview-template-required-arguments-template-id)|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Error: Your system clock must be accurate to within 30 seconds"`<br>`}]`|Check your system clock|
|`403`|`[{`<br>`"error": "AuthError",`<br>`"message": "Invalid token: signature, api token not found"`<br>`}]`|Use the correct API key. Refer to [API keys](/#api-keys) for more information|


# Get received text messages

This API call will return one page of up to 250 received text messages. You can get either the most recent messages, or get older messages by specifying a particular notification ID in the [`older_than`](/#older-than) argument.

### Method

```java
ReceivedTextMessageList response = client.getReceivedTextMessages(olderThanId);
```

To get the most recent messages, you must pass in an empty argument or `null`.

To get older messages, pass the ID of an older notification into the `olderThanId` argument. This will return the next oldest messages from the specified notification ID.

### Arguments

#### olderThanId (optional)

Input the ID of a received text message into this argument. If you use this argument, the next 250 received text messages older than the given ID are returned.

```
String emailReplyToId='8e222534-7f05-4972-86e3-17c5d9f894e2'
```

If you pass in an empty argument or `null`, the most recent 250 text messages are returned.

### Response

If the request to the client is successful, you will receive a `ReceivedTextMessageList` response that returns all received texts.

```java
private List<ReceivedTextMessage> receivedTextMessages;
private String currentPageLink;
private String nextPageLink;
```
The `ReceivedTextMessageList` will have the following properties:

```java
private UUID id;
private String notifyNumber;
private String userNumber;
private UUID serviceId;
private String content;
private DateTime createdAt;
```
