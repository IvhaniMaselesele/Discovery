package za.co.discovery.assignment;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "za.co.discovery.assignment.dataAccess")
public class DaoConfig {

}
