import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestNotificationClient {

    /**
     *  Args: secret, issuer, baseUrl
      * @param args
     * @throws Exception
     */
//    public static void main(String[] args) throws Exception {
//        NotificationClient client = new NotificationClient(args[0], args[1], args[2]);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Enter type of message (sms|email): ");
//        String messageType = reader.readLine();
//
//        String to;
//        if(messageType.equals("sms")){
//            System.out.println("Enter the mobile number: ");
//            to = reader.readLine();
//        }
//        else if (messageType.equals("email")){
//            System.out.println("Enter the email address: ");
//            to = reader.readLine();
//        }
//        else{
//            throw new Exception("Please enter email or sms for messageType");
//        }
//        System.out.println("Enter the template id: ");
//        String templateId = reader.readLine();
//        System.out.println("Enter the personalisation (as JSON): ");
//        String personalisation = reader.readLine();
//        if(messageType.equals("sms")){
//            client.sendSms(templateId, to, personalisation);
//        }
//        else{
//            client.sendEmail(templateId, to, personalisation);
//        }
//    }

    public static void main(String[] args) throws Exception {
        NotificationClient client = new NotificationClient(args[0], args[1], args[2]);
//        NotificationResponse response = client.sendEmail("ebc0f956-4672-4554-a89b-547c906cc3bd", "rebecca.law@digital.cabinet-office.gov.uk", null);
//        client.sendSms("43284901-6c7f-4224-9c18-ff73610a9df1", "+447515349060", null);
        Notification notification = client.getNotificationById("0585c4bc-d9d1-4c77-a7f8-7cb14ac28067");
    }
}
