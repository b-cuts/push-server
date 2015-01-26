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

package com.cosmicpush.pub.internal;

import java.util.ArrayList;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ApiException;

public class RequestErrors extends ArrayList<String> {

  public RequestErrors() {
  }

  public boolean isNotEmpty() {
    return !isEmpty();
  }

  public void assertNoErrors() {
    if (isNotEmpty()) {
      throw toBadRequestException();
    }
  }

  public ApiException toBadRequestException() {
    String msg = StringUtils.toDelineatedString("\n", this);
    return ApiException.badRequest(msg);
  }
}
