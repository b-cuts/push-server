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
    PushRequestStore pushRequestStore = new PushRequestStore(couchServer, "cosmic-push");

    List<Account> accounts = accountStore.getAll();
    for (Account account : accounts) {
      accountStore.update(account);
      deleteForAccount(accountStore, pushRequestStore, account);
    }

    deleteOrphans(pushRequestStore);
*/
  }

/*
  private void deleteOrphans(PushRequestStore pushRequestStore) {

    int count = 0;
    QueryResult<PushRequest> queryResult = pushRequestStore.getAll(100);

    do {
      for (PushRequest request : queryResult.getEntityList()) {
        pushRequestStore.delete(request);
      }
      count += queryResult.getSize();
      System.out.printf("Deleted %s records\n", count);

    } while (queryResult.nextPage());

  }
*/

/*
  private void deleteForAccount(accountStore accountStore, PushRequestStore pushRequestStore, Account account) {

    for (Domain domain : account.getDomains()) {

      int count = 0;
      QueryResult<PushRequest> queryResult = pushRequestStore.getByClient(domain, 100);

      do {
        for (PushRequest request : queryResult.getEntityList()) {
          pushRequestStore.delete(request);
        }
        count += queryResult.getSize();
        System.out.printf("Deleted %s records\n", count);

      } while (queryResult.nextPage());
    }
  }
*/
}
