package com.cosmicpush.app.system;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.*;
import com.couchace.core.api.response.EntityDocument;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.*;
import org.crazyyak.dev.common.DateUtils;

import static java.lang.String.format;

public class CpJobs {

  public static final Log log = LogFactory.getLog(CpJobs.class);
  public static final String PDT = "America/Los_Angeles";

  private static final AtomicBoolean runningCompact = new AtomicBoolean(false);
  private static final AtomicBoolean runningPruner = new AtomicBoolean(false);

  private final AppContext appContext;

  public CpJobs(AppContext appContext) {
    this.appContext = appContext;
  }

  @PostConstruct
  public void runAtStartup() {
    cleanAndCompactDatabase();
  }

  // Every night at midnight
  // @Scheduled(cron="0 0 0 * * *", zone=PDT)
  public void cleanAndCompactDatabase() {

    if (runningCompact.compareAndSet(false, true) == false) {
      return;
    }

    try {
      appContext.getCouchServer().compactAndCleanAll();

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      runningCompact.set(false);
    }
  }

  // @Scheduled(initialDelay = 1000*60, fixedDelay = 1000*60*60, zone=PDT)
  public void pruneEvents() {

    if (runningPruner.compareAndSet(false, true) == false) {
      return;
    }

    try {
      LocalDateTime now = DateUtils.currentLocalDateTime();
      List<Account> accounts = appContext.getAccountStore().getAll();

      for (Account account : accounts) {
        pruneEvents(now, account);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      runningPruner.set(false);
    }
  }

  private void pruneEvents(LocalDateTime now, Account account) {

    if (account.getRetentionDays() <= 0) {
      return;
    }

    List<ApiClient> apiClients = account.getApiClients();
    for (ApiClient apiClient : apiClients) {

      int count = 0;
      QueryResult<ApiRequest> queryResult = appContext.getApiRequestStore().getByClient(apiClient, 100);

      do {
        List<EntityDocument<ApiRequest>> list = queryResult.getDocumentList();
        for (EntityDocument<ApiRequest> document : list) {
          pruneEvents(now, account, document);
        }

        count += queryResult.getSize();
        log.info(format("Deleted %s records\n", count));

      } while (queryResult.nextPage());
    }
  }

  private void pruneEvents(LocalDateTime now, Account account, EntityDocument<ApiRequest> document) {
    int days = account.getRetentionDays();
    ApiRequest apiRequest = document.getEntity();
    LocalDateTime later = apiRequest.getCreatedAt().plusWeeks(days);
    if (now.isAfter(later)) {
      appContext.getApiRequestStore().deleteByDocumentId(
        document.getDocumentId(),
        document.getDocumentRevision()
      );
    }
  }
}
