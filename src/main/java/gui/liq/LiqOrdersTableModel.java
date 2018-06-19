package gui.liq;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class LiqOrdersTableModel extends AbstractTableModel {
    private List marketOrders;

    LiqOrdersTableModel(List marketOrders) {
        this.marketOrders = marketOrders;
    }

    public Class getColumnClass(int columnIndex) { return LiqOrder.class; }
    public int getColumnCount() { return 1; }
    public String getColumnName(int columnIndex) { return ""; }
    public int getRowCount() { return (marketOrders == null) ? 0 : marketOrders.size(); }
    public Object getValueAt(int rowIndex, int columnIndex) { return (marketOrders == null) ? null : marketOrders.get(rowIndex); }
    public boolean isCellEditable(int columnIndex, int rowIndex) { return false; }
}

