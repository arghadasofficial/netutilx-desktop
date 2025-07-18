/*
 * This source file was generated by the Gradle 'init' task
 */
package argha.netutilx;

import argha.netutilx.ui.MainAppFrame;
import com.formdev.flatlaf.FlatIntelliJLaf; // A clean, light theme
import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
       try {
            FlatIntelliJLaf.setup(); // Or try FlatDarculaLaf for a dark theme
        } catch (Exception e) {
            System.err.println("Failed to initialize LaF");
        }
        SwingUtilities.invokeLater(() -> {
            new MainAppFrame().setVisible(true);
        });
    }
}
