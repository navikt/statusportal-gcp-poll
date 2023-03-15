package no.nav.statusportalgcppoll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.nav.statusportalgcppoll.util.OauthUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PortalserverKlient {

    static String portalApiUrl = System.getenv("portalserver_path");

    public static List<ServiceDto> getPollingServices() throws IOException{
            HttpURLConnection con = getPollingServicesConnection();
            String body = readBody(con);
            Gson g = new Gson();
            List<ServiceDto> services = Arrays.asList(g.fromJson(body, ServiceDto[].class));
            services = services.stream().filter(s -> s.getPollingUrl() != null
                    && !s.getPollingUrl().equals("")
                    && !s.getPollingUrl().equals("null"))
                    .collect(Collectors.toList());

            if(System.getenv("ENV").equals("dev")){
                services.forEach(s -> s.setPollingUrl("MOCK"));
            }
            return services;

    }



    private static HttpURLConnection getPollingServicesConnection() throws IOException {
        String urlString = portalApiUrl+ "/rest/Services/Minimal";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    private static String readBody(HttpURLConnection con) throws IOException {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }


    public static void postStatus(List<RecordDto> recordDtos) throws Exception {
        URL url = new URL (portalApiUrl + "/rest/UpdateRecords");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty ("Authorization", OauthUtil.getAccessTokenForPortal());
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RecordDto.class,new RecordDto.RecordDtoAdapter())
                .create();
        String jsonInputString = gson.toJson(recordDtos);
        try(OutputStream os = con.getOutputStream()) {

            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response);
            System.out.println("Postet status ok");
        }
    }
}
