package ls.lesm.config;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
     public static final String AUTHORIZATION_HEADER = "Authorization";
	private ApiInfo apiInfo() {
		return new ApiInfo("Lancesoft Employee Status Monitor",   //title
				"EMPLOYEE TRACKER", //description
				"LESM 1.0", //version
				"https://www.lesm.com", //terms of service URL
				new Contact("lesm", "https://github.com/umair8k", "Umairfaisal@lancesoft.com"), //developer contact info
				"lancesoft Pvt Ltd", //license
				"https://lancesoft.com", //license URL
				Collections.emptyList()	//vendor names as list
				);
	}

	 private ApiKey apiKey() { 
	     return new ApiKey("JWT", "Authorization", "header"); 
	 }

	    @Bean
	    public Docket api(){
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .securityContexts(Arrays.asList(securityContext()))
	                .securitySchemes(Arrays.asList(apiKey()))
	                .select()
	                .apis(RequestHandlerSelectors.any())
	                .paths(PathSelectors.any())
	                .build();
	    }
	   
	    private SecurityContext securityContext(){
	        return SecurityContext.builder().securityReferences(defaultAuth()).build();
	    }

	    private List<SecurityReference> defaultAuth(){
	        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	    }	
}