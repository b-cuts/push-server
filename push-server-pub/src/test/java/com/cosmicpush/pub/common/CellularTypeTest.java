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

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class CellularTypeTest {

  public void emailToPhoneNumber() throws Exception {
    assertEquals(CellularType.emailToPhoneNumber(null), null);
    assertEquals(CellularType.emailToPhoneNumber(""), null);
    assertEquals(CellularType.emailToPhoneNumber("5596585737"), "5596585737");
    assertEquals(CellularType.emailToPhoneNumber("stcg.net"), "stcg.net");
    assertEquals(CellularType.emailToPhoneNumber("5596585737@stcg.net"), "5596585737");
  }

  public void testParseServerName() throws Exception {
    assertEquals(CellularType.emailToCellularType(null), null);
    assertEquals(CellularType.emailToCellularType(""), null);
    assertEquals(CellularType.emailToCellularType("5596585737"), null);
    assertEquals(CellularType.emailToCellularType("stcg.net"), null);
    assertEquals(CellularType.emailToCellularType("@stcg.net"), null);
    assertEquals(CellularType.emailToCellularType("5596585737@stcg.net"), null);
    assertEquals(CellularType.emailToCellularType("5596585737@txt.att.net"), CellularType.ATT);
  }
}
