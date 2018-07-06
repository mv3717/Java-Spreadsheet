package common.gui;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import common.parser.ParseException;
import common.parser.Parser;
import common.parser.TokenMgrError;

public class Util {
  /**
   * @param text
   * @return a list of assignment-expression pairs {cell-reference, expression}.
   * 
   * @throws ParseException if an invalid command sequence is encountered.
   */
  public static List<String[]> extractOperations(String text)
      throws ParseException {

    List<String[]> result = new ArrayList<String[]>();
    StringTokenizer stStatement = new StringTokenizer(text, ";");
    while (stStatement.hasMoreTokens()) {
      StringTokenizer stAssignment =
          new StringTokenizer(stStatement.nextToken(), "=");
      while (stAssignment.hasMoreTokens()) {
        String left = stAssignment.nextToken().trim();

        if (!stAssignment.hasMoreTokens()) {
          throw new ParseException(
              "Invalid Command. Expected: <cell> = <expression>");
        }

        String right = stAssignment.nextToken().trim();

        if (isLocation(left)) {
          if (isFormula("=" + right)) {
            result.add(new String[] { left, "=" + right });
          } else {
            result.add(new String[] { left, right });
          }

        } else {
          throw new ParseException("Invalid Command: " + left + "=" + right);
        }
      }

    }
    return result;
  }

  /**
   * Tests if the specified String is a valid cell reference
   * 
   * @param text
   * @return <tt>true</tt> if the specified text is a valid cell reference.
   */
  public static boolean isLocation(String text) {
    if (text == null || text.length() < 2)
      return false;

    try {
      return new Parser(new StringReader(text)).testLocation();
    } catch (ParseException e) {
      return false;
    } catch (TokenMgrError e) {
      return false;
    }
  }

  /**
   * Tests if the specified String is a valid formula.
   * 
   * @param text suspected numerical text
   * @return <tt>true</tt>
   */
  public static boolean isFormula(String text) {

    if (text == null || text.length() <= 0)
      return false;

    try {
      return new Parser(new StringReader(text)).testNumerical();
    } catch (ParseException e) {
      return false;
    } catch (TokenMgrError e) {
      return false;
    }

  }
}
