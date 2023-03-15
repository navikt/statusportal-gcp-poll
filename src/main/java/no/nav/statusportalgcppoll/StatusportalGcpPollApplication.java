package no.nav.statusportalgcppoll;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class StatusportalGcpPollApplication {


	public static void main(String[] args) {

		SpringApplication.run(StatusportalGcpPollApplication.class, args);

		try{

			List<ServiceDto> services = PortalserverKlient.getPollingServices();
			System.out.println("Fetched " + services.size() +" services from statuplatform");
			List<RecordDto> recordDtos = services.stream().map(Poller::poll).collect(Collectors.toList());
			System.out.println("polled " + recordDtos.size() +" records from service endpoints");
			System.out.println(recordDtos);
			PortalserverKlient.postStatus(recordDtos);
		}
		catch (Exception e){
			System.out.println(e);
		}


	}


}
