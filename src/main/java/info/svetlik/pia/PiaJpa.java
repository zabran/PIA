package info.svetlik.pia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

import info.svetlik.pia.configuration.AppConfig;
import info.svetlik.pia.configuration.DbConfig;

//ondrej@svetlik.info
//flexibee.en

@SpringBootApplication
@Import({
	AppConfig.class,
	DbConfig.class
})
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class PiaJpa {

	public static void main(String[] args) {
		SpringApplication.run(PiaJpa.class, args);
	}

}
