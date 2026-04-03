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
public final class DotChainAssignmentIndentTest {

  @Test
  public void assignmentRhsDotChainUsesTwoSpaceContinuation() throws FormatterException {
    String input =
        """
        import java.time.LocalDate;
        import java.util.List;

        class AssignmentDotChainIndent {
          void format(List<Account> subsidiaryAccounts) {
            LocalDate nearestExpirationDate = subsidiaryAccounts.stream()
                    .map(Account::getExpirationDate)
                    .filter(expirationDate -> expirationDate != null)
                    .min(LocalDate::compareTo)
                    .orElse(null);
          }

          static class Account {
            LocalDate getExpirationDate() {
              return null;
            }
          }
        }
        """;

    String expected =
        """
        import java.time.LocalDate;
        import java.util.List;

        class AssignmentDotChainIndent {
          void format(List<Account> subsidiaryAccounts) {
            LocalDate nearestExpirationDate = subsidiaryAccounts.stream()
              .map(Account::getExpirationDate)
              .filter(expirationDate -> expirationDate != null)
              .min(LocalDate::compareTo)
              .orElse(null);
          }

          static class Account {
            LocalDate getExpirationDate() {
              return null;
            }
          }
        }
        """;

    assertThat(format(input)).isEqualTo(expected);
  }

  @Test
  public void regularDotChainComputeIfAbsentRemainsStable() throws FormatterException {
    String input =
        """
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

        class RegularChain {
          void format(Map<String, List<Account>> accountsBySubsidiary, Account account) {
            accountsBySubsidiary
              .computeIfAbsent(account.getSubsidiary(), ignored -> new ArrayList<>())
              .add(account);
          }

          static class Account {
            String getSubsidiary() {
              return null;
            }
          }
        }
        """;

    String expected =
        """
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

        class RegularChain {
          void format(Map<String, List<Account>> accountsBySubsidiary, Account account) {
            accountsBySubsidiary
              .computeIfAbsent(account.getSubsidiary(), ignored -> new ArrayList<>())
              .add(account);
          }

          static class Account {
            String getSubsidiary() {
              return null;
            }
          }
        }
        """;

    assertThat(format(input)).isEqualTo(expected);
  }

  @Test
  public void builderChainInsideMethodArgumentRemainsStable() throws FormatterException {
    String input =
        """
        import java.math.BigDecimal;
        import java.time.LocalDate;
        import java.util.List;

        class BuilderChain {
          void format(
              List<BalanceDetailDTO> detail,
              String subsidiary,
              BigDecimal balance,
              LocalDate nearestExpirationDate,
              BigDecimal balanceToBeExpired) {
            detail.add(
              BalanceDetailDTO.builder()
                .subsidiary(subsidiary)
                .balance(balance)
                .nearestExpirationDate(nearestExpirationDate)
                .balanceToBeExpired(balanceToBeExpired)
                .build());
          }

          static class BalanceDetailDTO {
            static Builder builder() {
              return null;
            }

            interface Builder {
              Builder subsidiary(String value);

              Builder balance(BigDecimal value);

              Builder nearestExpirationDate(LocalDate value);

              Builder balanceToBeExpired(BigDecimal value);

              BalanceDetailDTO build();
            }
          }
        }
        """;

    String expected =
        """
        import java.math.BigDecimal;
        import java.time.LocalDate;
        import java.util.List;

        class BuilderChain {
          void format(
            List<BalanceDetailDTO> detail,
            String subsidiary,
            BigDecimal balance,
            LocalDate nearestExpirationDate,
            BigDecimal balanceToBeExpired) {
            detail.add(
              BalanceDetailDTO.builder()
                .subsidiary(subsidiary)
                .balance(balance)
                .nearestExpirationDate(nearestExpirationDate)
                .balanceToBeExpired(balanceToBeExpired)
                .build());
          }

          static class BalanceDetailDTO {
            static Builder builder() {
              return null;
            }

            interface Builder {
              Builder subsidiary(String value);

              Builder balance(BigDecimal value);

              Builder nearestExpirationDate(LocalDate value);

              Builder balanceToBeExpired(BigDecimal value);

              BalanceDetailDTO build();
            }
          }
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
