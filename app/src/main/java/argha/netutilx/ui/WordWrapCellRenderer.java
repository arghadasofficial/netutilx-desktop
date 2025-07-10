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
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 *
 * @author argha
 */
public class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {

    public WordWrapCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
        setMargin(new Insets(5, 5, 5, 5)); // Add some padding
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        setText((value == null) ? "" : value.toString());
        // Set colors based on selection
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        // Dynamically adjust the row height to fit the wrapped text
        int fontHeight = getFontMetrics(getFont()).getHeight();
        int textLength = getText().length();
        int lines = (textLength == 0) ? 1 : (int) Math.ceil((double) textLength / table.getColumnModel().getColumn(column).getWidth() * fontHeight * 0.75); // Heuristic
        int newHeight = fontHeight * lines + getInsets().top + getInsets().bottom;

        if (table.getRowHeight(row) != newHeight) {
            table.setRowHeight(row, newHeight);
        }

        return this;
    }
}
