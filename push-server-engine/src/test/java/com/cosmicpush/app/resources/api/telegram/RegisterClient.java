/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api.telegram;

/*
import org.telegram.api.engine.*;
import org.telegram.mtproto.state.ConnectionInfo;
import org.telegram.tl.*;
*/

public class RegisterClient {

/*
  private static final int REQUEST_TIMEOUT = 30000;

  public static void main(String...args) {
    try {
      new RegisterClient().run();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.exit(0);
  }

  public void run() throws Exception {

    ConnectionInfo[] connections = new ConnectionInfo[]{
        new ConnectionInfo(1, 0, "109.239.131.195", 80)
        // new ConnectionInfo(1, 0, "173.240.5.253", 443)
    };

    MemoryApiState apiState = new MemoryApiState(connections);
    TelegramApi api = new TelegramApi(apiState, new CpAppInfo(), new CpApiCallback());

    TLSaveDeveloperInfo info = new TLSaveDeveloperInfo("Cosmic Push", "jacob.parr@munchiemonster.com", "15596407277", 38, "Oakhurst");
    TLObject object = api.doRpcCallNonAuth(info, 120*1000, 1);

    System.out.printf("");

  }
*/
}
