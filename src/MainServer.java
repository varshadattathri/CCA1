import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class MainServer {

    public static void main(String[] args) throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

        server.createContext("/rank", new RankHandler());

        server.setExecutor(null);

        server.start();

        System.out.println("Server running at:");
        System.out.println("http://localhost:8080");
    }

    static class RankHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange ex)
                throws IOException {

            ex.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*"
            );

            if (ex.getRequestMethod()
                    .equalsIgnoreCase("OPTIONS")) {

                ex.sendResponseHeaders(200, -1);

                return;
            }

            String body = new String(
                    ex.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String[] data = body.split("###", -1);

            String jd = data[0];

            String r1 = data[1];

            String r2 = data[2];

            String r3 = data[3];

            String json =
                    "{"
                    + "\"resumes\":["
                    + "{"
                    + "\"name\":\"Resume 1\","
                    + "\"scorePercent\":85,"
                    + "\"fitCategory\":\"Strong Fit\","
                    + "\"explanation\":\"Good Java and SQL skills\","
                    + "\"matchedKeywords\":[\"Java\",\"SQL\"],"
                    + "\"missingKeywords\":[\"Spring\"]"
                    + "},"
                    + "{"
                    + "\"name\":\"Resume 2\","
                    + "\"scorePercent\":60,"
                    + "\"fitCategory\":\"Moderate Fit\","
                    + "\"explanation\":\"Missing backend skills\","
                    + "\"matchedKeywords\":[\"Java\"],"
                    + "\"missingKeywords\":[\"Spring\",\"SQL\"]"
                    + "}"
                    + "]"
                    + "}";

            byte[] response =
                    json.getBytes(StandardCharsets.UTF_8);

            ex.getResponseHeaders().set(
                    "Content-Type",
                    "application/json"
            );

            ex.sendResponseHeaders(200, response.length);

            OutputStream os = ex.getResponseBody();

            os.write(response);

            os.close();
        }
    }
}
