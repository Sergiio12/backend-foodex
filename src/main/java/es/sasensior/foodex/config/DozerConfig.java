 package es.sasensior.foodex.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfig {
	
	@Bean(name = "mapper")
	DozerBeanMapper getMapper() {
		return new DozerBeanMapper(); 
		
	}

}
