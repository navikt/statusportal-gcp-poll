package no.nav.statusportalgcppoll;

import com.google.gson.Gson;
import no.nav.statusportalgcppoll.util.OauthUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication
public class StatusportalGcpPollApplication {


//	static String portalApiUrl = "http://localhost:3005";
//	static String portalApiUrl =  "http://portalserver";
	static String portalApiUrl =  "https://status-api.intern.dev.nav.no";

	public static void main(String[] args) {

		SpringApplication.run(StatusportalGcpPollApplication.class, args);
//		try{
//			postStatus();
//		}
//		catch (Exception e){
//			System.out.println(e);
//		}
		try{
			HttpURLConnection con = getPollingServices();
			String body = readBody(con);
			Gson g = new Gson();
			List<ServiceDto> services = Arrays.asList(g.fromJson(body, ServiceDto[].class));
			services = services.stream().filter(s -> s.getPollingUrl() != null && !s.getPollingUrl().equals("")).collect(Collectors.toList());
//			services.forEach(s -> s.setPollingUrl("MOCK"));
			List<RecordDto> recordDtos = services.stream().map(Poller::poll).collect(Collectors.toList());
			System.out.println(recordDtos);
		}
		catch (Exception e){
			System.out.println(e);
		}


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

	private static void postStatus() throws Exception {
		URL url = new URL (portalApiUrl + "/rest/Dashboard");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		String beaerToken = OauthUtil.getAccessTokenForPortal();
		System.out.println(beaerToken);
		con.setRequestProperty ("Authorization", OauthUtil.getAccessTokenForPortal());
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		UUID uuid = UUID.randomUUID();
		String jsonInputString = "{\"name\": \"PollingDashboard\"}";
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
