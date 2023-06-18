package com.infinia.scrollup.driver;

import com.infinia.scrollup.driver.conf.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class ScrollUpDriverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrollUpDriverApplication.class, args);
	}

}
