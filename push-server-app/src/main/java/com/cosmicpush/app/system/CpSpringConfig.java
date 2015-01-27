package com.cosmicpush.app.system;

import com.cosmicpush.jackson.CpObjectMapper;
import org.crazyyak.lib.couchace.spring.CouchPropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(value={"com.cosmicpush"})
@ImportResource("classpath:/push-server/push-server-spring.xml")
public class CpSpringConfig {

  @Bean
  public CouchPropertyPlaceholderConfigurer couchPropertyPlaceholderConfigurer() throws Exception {
    return new CouchPropertyPlaceholderConfigurer("cosmic-push");
  }

  @Bean
  public CpObjectMapper cpObjectMapper() {
    return new CpObjectMapper();
  }
}
