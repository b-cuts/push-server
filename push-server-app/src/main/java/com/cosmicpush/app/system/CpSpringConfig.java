package com.cosmicpush.app.system;

import com.cosmicpush.common.DiyBeanFactory;
import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import org.crazyyak.lib.spring.couchace.CouchPropertyPlaceholderConfigurer;
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
    return DiyBeanFactory.get().getObjectMapper();
  }

  @Bean
  public CpCouchServer getCpCouchServer() {
    return DiyBeanFactory.get().getCouchServer();
  }

  @Bean
  public AccountStore getAccountStore() {
    return DiyBeanFactory.get().getAccountStore();
  }

  @Bean
  public ApiRequestStore getApiRequestStore() {
    return DiyBeanFactory.get().getApiRequestStore();
  }

}
