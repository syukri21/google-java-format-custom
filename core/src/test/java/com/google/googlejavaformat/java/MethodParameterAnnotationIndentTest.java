/*
 * Copyright 2026 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.googlejavaformat.java;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class MethodParameterAnnotationIndentTest {

  @Test
  public void annotatedMethodParameterTypeUsesTwoSpaceContinuation() throws FormatterException {
    String input =
        """
        interface Api {
          SuccessResponse<CreditResponseDto> credit(
            @org.springframework.web.bind.annotation.RequestHeader("x-idempotency-key")
             String idempotencyKey,
            CreditRequestDto requestDto);
        }
        """;

    String expected =
        """
        interface Api {
          SuccessResponse<CreditResponseDto> credit(
            @org.springframework.web.bind.annotation.RequestHeader("x-idempotency-key")
            String idempotencyKey,
            CreditRequestDto requestDto);
        }
        """;

    assertThat(format(input)).isEqualTo(expected);
  }

  private static String format(String input) throws FormatterException {
    Formatter formatter = new Formatter();
    String output = formatter.formatSource(input);
    return StringWrapper.wrap(output, formatter);
  }
}
