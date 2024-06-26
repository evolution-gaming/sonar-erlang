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
package org.sonar.erlang.parser;

import com.google.common.base.Joiner;
import org.junit.Test;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ErlangParserStatementTest {
  private final LexerlessGrammar b = ErlangGrammarImpl.createGrammar();

  @Test
  public void statements() {
    assertThat(b.rule(ErlangGrammarImpl.statements))
      .matches(code("1,", "A"))
      .matches(code("1+3,", "<<A>>"))
      .matches("Flags band (bnot Flag)");
  }

  @Test
  public void ifStatements() {
    assertThat(b.rule(ErlangGrammarImpl.statements))
      .matches(code("if A =:= B -> ok end"))
      .matches(code("if A =:= B -> ok; true -> io:format(\"assert error in module ~p on line ~p~n\") end"));
  }

  @Test
  public void funStatements() {
    assertThat(b.rule(ErlangGrammarImpl.statements))
      .matches(code("fun (Name) ->" + "Spec = agner:spec(Name),"
        + "Searchable = string:to_lower(\"hElO\")" + "end"))
      .matches(code("fun	(Name) ->", "Spec = agner:spec(Name),",
        "Searchable = string:to_lower(\"hElO\");",
        "(Name, 23) when Name>=2 ->", "Spec = agner:spec(Name),",
        "Searchable = string:to_lower(\"hElO\")", "end"))
      .matches(code("fun (Name) ->" + "Spec = agner:spec(Name),"
        + "Searchable = string:to_lower(\"hElO\")" + "end()"))
      .matches(code("fun module:function/3"))
      .matches(code("fun M:F/Arity"))
      .matches(code("fun ?MODULE:passthrough_fun_to_sql/1"));

  }

  @Test
  public void caseStatements() {
    assertThat(b.rule(ErlangGrammarImpl.statements))
      .matches(code("case Signal of", "{signal, _What, _From, _To} ->", "true;",
        "{signal, _What, _To} ->", "true;", "_Else -> false", "end"));
  }

  @Test
  public void sendStatements() {
    assertThat(b.rule(ErlangGrammarImpl.statements))
      .matches(code("Client ! {self(), data_sent}"))
      .matches(code("Client ! {self(), data_sent}, A"))
      .matches(code("B, Client ! {self(), data_sent}, A"));
  }

  @Test
  public void receiveStatements() {
    assertThat(b.rule(ErlangGrammarImpl.statements))
      .matches(code("receive", "onhook ->", "disconnect(),", "idle();", "{connect, B} ->",
        "B ! {busy, self()},", "wait_for_onhook()", "after", "60000 ->",
        "disconnect(),", "error()", "end"))
      .matches(code("receive after To -> ok end"));
  }

  @Test
  public void tryStatements() {
    assertThat(b.rule(ErlangGrammarImpl.statements))
      .matches(code("try Exprs of Pattern when GuardSeq -> Body after AfterBody end"))
      .matches(code("try Exprs catch ExpressionPattern -> ExpressionBody after AfterBody end"))
      .matches(code("try Exprs catch ExpressionPattern:ExceptionBody:Stacktrace -> ExpressionBody after AfterBody end"))
      .matches(code("try Exprs after AfterBody end"))
      .matches(code("try", "{ok,Bin} = file:read(F, 1024*1024),", "binary_to_term(Bin)", "after",
        "file:close(F)", "end"))
      .matches(code("try Expr", "catch", "throw:Term -> Term;",
        "exit:Reason -> {'EXIT',Reason};",
        "error:Reason -> {'EXIT',{Reason,erlang:get_stacktrace()}}", "end"))
      .matches(code("try beam_disasm:file(Name) of %+2 statement try and call (beam_disasm:file/1) --> guess its wrong...",
        "{error,beam_lib,Reason} -> [{beam_lib,Reason}]; %+1 statement",
        "{beam_file,L} ->",
        "    {value,{code,Code0}} = lists:keysearch(code, 1, L), %+1 expression statement",
        "    Code = beam_file_1(Code0, []), %+1 statement",
        "    validate(Code) %+1 statement",
        "  catch _:_ -> [disassembly_failed] %+1 statement",
        "end"));
  }

  @Test
  public void blockStatements() {
    assertThat(b.rule(ErlangGrammarImpl.statements))
      .matches(code("begin a, S=2 end"));
  }

  private static String code(String... lines) {
    return Joiner.on("\n").join(lines);
  }

}
