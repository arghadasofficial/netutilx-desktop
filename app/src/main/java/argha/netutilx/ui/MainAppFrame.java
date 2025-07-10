/*
 * Copyright (C) 2025 argha
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package argha.netutilx.ui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author argha
 */
public class MainAppFrame extends JFrame {

    public MainAppFrame() {
        setTitle("Netutilx DNS Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        // Create the main panel and add it to the frame
        DnsToolPanel dnsPanel = new DnsToolPanel();
        add(dnsPanel, BorderLayout.CENTER);
    }
}
