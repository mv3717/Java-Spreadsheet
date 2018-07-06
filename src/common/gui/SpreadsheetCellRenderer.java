package common.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import common.api.CellLocation;
import common.api.Tabular;
import common.api.value.Value;
import common.api.value.ValueEvaluator;

public class SpreadsheetCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 1L;

  private static Border focusCellHighlightBorder =
      UIManager.getBorder("Table.focusCellHighlightBorder");

  private static Color focusCellForeground =
      UIManager.getColor("Table.focusCellForeground");

  private static Color focusCellBackground =
      UIManager.getColor("Table.focusCellBackground");

  private static Color headerBackground =
      UIManager.getColor("TableHeader.background");

  private static Color tableBackground = UIManager.getColor("Table.background");

  private Tabular spreadsheet;

  protected SpreadsheetCellRenderer(Tabular spreadsheet, Font font) {
    super();
    this.spreadsheet = spreadsheet;
    this.setFont(font);

    if (focusCellHighlightBorder == null) {
      focusCellHighlightBorder = new LineBorder(Color.GRAY, 2);
    }

    if (focusCellForeground == null) {
      focusCellForeground = Color.WHITE;
    }

    if (focusCellBackground == null) {
      focusCellBackground = Color.WHITE;
    }

    if (headerBackground == null) {
      headerBackground = new Color(238, 238, 238);
    }

    if (tableBackground == null) {
      tableBackground = Color.WHITE;
    }

  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {

    if (column == 0) {
      value = String.valueOf((row + 1));
      setBorder(noFocusBorder);
      setBackground(headerBackground);
      setHorizontalAlignment(JTextField.CENTER);
    } else {
      String location = SpreadsheetTableModel.convertColumn(column) + (row + 1);
      CellLocation reference = new CellLocation(location);

      Value v = spreadsheet.getValue(reference);
      value = v;
      if (v != null) {
        v.evaluate(new ValueEvaluator() {
          @Override
          public void evaluateString(String expression) {
            setHorizontalAlignment(JTextField.LEFT);
          }

          @Override
          public void evaluateLoop() {
            setHorizontalAlignment(JTextField.CENTER);
          }

          @Override
          public void evaluateInvalid(String expression) {
            setHorizontalAlignment(JTextField.LEFT);
          }

          @Override
          public void evaluateDouble(double value) {
            setHorizontalAlignment(JTextField.RIGHT);
          }
        });
      }

      if (hasFocus) {
        setBorder(focusCellHighlightBorder);
        setForeground(focusCellForeground);
        setBackground(focusCellBackground);
      } else {
        setBorder(noFocusBorder);
        setBackground(tableBackground);
      }
    }

    setValue(value);
    return this;
  }

  @Override
  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
  }
}
