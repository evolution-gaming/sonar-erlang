/*
 * SonarQube Erlang Plugin
 * Copyright © 2012-2018 Tamas Kende <kende.tamas@gmail.com>
 * Copyright © 2018 Denes Hegedus (Cursor Insight Ltd.) <hegedenes@cursorinsight.com>
 * Copyright © 2020 Andris Raugulis <moo@arthepsy.eu>
 * Copyright © 2021 Daniils Petrovs <dpetrovs@evolution.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sonar.erlang.checks;


import org.sonar.squidbridge.checks.CheckMessagesVerifier;
import org.junit.Test;

import org.sonar.squidbridge.api.SourceFile;

import java.io.File;

public class IsTailRecursiveCheckTest {

  @Test
  public void test() {
    IsTailRecursiveCheck check = new IsTailRecursiveCheck();

    SourceFile file = TestHelper.scanSingleFile(new File(
      "src/test/resources/checks/branchesofrecursion.erl"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages()).next().atLine(4).noMore();
  }

  @Test
  public void test2() {
    IsTailRecursiveCheck check = new IsTailRecursiveCheck();
    SourceFile file = TestHelper.scanSingleFile(new File(
      "src/test/resources/checks/istailrecursive.erl"), check);

    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(7)
      .next().atLine(12)
      .noMore();
  }

  @Test
  public void test_bug() {
    IsTailRecursiveCheck check = new IsTailRecursiveCheck();
    SourceFile file = TestHelper.scanSingleFile(new File(
      "src/test/resources/checks/mod_configure.erl"), check);


  }
}
