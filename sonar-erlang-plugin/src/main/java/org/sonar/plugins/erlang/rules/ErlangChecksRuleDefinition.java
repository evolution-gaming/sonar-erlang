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
package org.sonar.plugins.erlang.rules;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.erlang.checks.CheckList;
import org.sonar.plugins.erlang.languages.ErlangLanguage;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

public class ErlangChecksRuleDefinition implements RulesDefinition {

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(CheckList.REPOSITORY_KEY, ErlangLanguage.KEY).setName(CheckList.REPOSITORY_NAME);
    new AnnotationBasedRulesDefinition(repository, ErlangLanguage.KEY).addRuleClasses(false, CheckList.getChecks());
    repository.done();
  }

}
