package project.model;

import com.google.gson.Gson;
import project.posts.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The class will interact with the API via HTTP
 */
public class Http {
    /**
     * @param url - the request url
     * @return String from result of API in JSON format
     */
    public HttpResponse<String> getRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//            System.out.println("Response status code was: " + response.statusCode());
//            System.out.println("Response headers were: " + response.headers());
//            System.out.println("Response body was:\n" + response.body());

//            Gson gson = new Gson();
//            Post post = gson.fromJson(response.body(), Post.class);
//            System.out.println(post.getError());

            return response;

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }

        return null;
    }

    /**
     * @param url - the request url
     * @param quantity the request quantity
     * @param measure the request measure
     * @param food the request food
     * @return String from result of API in JSON format
     */
    public HttpResponse<String> postRequest(String url, double quantity, String measure, String food) {
        try {
            Ingredients inte = new Ingredients(quantity, measure, food);
            Postbody pd = new Postbody(inte);
            Gson gson = new Gson();
            String postJSON = gson.toJson(pd);

            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(postJSON))
                    .header("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//            System.out.println("Response status code was: " + response.statusCode());
//            System.out.println("Response headers were: " + response.headers());
//            System.out.println("Response body was:\n" + response.body());

//            Post post = gson.fromJson(response.body(), Post.class);
//            System.out.println(post.getError());

            return response;
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }

        return null;
    }

    /**
     * @param from the request sender
     * @param target the request receiver
     * @param key the request key
     * @param content the request content
     * @return String from result of API in JSON format
     */
    public HttpResponse<String> emailRequest(String from, String target, String key, String content) {
        try {
            Personalizations report = new Personalizations(target);
            EmailBody email = new EmailBody(report, content, from);
            Gson gson = new Gson();
            String postJSON = gson.toJson(email);

            System.out.println(postJSON);

//            Post post = null;
            HttpRequest request = HttpRequest.newBuilder(new URI("https://api.sendgrid.com/v3/mail/send"))
                    .POST(HttpRequest.BodyPublishers.ofString(postJSON))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer "+key)
                    .build();

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//            System.out.println("Response status code was: " + response.statusCode());
//            System.out.println("Response headers were: " + response.headers());
//            System.out.println("Response body was:\n" + response.body());

//            Post post = gson.fromJson(response.body(), Post.class);
//            System.out.println(post.getError());

            return response;
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }

        return null;
    }

    /**
     * @param response String from result of API in JSON format
     * @return transfer the string to gson format
     */
    public Post getPost(HttpResponse<String> response){
        Gson gson = new Gson();
        return gson.fromJson(response.body(), Post.class);
    }

}

