package spreadsheet;

import common.gui.SpreadsheetGUI;

public class Main {

  private static final int DEFAULT_NUM_ROWS = 5000;
  private static final int DEFAULT_NUM_COLUMNS = 5000;

  public static void main(String[] args) {

    Spreadsheet tabular = new Spreadsheet();
    SpreadsheetGUI spreadsheet = new SpreadsheetGUI(tabular, DEFAULT_NUM_ROWS,
        DEFAULT_NUM_COLUMNS);
    spreadsheet.start();

  }

}
