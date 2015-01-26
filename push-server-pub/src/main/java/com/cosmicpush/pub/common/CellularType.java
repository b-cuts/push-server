/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cosmicpush.pub.common;

import java.io.Serializable;
import org.crazyyak.dev.common.StringUtils;

public enum CellularType implements Serializable {

  Alltel("Alltel", "@message.alltel.com"),
  ATT("AT&T", "@txt.att.net"),
  Cingular("Cingular", "@cingularme.com"),
  MetroPCS("Metro PCS", "@MyMetroPcs.com"),
  Nextel("Nextel", "@messaging.nextel.com"),
  Powertel("Powertel", "@ptel.net"),
  Sprint("Sprint", "@messaging.sprintpcs.com"),
  SunCom("SunCom", "@tms.suncom.com"),
  TMobile("T-Mobile", "@tmomail.net"),
  USCellular("US Cellular", "@email.uscc.net"),
  Verizon("Verizon", "@vtext.com"),
  VirginMobile("Virgin Mobile", "@vmobl.com"),
  Cricket("Cricket", "@mms.mycricket.com");

  private final String company;
  private final String emailRight;

  private CellularType(String company, String emailRight) {
    this.company = company;
    this.emailRight = emailRight;
  }

  public String getName() {
    return name();
  }

  public String getCompany() {
    return company;
  }

  public String getEmailRight() {
    return emailRight;
  }

  public static String emailToPhoneNumber(String address) {
    if (StringUtils.isBlank(address)) {
      return null;
    }
    int pos = address.indexOf("@");
    if (pos < 0) return address;

    return address.substring(0, pos);
  }

  public static CellularType emailToCellularType(String address) {
    if (StringUtils.isBlank(address)) {
      return null;
    }
    int pos = address.indexOf("@");
    if (pos < 0) return null;

    String value = address.substring(pos);

    for (CellularType cellularType : values()) {
      if (cellularType.getEmailRight().equals(value)) {
        return cellularType;
      }
    }
    return null;
  }
}
