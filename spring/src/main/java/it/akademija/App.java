package it.akademija;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
<<<<<<< HEAD
//@EnableWebMvc  // this one is for spring security https://www.baeldung.com/spring-mvc-tutorial
=======
@EnableWebMvc  // this one is for spring security https://www.baeldung.com/spring-mvc-tutorial
>>>>>>> da92aed... some stuff which does not work
@ImportResource({"classpath*:application-context.xml"}) // iesko sitam faile beans aprasymo
public class App extends SpringBootServletInitializer {@Bean
public Docket swaggerDocket() {
	return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(apiInfo())
			.select()
			.apis(RequestHandlerSelectors.basePackage("it.akademija"))
			.build();
}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("REST Documentation")
				.version("0.0.1-SNAPSHOT")
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}