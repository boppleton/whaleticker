import gui.LaunchWindow;
import org.pushingpixels.substance.api.skin.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        startLaunchWindow();

    }

    private static void startLaunchWindow() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceMistSilverLookAndFeel());
                } catch (Exception e) {
                    System.out.println("Substance Graphite failed to initialize");
                }

                LaunchWindow launchWindow = new LaunchWindow("whaleticker");
                launchWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set X close
                launchWindow.setSize(220, 170); //set dimensions
                launchWindow.setLocationRelativeTo(null); //null makes it open in the center
                launchWindow.setVisible(true); //show window

            }});
    }
}
