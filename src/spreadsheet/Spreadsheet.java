package spreadsheet;

import common.api.CellLocation;
import common.api.ExpressionUtils;
import common.api.Tabular;
import common.api.value.InvalidValue;
import common.api.value.LoopValue;
import common.api.value.StringValue;
import common.api.value.Value;
import common.api.value.ValueEvaluator;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class Spreadsheet implements Tabular {

  private Map<CellLocation, Cell> values = new HashMap();
  private ArrayDeque<Cell> recomputedCells = new ArrayDeque<>();
  private Map<CellLocation, Double> results = new HashMap<>();


  public ArrayDeque<Cell> getRecomputedCells() {
    return recomputedCells;
  }

  @Override
  public void setExpression(CellLocation location, String expression) {
    if (values.containsKey(location)) {
      values.get(location).removeTracker();
      values.get(location).setExpression(expression);
      values.get(location).setValue(new InvalidValue(expression));
    } else {
      Cell newCell = new Cell(location, this);
      newCell.setExpression(expression);
      newCell.setValue(new InvalidValue(expression));
      values.put(location, newCell);
    }
    recomputedCells.add(values.get(location));
    ExpressionUtils.getReferencedLocations(values.get(location)
        .getExpression()).forEach(cell -> {
      values.putIfAbsent(cell, new Cell(cell, null));
      if (!(cell.equals(values.get(location).getLocation()))) {
        values.get(location).addToNeededCells(values.get(cell));
        values.get(cell).addToSetOfReferences(values.get(location));
      }
    });
    values.get(location).getSetOfReferences()
        .forEach(reference -> reference.update(values.get(location)));
  }

  void addToRecomputedCells(Cell cell) {
    recomputedCells.add(cell);
  }

  @Override
  public String getExpression(CellLocation location) {
    if (values.containsKey(location)) {
      return values.get(location).getExpression();
    } else {
      return null;
    }
  }

  @Override
  public Value getValue(CellLocation location) {
    if (values.containsKey(location)) {
      return values.get(location).getValue();
    } else {
      return null;
    }
  }

  @Override
  public void recompute() {
    for (int i = 0; i < recomputedCells.size(); i++) {
      if (recomputedCells.iterator().hasNext()) {
        recomputeCell(recomputedCells.iterator().next());
      }
    }
  }


  private void recomputeCell(Cell c) {

    LinkedHashSet<Cell> related = new LinkedHashSet<>();
    related.addAll(c.getCellsNeeded());
    checkLoops(c, related, new LinkedHashSet<>());

    if (!c.getValue().equals(LoopValue.INSTANCE)) {
      c.setValue(new StringValue(c.getExpression()));

      for (Cell cell : c.getCellsNeeded()) {
        if (recomputedCells.contains(cell)) {
          recomputedCells.addFirst(cell);
          if (cell.getCellsNeeded() != null) {
            recomputeCell(cell);
          }
        }
      }

      recomputedCells.addLast(c);
      calculateCellValue(c);
      recomputedCells.remove(c);
    }
  }

  private void calculateCellValue(Cell cell) {
    ValueEvaluator evaluator = new ValueEvaluator() {
      @Override
      public void evaluateDouble(double value) {
        results.put(cell.getLocation(), value);
      }

      @Override
      public void evaluateLoop() {

      }

      @Override
      public void evaluateString(String expression) {
      }

      @Override
      public void evaluateInvalid(String expression) {

      }
    };
    Value value = ExpressionUtils.computeValue(cell.getExpression(), results);
    cell.setValue(value);
    value.evaluate(evaluator);
  }

  private void checkLoops(Cell c, LinkedHashSet<Cell> children,
      LinkedHashSet<Cell> cellsSeen) {
    if (children.contains(c)) {
      markAsValidatedLoop(c, cellsSeen);
    } else {
      for (Cell cell : children) {
        cellsSeen.add(cell);
        checkLoops(c, cell.getCellsNeeded(), cellsSeen);
      }
    }
  }

  private void markAsValidatedLoop(Cell startCell, LinkedHashSet<Cell> cells) {
    startCell.setValue(LoopValue.INSTANCE);
    cells.forEach(cell -> {
      cell.setValue(LoopValue.INSTANCE);
      recomputedCells.remove(cell);
    });
  }
}
