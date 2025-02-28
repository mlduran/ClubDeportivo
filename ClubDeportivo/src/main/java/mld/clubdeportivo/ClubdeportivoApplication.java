package mld.clubdeportivo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@ServletComponentScan
@SpringBootApplication
public class ClubdeportivoApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ClubdeportivoApplication.class, args);
                
	}

}
