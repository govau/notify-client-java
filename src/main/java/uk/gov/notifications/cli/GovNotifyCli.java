package uk.gov.notifications.cli;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;

import uk.gov.notifications.client.api.ClientConfiguration;
import uk.gov.notifications.client.api.GovNotifyApi;
import uk.gov.notifications.client.api.GovNotifyApiClient;
import uk.gov.notifications.client.model.EmailRequest;
import uk.gov.notifications.client.model.NotificationCreatedResponse;
import uk.gov.notifications.client.model.SmsRequest;
import uk.gov.notifications.client.model.StatusResponse;

import static uk.gov.notifications.client.model.Personalisation.fromJsonString;

public class GovNotifyCli {

    public static void main(String[] args) throws Exception {

        ArgumentParser parser = ArgumentParsers.newArgumentParser("./gov-notify-cli")
                .defaultHelp(true)
                .description("CLI version of the Gov.UK Notify API client");

        parser.addArgument("<url>").help("Url to Gov.UK Notify service");
        parser.addArgument("<service>").help("Your service id");
        parser.addArgument("<command>").help("Command: createSms|createEmail|checkStatus");

        Namespace ns = parser.parseArgsOrFail(args);

        String url = ns.getString("<url>");
        String serviceId = ns.get("<service>");
        String call = ns.getString("<command>");

        ClientConfiguration.Builder configBuilder = ClientConfiguration.builder()
                .baseUrl(url).serviceId(serviceId);

        try {
            if ("createSms".equalsIgnoreCase(call)) {
                sendSms(configBuilder);
            } else if ("createEmail".equalsIgnoreCase(call)) {
                sendEmail(configBuilder);
            } else if ("checkStatus".equalsIgnoreCase(call)) {
                checkStatus(configBuilder);
            } else {
                parser.printHelp();
            }
        } catch (Exception gnce) {
            System.out.println("\nerror: " + gnce.getMessage());
        }
    }

    private static void sendSms(ClientConfiguration.Builder builder) throws Exception {

        String phone = ask("enter number (+441234123123): ");
        String templateId = ask("enter template id: ");
        String personalisationJson = ask("enter personalisation (as JSON): ");
        String secret = askPassword("enter API key (secret): ");

        SmsRequest request = SmsRequest.builder().phoneNumber(phone).templateId(templateId)
                .personalisation(fromJsonString(personalisationJson))
                .build();
        GovNotifyApi client = new GovNotifyApiClient(builder.secret(secret).build());

        NotificationCreatedResponse response = client.sendSms(request);

        System.out.println("Notification id: " + response.getId());
    }

    private static void sendEmail(ClientConfiguration.Builder builder) throws Exception {

        String email = ask("enter email: ");
        String templateId = ask("enter template id: ");
        String personalisationJson = ask("enter personalisation (as JSON): ");
        String secret = askPassword("enter API key (secret): ");

        EmailRequest request = EmailRequest.builder().email(email).templateId(templateId)
                .personalisation(fromJsonString(personalisationJson))
                .build();

        GovNotifyApi client = new GovNotifyApiClient(builder.secret(secret).build());
        NotificationCreatedResponse response = client.sendEmail(request);

        System.out.println("Notification id: " + response.getId());
    }

    private static void checkStatus(ClientConfiguration.Builder builder) throws Exception {

        String notificationId = ask("enter notification id: ");
        String secret = askPassword("enter API key (secret): ");

        GovNotifyApi client = new GovNotifyApiClient(builder.secret(secret).build());
        StatusResponse response = client.checkStatus(notificationId);

        System.out.println("Status: " + response.toString());
    }

    private static String ask(String label) {
        return System.console().readLine(label);
    }

    private static String askPassword(String label) {
        return new String(System.console().readPassword(label));
    }
}




