/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system.jobs;

public class WipeRequests {

  public static void main(String...args) {
    try {
      new WipeRequests().run();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.exit(0);
  }

  public WipeRequests() {
  }

  public void run() throws Exception {
/*
    GenericApplicationContext springContext = new GenericApplicationContext();
    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(springContext);
    xmlReader.loadBeanDefinitions("cosmic-push-app/cosmic-push-app.xml");
    springContext.refresh();

    CpServerObjectMapper cpObjectMapper = springContext.getBean(CpServerObjectMapper.class);

    CouchSetup couchSetup = new CouchSetup("http://localhost:5986")
          .setUserName("admin")
          .setPassword("go2Couch")
          .setHttpClient(JerseyCouchHttpClient.class)
          .setJsonStrategy(new JacksonCouchJsonStrategy(cpObjectMapper));

    CpCouchServer couchServer = new CpCouchServer(couchSetup);

    AccountStore accountStore = new AccountStore(couchServer, "cosmic-push");
    ApiRequestStore apiRequestStore = new ApiRequestStore(couchServer, "cosmic-push");

    List<Account> accounts = accountStore.getAll();
    for (Account account : accounts) {
      accountStore.update(account);
      deleteForAccount(accountStore, apiRequestStore, account);
    }

    deleteOrphans(apiRequestStore);
*/
  }

/*
  private void deleteOrphans(ApiRequestStore apiRequestStore) {

    int count = 0;
    QueryResult<ApiRequest> queryResult = apiRequestStore.getAll(100);

    do {
      for (ApiRequest request : queryResult.getEntityList()) {
        apiRequestStore.delete(request);
      }
      count += queryResult.getSize();
      System.out.printf("Deleted %s records\n", count);

    } while (queryResult.nextPage());

  }
*/

/*
  private void deleteForAccount(accountStore accountStore, ApiRequestStore apiRequestStore, Account account) {

    for (Domain domain : account.getDomains()) {

      int count = 0;
      QueryResult<ApiRequest> queryResult = apiRequestStore.getByClient(domain, 100);

      do {
        for (ApiRequest request : queryResult.getEntityList()) {
          apiRequestStore.delete(request);
        }
        count += queryResult.getSize();
        System.out.printf("Deleted %s records\n", count);

      } while (queryResult.nextPage());
    }
  }
*/
}
