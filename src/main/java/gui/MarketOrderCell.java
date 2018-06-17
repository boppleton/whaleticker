package gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MarketOrderCell extends AbstractCellEditor implements TableCellRenderer {

    JPanel panel;
    JLabel text;


    MarketOrder order;

    public MarketOrderCell() {

        text = new JLabel();


        panel = new JPanel(new BorderLayout());

        ImageIcon icon = new ImageIcon(getClass().getResource("/bitmex22.png"));
        text.setIcon(icon);

        panel.add(text, BorderLayout.PAGE_START);

    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        MarketOrder order = (MarketOrder)value;
        updateData(order, isSelected, table);
        return panel;
    }


    private void updateData(MarketOrder order, boolean isSelected, JTable table) {
        this.order = order;

        text.setText(coolFormat((double) order.amt, 0));

        panel.setBackground(getColor(order.amt));

        int orderAmt = Math.abs(order.amt);

        if (orderAmt < 1000) {
            panel.setBorder(null);
            text.setForeground(Color.BLACK);
            text.setFont(new Font(null, Font.ITALIC, 12));

        } else if (orderAmt < 100000) {
            panel.setBorder(null);
            text.setForeground(Color.BLACK);
            text.setFont(new Font(null, Font.PLAIN, 15));

        }else if (orderAmt < 500000) {
            panel.setBorder(null);
            text.setForeground(Color.BLACK);
            text.setFont(new Font(null, Font.PLAIN, 15));

        }
        else if (orderAmt < 1000000) {
            panel.setBorder(null);
            text.setForeground(Color.WHITE);
            text.setFont(new Font(null, Font.CENTER_BASELINE, 17));

        } else { //over 1mil
            panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            text.setForeground(Color.YELLOW);
            text.setFont(new Font(null, Font.BOLD, 17));
        }

//        if (isSelected) {
//            panel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2, true));
//        } else {
//            panel.setBorder(null);
//        }
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    private static Color getColor(int amt) {

        int intensity = (int) Math.abs(amt) / 2000;

        if (intensity > 165) {
            intensity = 165;
        } else if (intensity < 1) {
            intensity = 1;
        }


        if (amt > 0) {
            return new Color(170 - intensity, 255 - intensity / 2, 170 - intensity);
        } else {
            return new Color(255 - intensity / 2, 170 - intensity, 170 - intensity);
        }

    }

    //usd text format for k and mil.  from S.O. i did not make this, must have been some sort of high wizard
    private static String coolFormat(double n, int iteration) {

        String[] c = new String[]{"k", "mil"};

        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000 ? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99) ? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration])
                : coolFormat(d, iteration + 1));
    }

}
