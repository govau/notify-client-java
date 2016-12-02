//package uk.gov.service.notify;
//
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//
//public class NotificationListTest {
//    @Test
//    public void testNotificationList_canCreateObjectFromJson(){
//        String content = "{\n" +
//                "  \"links\": {\"next\":\"/notifications?page=2\",\"last\":\"/notifications?page=3\"},\n" +
//                "  \"notifications\": [\n" +
//                "    {\n" +
//                "      \"api_key\": \"someApiKey\",\n" +
//                "      \"body\": \"Hello hello\",\n" +
//                "      \"content_char_count\": null,\n" +
//                "      \"created_at\": \"2016-07-15T15:16:21.675607+00:00\",\n" +
//                "      \"id\": \"0585c4bc-d9d1-4c77-a7f8-7cb14ac28067\",\n" +
//                "      \"job\": null,\n" +
//                "      \"job_row_number\": null,\n" +
//                "      \"notification_type\": \"email\",\n" +
//                "      \"reference\": \"01020155ef2144d1-43dc8ac4-3eea-412a-994c-0dd291a42340-000000\",\n" +
//                "      \"sent_at\": \"2016-07-15T15:16:23.057513+00:00\",\n" +
//                "      \"sent_by\": \"ses\",\n" +
//                "      \"service\": \"1339fcc3-7a4e-4661-aa0e-188de366bd92\",\n" +
//                "      \"status\": \"delivered\",\n" +
//                "      \"subject\": \"Version 2\",\n" +
//                "      \"template\": {\n" +
//                "        \"id\": \"ebc0f956-4672-4554-a89b-547c906cc3bd\",\n" +
//                "        \"name\": \"Testing versions\",\n" +
//                "        \"template_type\": \"email\"\n" +
//                "      },\n" +
//                "      \"template_version\": 2,\n" +
//                "      \"to\": \"someon@digital.cabinet-office.gov.uk\",\n" +
//                "      \"updated_at\": \"2016-07-15T15:16:23.729177+00:00\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"api_key\": \"someApiKey\",\n" +
//                "      \"body\": \"Hello hello\",\n" +
//                "      \"content_char_count\": null,\n" +
//                "      \"created_at\": \"2016-07-15T15:05:55.713976+00:00\",\n" +
//                "      \"id\": \"98f3f73d-5c32-4fc8-9d5b-dbdef99f8b50\",\n" +
//                "      \"job\": null,\n" +
//                "      \"job_row_number\": null,\n" +
//                "      \"notification_type\": \"email\",\n" +
//                "      \"reference\": \"01020155ef17b686-f075005d-235e-4a5a-9f5d-8d31d7b0aa31-000000\",\n" +
//                "      \"sent_at\": \"2016-07-15T15:05:56.802400+00:00\",\n" +
//                "      \"sent_by\": \"ses\",\n" +
//                "      \"service\": \"1339fcc3-7a4e-4661-aa0e-188de366bd92\",\n" +
//                "      \"status\": \"delivered\",\n" +
//                "      \"subject\": \"Version 2\",\n" +
//                "      \"template\": {\n" +
//                "        \"id\": \"ebc0f956-4672-4554-a89b-547c906cc3bd\",\n" +
//                "        \"name\": \"Testing versions\",\n" +
//                "        \"template_type\": \"email\"\n" +
//                "      },\n" +
//                "      \"template_version\": 2,\n" +
//                "      \"to\": \"someon@digital.cabinet-office.gov.uk\",\n" +
//                "      \"updated_at\": \"2016-07-15T15:05:58.194462+00:00\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"api_key\": \"someApiKey\",\n" +
//                "      \"body\": \"Hello there staging\",\n" +
//                "      \"content_char_count\": 32,\n" +
//                "      \"created_at\": \"2016-07-15T15:02:57.040124+00:00\",\n" +
//                "      \"id\": \"050082af-8998-493d-b84e-f74098ca719c\",\n" +
//                "      \"job\": null,\n" +
//                "      \"job_row_number\": null,\n" +
//                "      \"notification_type\": \"sms\",\n" +
//                "      \"reference\": null,\n" +
//                "      \"sent_at\": \"2016-07-15T15:02:59.143778+00:00\",\n" +
//                "      \"sent_by\": \"mmg\",\n" +
//                "      \"service\": \"1339fcc3-7a4e-4661-aa0e-188de366bd92\",\n" +
//                "      \"status\": \"delivered\",\n" +
//                "      \"template\": {\n" +
//                "        \"id\": \"43284901-6c7f-4224-9c18-ff73610a9df1\",\n" +
//                "        \"name\": \"First template\",\n" +
//                "        \"template_type\": \"sms\"\n" +
//                "      },\n" +
//                "      \"template_version\": 1,\n" +
//                "      \"to\": \"+447515349060\",\n" +
//                "      \"updated_at\": \"2016-07-15T15:03:01.662838+00:00\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"api_key\": \"someApiKey\",\n" +
//                "      \"body\": \"Hello hello\",\n" +
//                "      \"content_char_count\": null,\n" +
//                "      \"created_at\": \"2016-07-15T14:38:19.794906+00:00\",\n" +
//                "      \"id\": \"96704203-ae0f-4b61-bb22-d89faf973583\",\n" +
//                "      \"job\": null,\n" +
//                "      \"job_row_number\": null,\n" +
//                "      \"notification_type\": \"email\",\n" +
//                "      \"reference\": \"01020155eefe7053-925b0438-8b9f-43b8-983e-38db18c3ea29-000000\",\n" +
//                "      \"sent_at\": \"2016-07-15T14:38:20.425885+00:00\",\n" +
//                "      \"sent_by\": \"ses\",\n" +
//                "      \"service\": \"1339fcc3-7a4e-4661-aa0e-188de366bd92\",\n" +
//                "      \"status\": \"delivered\",\n" +
//                "      \"subject\": \"Version 2\",\n" +
//                "      \"template\": {\n" +
//                "        \"id\": \"ebc0f956-4672-4554-a89b-547c906cc3bd\",\n" +
//                "        \"name\": \"Testing versions\",\n" +
//                "        \"template_type\": \"email\"\n" +
//                "      },\n" +
//                "      \"template_version\": 2,\n" +
//                "      \"to\": \"someon@digital.cabinet-office.gov.uk\",\n" +
//                "      \"updated_at\": \"2016-07-15T14:38:21.486768+00:00\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"api_key\": \"someApiKey\",\n" +
//                "      \"body\": \"Hello hello\",\n" +
//                "      \"content_char_count\": null,\n" +
//                "      \"created_at\": \"2016-07-15T13:50:04.886965+00:00\",\n" +
//                "      \"id\": \"61298279-ccd7-4deb-ab8a-841947bef7a5\",\n" +
//                "      \"job\": null,\n" +
//                "      \"job_row_number\": null,\n" +
//                "      \"notification_type\": \"email\",\n" +
//                "      \"reference\": \"01020155eed24749-ed4e4f44-2e0a-4bfc-a920-29f4a73d68cd-000000\",\n" +
//                "      \"sent_at\": \"2016-07-15T13:50:06.429177+00:00\",\n" +
//                "      \"sent_by\": \"ses\",\n" +
//                "      \"service\": \"1339fcc3-7a4e-4661-aa0e-188de366bd92\",\n" +
//                "      \"status\": \"temporary-failure\",\n" +
//                "      \"subject\": \"Version 2\",\n" +
//                "      \"template\": {\n" +
//                "        \"id\": \"ebc0f956-4672-4554-a89b-547c906cc3bd\",\n" +
//                "        \"name\": \"Testing versions\",\n" +
//                "        \"template_type\": \"email\"\n" +
//                "      },\n" +
//                "      \"template_version\": 2,\n" +
//                "      \"to\": \"someon@digital.cabinet.office.gov.uk\",\n" +
//                "      \"updated_at\": \"2016-07-16T04:41:22.339992+00:00\"\n" +
//                "    }\n" +
//                "  ],\n" +
//                "  \"page_size\": 50,\n" +
//                "  \"total\": 5\n" +
//                "}";
//
//        NotificationList result = new NotificationList(content);
//        assertEquals(5, result.getNotifications().size());
//        assertEquals(5, result.getTotal());
//        assertEquals(50, result.getPageSize());
//        assertEquals("/notifications?page=2", result.getNextPageLink());
//        assertEquals("/notifications?page=3", result.getLastPageLink());
//
//    }
//}