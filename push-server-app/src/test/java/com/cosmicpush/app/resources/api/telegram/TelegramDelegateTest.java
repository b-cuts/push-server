/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api.telegram;

/*
import java.util.regex.*;
import org.telegram.api.TLAbsUpdates;
import org.telegram.api.auth.TLSentCode;
import org.telegram.api.engine.*;
import org.telegram.api.requests.TLRequestAuthSendCode;
import org.testng.annotations.Test;
*/

public class TelegramDelegateTest {

/*
  private static final int REQUEST_TIMEOUT = 30000;
  private static final Pattern REGEXP_PATTERN = Pattern.compile("[A-Z_0-9]+");

  public void testRun() throws Exception {
    CpAppInfo appInfo = new CpAppInfo();
    MemoryApiState apiState = new MemoryApiState(false);

    final TelegramApi api = new TelegramApi(apiState, appInfo, new ApiCallback() {
      @Override
      public void onAuthCancelled(TelegramApi telegramApi) {
        System.out.printf("");
      }

      @Override public void onUpdatesInvalidated(TelegramApi api) {
        System.out.printf("");
      }

      @Override
      public void onUpdate(TLAbsUpdates tlAbsUpdates) {
        System.out.printf("");
      }
    });

    String phoneNumber = "6577772604";

    RpcCallback callback = new RpcCallback<TLSentCode>() {
        @Override
        public void onResult(final TLSentCode result) {
          String phoneCodeHash = result.getPhoneCodeHash();
        }

        @Override
        public void onError(int errorCode, String message) {

          System.out.printf(message);

          if (errorCode == 0) {
            return;
          }

          String tagError = getErrorTag(message);

          if (errorCode == 303) {
            int destDC;
            if (tagError.startsWith("NETWORK_MIGRATE_")) {
              destDC = Integer.parseInt(tagError.substring("NETWORK_MIGRATE_".length()));
            } else if (tagError.startsWith("PHONE_MIGRATE_")) {
              destDC = Integer.parseInt(tagError.substring("PHONE_MIGRATE_".length()));
            } else if (tagError.startsWith("USER_MIGRATE_")) {
              destDC = Integer.parseInt(tagError.substring("USER_MIGRATE_".length()));
            } else {
              return;
            }
            api.switchToDc(destDC);
            startActivation();
            return;
          }

          if (tagError.equals("PHONE_NUMBER_INVALID")) {
            return;
          }

          if (tagError.startsWith("FLOOD_WAIT")) {
            return;
          }

          System.out.printf("onSmsSent error");
        }
      };

    TLRequestAuthSendCode authCode = new TLRequestAuthSendCode(phoneNumber, 0, TelegramConfig.API_ID, TelegramConfig.API_HASH, "en");
    api.doRpcCallNonAuth(authCode, REQUEST_TIMEOUT, callback);
  }

  private static void startActivation() {
    System.out.printf("Start activate...");
  }

  private static String getErrorTag(String srcMessage) {
      if (srcMessage == null) {
          return "UNKNOWN";
      }
      Matcher matcher = REGEXP_PATTERN.matcher(srcMessage);
      if (matcher.find()) {
          return matcher.group();
      }
      return "UNKNOWN";
  }
*/
}
