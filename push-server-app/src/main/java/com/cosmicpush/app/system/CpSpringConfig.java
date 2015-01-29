package com.cosmicpush.app.system;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import org.crazyyak.lib.spring.couchace.CouchPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Value;
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

  @Bean
  public CpCouchServer getCpCouchServer(@Value("${couchdb.name}") String databaseName) {
    return new CpCouchServer(databaseName);
  }

  @Bean
  public AccountStore getAccountStore(CpCouchServer couchServer) {
    return new AccountStore(couchServer);
  }

  @Bean
  public ApiRequestStore getApiRequestStore(CpCouchServer couchServer) {
    return new ApiRequestStore(couchServer);
  }

}
