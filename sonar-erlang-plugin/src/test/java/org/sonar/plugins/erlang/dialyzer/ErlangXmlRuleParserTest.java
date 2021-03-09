package org.sonar.plugins.erlang.dialyzer;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.rules.Rule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class ErlangXmlRuleParserTest {
  private final File dialyzerRuleFile = new File("src/test/resources/org/sonar/plugins/erlang/dialyzer/rules.xml");

  @Test
  public void testParseSuccess() {
    try {
      InputStream in = new FileInputStream(dialyzerRuleFile);

      ErlangXmlRuleParser parser = new ErlangXmlRuleParser();
      List<ErlangRule> rules = parser.parse(in);

      Rule rule = rules.get(0).getRule();

      Assert.assertEquals(
              "X001",
              rule.getKey()
      );

      Assert.assertEquals(
              "Undefined function calls",
              rule.getName()
      );

      Assert.assertEquals(
              "Undefined function calls",
              rule.getDescription()
      );

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
