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

import argha.netutilx.model.DnsRecord;
import argha.netutilx.model.DnsServer;
import argha.netutilx.model.DnsType;
import argha.netutilx.service.DnsApiService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author argha
 */
public class DnsToolPanel extends JPanel {

    private final JTextField queryField;
    private final JButton fetchButton;
    private final JTable resultTable;
    private final DefaultTableModel tableModel;
    private final DnsApiService apiService;
    private final ButtonGroup serverButtonGroup = new ButtonGroup();
    private final ButtonGroup typeButtonGroup = new ButtonGroup();
    private final JPanel serverPanel = new JPanel();
    private final JPanel typePanel = new JPanel();

    public DnsToolPanel() {
        super(new BorderLayout(15, 15));
        this.apiService = new DnsApiService();

        // --- Create Components ---
        queryField = new JTextField();
        fetchButton = new JButton("Fetch DNS Info");
        fetchButton.setIcon(new FlatSVGIcon("actions/search.svg")); // Modern icon

        tableModel = new DefaultTableModel(new String[]{"Name", "TTL", "Type", "Data"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);

        setupModernUI();
        buildLayout();
        addEventListeners();
        loadInitialData();
    }

    private void setupModernUI() {
        // General panel styling
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Table styling
        resultTable.setFillsViewportHeight(true);
        resultTable.setRowHeight(30);
        resultTable.setIntercellSpacing(new Dimension(0, 1));
        resultTable.getTableHeader().setFont(resultTable.getTableHeader().getFont().deriveFont(Font.BOLD));
        resultTable.getColumnModel().getColumn(3).setCellRenderer(new WordWrapCellRenderer()); // Data column
    }

    private void buildLayout() {
        // --- Top Input Panel ---
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.add(new JLabel("Enter Domain or IP Address:"), BorderLayout.NORTH);
        inputPanel.add(queryField, BorderLayout.CENTER);
        inputPanel.add(fetchButton, BorderLayout.EAST);

        // --- Center Selection Panels ---
        serverPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        typePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JPanel selectionContainer = new JPanel();
        selectionContainer.setLayout(new BoxLayout(selectionContainer, BoxLayout.Y_AXIS));
        selectionContainer.add(createTitledPanel("DNS Server", serverPanel));
        selectionContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        selectionContainer.add(createTitledPanel("Record Type", typePanel));

        // --- Bottom Results Panel ---
        JPanel resultsPanel = new JPanel(new BorderLayout(10, 10));
        JButton copyAllButton = new JButton("Copy All Results");
        copyAllButton.setIcon(new FlatSVGIcon("icons/copy.svg"));
        copyAllButton.addActionListener(e -> copyTableData());

        JPanel resultsHeader = new JPanel(new BorderLayout());
        resultsHeader.add(new JLabel("Results"), BorderLayout.WEST);
        resultsHeader.add(copyAllButton, BorderLayout.EAST);

        resultsPanel.add(resultsHeader, BorderLayout.NORTH);
        resultsPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        // --- Add everything to the main panel ---
        add(inputPanel, BorderLayout.NORTH);
        add(selectionContainer, BorderLayout.CENTER);
        add(resultsPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitledPanel(String title, JPanel contentPanel) {
        JPanel titledPanel = new JPanel(new BorderLayout());
        titledPanel.setBorder(BorderFactory.createTitledBorder(title));
        titledPanel.add(contentPanel, BorderLayout.CENTER);
        return titledPanel;
    }

    private void addEventListeners() {
        fetchButton.addActionListener(e -> performDnsLookup());

        // Right-click context menu for the table
        resultTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = resultTable.rowAtPoint(e.getPoint());
                    int col = resultTable.columnAtPoint(e.getPoint());
                    if (row >= 0 && col >= 0) {
                        resultTable.setRowSelectionInterval(row, row);
                        resultTable.setColumnSelectionInterval(col, col);
                        createContextMenu(row, col).show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private JPopupMenu createContextMenu(int row, int col) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem copyCellItem = new JMenuItem("Copy Cell");
        copyCellItem.setIcon(new FlatSVGIcon("actions/copy.svg"));
        copyCellItem.addActionListener(e -> copyToClipboard(resultTable.getValueAt(row, col).toString()));

        JMenuItem copyRowItem = new JMenuItem("Copy Row");
        copyRowItem.setIcon(new FlatSVGIcon("actions/copy.svg"));
        copyRowItem.addActionListener(e -> {
            String rowData = IntStream.range(0, resultTable.getColumnCount())
                    .mapToObj(c -> resultTable.getValueAt(row, c).toString())
                    .collect(Collectors.joining("\t")); // Tab-separated for easy pasting into spreadsheets
            copyToClipboard(rowData);
        });

        JMenuItem copyColItem = new JMenuItem("Copy '" + resultTable.getColumnName(col) + "' Column");
        copyColItem.setIcon(new FlatSVGIcon("actions/copy.svg"));
        copyColItem.addActionListener(e -> {
            String colData = IntStream.range(0, resultTable.getRowCount())
                    .mapToObj(r -> resultTable.getValueAt(r, col).toString())
                    .collect(Collectors.joining("\n"));
            copyToClipboard(colData);
        });

        menu.add(copyCellItem);
        menu.add(copyRowItem);
        menu.add(copyColItem);
        return menu;
    }

    private void loadInitialData() {
        new SwingWorker<Void, Void>() {
            private List<DnsServer> servers;
            private List<DnsType> types;

            @Override
            protected Void doInBackground() throws Exception {
                servers = apiService.getDnsServers();
                types = apiService.getDnsTypes();
                return null;
            }

            @Override
            protected void done() {
                try {
                    // Create radio buttons for servers
                    for (DnsServer server : servers) {
                        JRadioButton rb = new JRadioButton(server.getName());
                        rb.setActionCommand(server.getId());
                        serverButtonGroup.add(rb);
                        serverPanel.add(rb);
                        if (servers.indexOf(server) == 0) {
                            rb.setSelected(true); // Select first one by default
                        }
                    }
                    // Create radio buttons for types
                    for (DnsType type : types) {
                        JRadioButton rb = new JRadioButton(type.getName());
                        rb.setActionCommand(type.getId());
                        typeButtonGroup.add(rb);
                        typePanel.add(rb);
                        if (types.indexOf(type) == 0) {
                            rb.setSelected(true); // Select first one by default
                        }
                    }
                } catch (Exception e) {
                    showError("Failed to load initial data: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void performDnsLookup() {
        String query = queryField.getText().trim();
        ButtonModel serverModel = serverButtonGroup.getSelection();
        ButtonModel typeModel = typeButtonGroup.getSelection();

        if (query.isEmpty() || serverModel == null || typeModel == null) {
            JOptionPane.showMessageDialog(this, "Please enter a domain/IP and select a server/type.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        setBusy(true);

        new SwingWorker<List<DnsRecord>, Void>() {
            @Override
            protected List<DnsRecord> doInBackground() throws Exception {
                return apiService.getDnsInfo(query, serverModel.getActionCommand(), typeModel.getActionCommand());
            }

            @Override
            protected void done() {
                try {
                    List<DnsRecord> records = get();
                    for (DnsRecord record : records) {
                        tableModel.addRow(new Object[]{record.getName(), record.getTtl(), record.getType(), record.getData()});
                    }
                } catch (Exception e) {
                    showError("Failed to fetch DNS info: " + e.getMessage());
                } finally {
                    setBusy(false);
                }
            }
        }.execute();
    }

    private void copyTableData() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < resultTable.getRowCount(); r++) {
            for (int c = 0; c < resultTable.getColumnCount(); c++) {
                sb.append(resultTable.getValueAt(r, c).toString()).append("\t");
            }
            sb.append("\n");
        }
        copyToClipboard(sb.toString());
        JOptionPane.showMessageDialog(this, "Results copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    private void setBusy(boolean busy) {
        fetchButton.setEnabled(!busy);
        setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "API Error", JOptionPane.ERROR_MESSAGE);
    }
}
