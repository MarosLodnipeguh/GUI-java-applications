package Graphics;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.EventObject;

class Table extends JTable {

    private int rowCount;
    private int columnCount;

    public Table(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        setModel(new DefaultTableModel(rowCount, columnCount));

        setCellSelectionEnabled(false);

        setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        setCellEditor(new NonEditableTableCellEditor());

        setRowHeight(20);
        for (int i = 0; i < columnCount; i++) {
            getColumnModel().getColumn(i).setPreferredWidth(20);
            getColumnModel().getColumn(i).setMinWidth(20);
            getColumnModel().getColumn(i).setMaxWidth(20);
        }

        setAutoResizeMode(AUTO_RESIZE_OFF);
        setPreferredScrollableViewportSize(new Dimension(500, 500));
        getTableHeader().setVisible(false);

        setGridColor(Color.GRAY);

        this.setValueAt(Color.GREEN, 0, 0);

        getCellEditor().stopCellEditing();
    }

    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component component = super.getTableCellRendererComponent(table, value, false, false, row, column);

            if (value == null) {
                setBackground(Color.WHITE);
                setText(""); // Ukryj tekst w komórce
            } else {
                setBackground((Color) value);
                setText(""); // Ukryj tekst w komórce
            }

            return component;
        }
    }

    class NonEditableTableCellEditor extends DefaultCellEditor {
        public NonEditableTableCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return false;
        }
    }



}
