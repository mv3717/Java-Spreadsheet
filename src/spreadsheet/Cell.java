package spreadsheet;

import common.api.CellLocation;
import common.api.monitor.Tracker;
import common.api.value.InvalidValue;
import common.api.value.Value;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Cell implements Tracker<Cell> {

  private CellLocation location;

  public CellLocation getLocation() {
    return location;
  }

  private Spreadsheet spreadsheet;
  private Value value;
  private Set<Tracker<Cell>> setOfReferences;
  private LinkedHashSet<Cell> cellsNeeded;

  public LinkedHashSet<Cell> getCellsNeeded() {
    return cellsNeeded;
  }

  private String expression;

  Cell(CellLocation location, Spreadsheet spreadsheet) {
    this.location = location;
    this.spreadsheet = spreadsheet;
    this.value = null;
    this.expression = null;
    setOfReferences = new HashSet<Tracker<Cell>>();
    cellsNeeded = new LinkedHashSet<Cell>();
  }

  String getExpression() {
    return expression;
  }

  void setExpression(String expression) {
    this.expression = expression;
  }

  public Set<Tracker<Cell>> getSetOfReferences() {
    return setOfReferences;
  }

  public void addToSetOfReferences(
      Cell cell) {
    this.setOfReferences.add(cell);
  }


  public void addToNeededCells(
      Cell cell) {
    this.cellsNeeded.add(cell);
  }


  void removeTracker() {
    setOfReferences.clear();
    cellsNeeded.clear();
  }

  Value getValue() {
    return value;
  }

  void setValue(Value value) {
    this.value = value;
  }

  @Override
  public void update(Cell changed) {
    if (!spreadsheet.getRecomputedCells().contains(this)) {
      spreadsheet.addToRecomputedCells(this);
      this.setValue(new InvalidValue(expression));
      setOfReferences.forEach(reference -> reference.update(this));
    }
  }
}
