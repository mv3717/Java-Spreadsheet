package common.gui;

import java.awt.*;

import javax.swing.*;

import common.api.CellLocation;
import common.api.Tabular;

public class SpreadsheetCellEditor extends DefaultCellEditor {

  private static final long serialVersionUID = 1L;

  private JTextField textField;
  private Tabular spreadsheet;

  protected SpreadsheetCellEditor(JTextField textField, Tabular spreadsheet) {
    super(textField);
    this.textField = textField;
    this.spreadsheet = spreadsheet;
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {

    String location = SpreadsheetTableModel.convertColumn(column) + (row + 1);

    CellLocation reference = new CellLocation(location);

    textField.setText(spreadsheet.getExpression(reference));

    textField.selectAll();
    textField.setSelectionStart(0);
    textField.setSelectionEnd(textField.getText().length());
    textField.setCaretPosition(textField.getText().length());

    return textField;
  }

}