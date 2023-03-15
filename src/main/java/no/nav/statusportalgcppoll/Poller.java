package no.nav.statusportalgcppoll;

import com.google.gson.Gson;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class Poller {
    static String portalApiUrl = System.getenv("portalserver_path");

    private static final String MOCK_URL = "https//servicemock/mock/Service/";
    private static final  String MOCK = "MOCK";
    private static final  String STATUSHOLDER = "STATUSHOLDER";
    private static final String STATUSHOLDER_URL = System.getenv("statusholder_url");

    public static RecordDto poll(ServiceDto serviceDto){
        try{
            LocalDateTime before = LocalDateTime.now();
            RecordDto recordDto = getPolledServiceStatus(serviceDto);
            LocalDateTime after = LocalDateTime.now();
            Integer responseTime = calcDiffBetween(before,after);
            recordDto.setResponseTime(responseTime);
            return  recordDto;
            //updateRecordForService(polledServiceStatus,serviceDto, responseTime);

        }

        catch (Exception e){

            System.out.println("private void poll Exception!!: " + e);

            return createPolledServiceStatusForUnresponsiveEndpoint(serviceDto);

        }
    }

    private static RecordDto getPolledServiceStatus(ServiceDto serviceDto) throws IOException {
        HttpURLConnection connection = getConnectionToServicePollEndpoint(serviceDto);
        String bodyString = readBody(connection);
        connection.disconnect();
        JsonObject jsonObject = toJson(bodyString);
        RecordDto recordDto = mapToRecordDto(jsonObject);
        recordDto.setTimestamp(OffsetDateTime.now());
        recordDto.serviceId(serviceDto.getId());
        return recordDto;
    }

    private static RecordDto mapToRecordDto(JsonObject jsonRecord){
        RecordDto recordDto = new RecordDto();
        recordDto.setStatus(StatusDto.fromValue(jsonRecord.getString("status")));
        recordDto.setDescription(jsonRecord.getString("description",null));
        recordDto.setLogLink(jsonRecord.getString("logglink",null));
        return recordDto;

    }

    private static HttpURLConnection getConnectionToServicePollEndpoint(ServiceDto serviceDto) throws IOException {
        String urlString;
        switch (serviceDto.getPollingUrl()){
            case MOCK: urlString = MOCK_URL + serviceDto.getId(); break;
            case STATUSHOLDER: urlString = STATUSHOLDER_URL+"/status/" + serviceDto.getId();break;
            default: urlString = serviceDto.getPollingUrl();
        }
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return con;
    }


    private static Integer calcDiffBetween(LocalDateTime before, LocalDateTime after) {
        Duration duration = Duration.between(after, before);
        return duration.toMillisPart();
    }
    private static HttpURLConnection getPollingServices() throws IOException {
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

    private static RecordDto createPolledServiceStatusForUnresponsiveEndpoint(ServiceDto serviceDto){
        return new RecordDto()
                .serviceId(serviceDto.getId())
                .description("Service status endpoint is not responding")
                .status(StatusDto.ISSUE)
                .timestamp(OffsetDateTime.now());
    }


    private static JsonObject toJson(String str){
        JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

}
