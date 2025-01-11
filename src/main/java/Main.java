import Controller.SocialMediaController;
import Model.Message;
import Util.ConnectionUtil;
import io.javalin.Javalin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);
        messageTest();
    }
    public static void messageTest() {
        // For logging
        ConnectionUtil.resetTestDatabase();
        HttpClient webClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/login"))
            .POST(HttpRequest.BodyPublishers.ofString("{" +
                    "\"username\": \"testuser1\", " +
                    "\"password\": \"password\" }"))
            .header("Content-Type", "application/json")
            .build();
            HttpResponse response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();

            System.out.println(status);
        } catch (InterruptedException e) {

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }
}
