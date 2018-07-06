package common.api;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import common.api.value.DoubleValue;
import common.api.value.StringValue;
import common.api.value.Value;
import common.parser.MissingValueException;
import common.parser.ParseException;
import common.parser.Parser;
import common.parser.TokenMgrError;

/**
 * Two utility methods for processing expressions represented as Strings.
 * 
 */
public final class ExpressionUtils {

  /**
   * Return a set (without duplicates) containing the locations of
   * cell-references used in the specified formula.
   * 
   * @param expression the expression from which to extract the cell references.
   * @return the set of CellLocations needed to evaluate the specified
   *         expression.
   */
  public static Set<CellLocation> getReferencedLocations(String expression) {
    Set<String> dependencySet = new LinkedHashSet<String>();

    try {
      if (new Parser(new StringReader(expression))
          .build(dependencySet) == Parser.BUILD_OK) {
        Set<CellLocation> out = new LinkedHashSet<CellLocation>();
        for (String s : dependencySet) {
          out.add(new CellLocation(s));
        }
        return out;
      }
    } catch (ParseException e) {
    } catch (TokenMgrError e) {
    }
    return Collections.emptySet();
  }

  /**
   * If the supplied expression is a valid expression, compute its value using
   * the specified map to substitute values in place of cell-locations. If for
   * <i>any</i> reason, the expression cannot be computed e.g. it does conform
   * to the grammar or it references a cell location not contained in map, the
   * Value returned will be a StringValue corresponding to the input expression.
   * 
   * @param expression expression to be computed.
   * @param map a map from each cell location in the specified expression to
   *          numerical values.
   * @return the result of the computation.
   */
  public static Value computeValue(String expression,
      Map<CellLocation, Double> map) {

    Value value;
    try {
      Map<String, Double> values = new HashMap<String, Double>();

      for (Map.Entry<CellLocation, Double> entry : map.entrySet()) {
        values.put(entry.getKey().toString(), entry.getValue());
      }

      return new DoubleValue(
          new Parser(new StringReader(expression)).compute(values));
    } catch (TokenMgrError e) {
      value = new StringValue(expression);
    } catch (ParseException e) {
      value = new StringValue(expression);
    } catch (MissingValueException e) {
      value = new StringValue(expression);
    }

    return value;
  }
}
