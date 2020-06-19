package za.co.discovery.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication(exclude = {SolrAutoConfiguration.class})
@Configuration
public class DiscoveryAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryAssignmentApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        return template;
    }

    @Bean
    public InputStream dataReading() throws IOException {
        String filename = "/planetTravelDetails.xlsx";
        ClassPathResource classPathResource = new ClassPathResource(filename);
        return classPathResource.getInputStream();
    }

}