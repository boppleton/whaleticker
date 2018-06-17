package gui.market;

import gui.market.MarketOrder;
import websocket.Formatter;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MarketOrderCell extends AbstractCellEditor implements TableCellRenderer {

    private JPanel panel;
    private JLabel text;
    private JLabel instrument;

    private MarketOrder order;

    MarketOrderCell() {

        text = new JLabel();
        instrument = new JLabel();
        instrument.setForeground(Color.GRAY);
        instrument.setFont(new Font(null, Font.ITALIC, 12));

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(text);
        panel.add(instrument);


    }


    private void updateData(MarketOrder order, boolean isSelected, JTable table) {
        this.order = order;

        text.setIcon(getIcon(order.exchange));

        setInstrument(order);

        text.setText(Formatter.kFormat((double) Math.abs(order.amt), 0));



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

        } else if (orderAmt < 500000) {
            panel.setBorder(null);
            text.setForeground(Color.BLACK);
            text.setFont(new Font(null, Font.PLAIN, 15));

        } else if (orderAmt < 1000000) {
            panel.setBorder(null);
            text.setForeground(Color.WHITE);
            text.setFont(new Font(null, Font.CENTER_BASELINE, 17));

        } else { //over 1mil
            panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            text.setForeground(Color.YELLOW);
            text.setFont(new Font(null, Font.BOLD, 17));
        }

    }

    private void setInstrument(MarketOrder order) {

        if (order.instrument.contains("bitmexJune")) {
            instrument.setText(" - june futures");
        } else if (order.instrument.contains("bitmexSept")) {
            instrument.setText(" - september futures");
        }else if (order.instrument.contains("okexThis")) {
            instrument.setText(" - this week futures");
        }else if (order.instrument.contains("okexNext")) {
            instrument.setText(" - next week futures");
        }else if (order.instrument.contains("okexQuat")) {
            instrument.setText(" - quarterly futures");
        } else {
            instrument.setText("");
        }


    }

    private Icon getIcon(String exchange) {

        ImageIcon icon = null;

        switch (exchange) {
            case "bitmex":
                icon = new ImageIcon(getClass().getResource("/bitmex22.png"));
                break;
            case "bitfinex":
                icon = new ImageIcon(getClass().getResource("/bitfinex22.png"));
                break;
            case "okex":
                icon = new ImageIcon(getClass().getResource("/okex22.png"));
                break;
            case "binance":
                icon = new ImageIcon(getClass().getResource("/binance.png"));
                break;
            case "gdax":
                icon = new ImageIcon(getClass().getResource("/gdax25.png"));
                break;
        }

        return icon;

    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        MarketOrder order = (MarketOrder) value;
        updateData(order, isSelected, table);
        return panel;
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


}
