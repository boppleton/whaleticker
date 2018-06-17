package gui;

import gui.market.MarketWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchWindow extends JFrame {

    private GridBagConstraints gbc;
    private Container c;

    public LaunchWindow(String title) {
        super(title);

        setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        c = getContentPane();

        setupMarketButton();

        setupLiqsButton();

        setupOrderbookButton();


    }

    private void setupLiqsButton() {

        //liqs button
        JButton liqsButton = new JButton("liquidations");
        gbc.gridx = 0;
        gbc.gridy = 2;
        c.add(liqsButton, gbc);

    }

    private void setupOrderbookButton() {

        //orderbook button
        JButton orderbookButton = new JButton("limit orders");
        gbc.gridx = 0;
        gbc.gridy = 1;
        c.add(orderbookButton, gbc);

    }

    private void setupMarketButton() {

        //market orders button
        JButton marketButton = new JButton("market orders");
        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MarketWindow marketWindow = new MarketWindow("");
                marketWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //set X close
                marketWindow.setSize(100, 400); //set dimensions
                marketWindow.setLocationRelativeTo(null); //null makes it open in the center
                marketWindow.setVisible(true); //show window
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        c.add(marketButton, gbc);

    }
}
