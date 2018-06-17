package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchWindow extends JFrame {

    public LaunchWindow(String title) {
        super(title);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        Container c = getContentPane();

        //market orders button
        JButton marketButton = new JButton("market orders");
        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MarketWindow marketWindow = new MarketWindow("");
                marketWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //set X close
                marketWindow.setSize(150, 400); //set dimensions
                marketWindow.setLocationRelativeTo(null); //null makes it open in the center
                marketWindow.setVisible(true); //show window
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        c.add(marketButton, gbc);

        //orderbook button
        JButton orderbookButton = new JButton("limit orders");
        gbc.gridx = 0;
        gbc.gridy = 1;
        c.add(orderbookButton, gbc);

        //liqs button
        JButton liqsButton = new JButton("liquidations");
        gbc.gridx = 0;
        gbc.gridy = 2;
        c.add(liqsButton, gbc);





    }
}
