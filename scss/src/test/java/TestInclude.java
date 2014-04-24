import org.testng.Assert;
import org.testng.annotations.Test;


public class TestInclude extends TestBase
{
  @Test
  public void testInclude()
  {
    String [] lines = {
        "@include large-text;"
    };
    ScssParser.StylesheetContext context = parse(lines);
    Assert.assertEquals(context.statement(0).includeDeclaration().Identifier().getText(), "large-text");
  }

  @Test
  public void testIncludeInBlock()
  {
    String [] lines = {
        ".test {",
        "  @include large-text;",
        "}"
    };
    ScssParser.StylesheetContext context = parse(lines);
    Assert.assertEquals(context.statement(0).ruleset().block().statement(0).includeDeclaration().Identifier().getText(), "large-text");
  }

  @Test
  public void testMultipleIncludes()
  {
    String [] lines = {
        "@include large-text;",
        "@include large-button;",

    };
    ScssParser.StylesheetContext context = parse(lines);
    Assert.assertEquals(context.statement(0).includeDeclaration().Identifier().getText(), "large-text");
    Assert.assertEquals(context.statement(1).includeDeclaration().Identifier().getText(), "large-button");

  }

  @Test
  public void testIncludesInMixin()
  {
    String [] lines = {
        "@mixin large-text {",
        "   @include large-button;",
        "}"

    };
    ScssParser.StylesheetContext context = parse(lines);
    Assert.assertEquals(context.statement(0).mixinDeclaration().Identifier().getText(), "large-text");
    Assert.assertEquals(context.statement(0).mixinDeclaration().block().statement(0)
                            .includeDeclaration().Identifier().getText(), "large-button");

  }


  @Test
  public void testIncludesWithParams()
  {
    String [] lines = {
        "@include large-button(blue, $var);"

    };
    ScssParser.StylesheetContext context = parse(lines);
    Assert.assertEquals(context.statement(0).includeDeclaration().Identifier().getText(), "large-button");
    Assert.assertEquals(context.statement(0).includeDeclaration().parameters()
                            .parameter(0).paramValue().Expression().getText(), "blue");
    Assert.assertEquals(context.statement(0).includeDeclaration().parameters()
                            .parameter(1).paramValue().ParamName().getText(), "$var");

  }

  @Test
  public void testIncludesWithBody()
  {
    String [] lines = {
        "@include large-button {",
        "  color: black",
        "}"

    };
    ScssParser.StylesheetContext context = parse(lines);
    Assert.assertEquals(context.statement(0).includeDeclaration().Identifier().getText(), "large-button");
    Assert.assertEquals(context.statement(0).includeDeclaration().block().property(0).identifier().getText(), "color");
    Assert.assertEquals(context.statement(0).includeDeclaration().block().property(0)
                            .value().commandStatement(0).expression().identifier().getText(), "black");


  }

  @Test
  public void testIncludesWithInterpolation()
  {
    String [] lines = {
        "@include large-button (#{$var1}) {",
        "  color-#{$var2}: black",
        "}"

    };
    ScssParser.IncludeDeclarationContext context = parse(lines).statement(0).includeDeclaration();

    Assert.assertEquals(context.parameters().parameter(0).paramValue().paramInterpolation().ParamName().getText(), "$var1");
    Assert.assertEquals(context.block().property(0).identifier().Identifier(0).getText(), "color-");
    Assert.assertEquals(context.block().property(0).identifier().interpolation(0).variableName().getText(), "$var2");



  }

}
