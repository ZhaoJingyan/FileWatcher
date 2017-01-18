package com.watcher.ui;

import com.watcher.FileInformationColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * File Watcher Swing UI.
 * Created by Zhao Jinyan on 2017/1/6.
 */
public class FileWatcherFrame extends JFrame implements com.watcher.ControlCenterAdapter {

    private DefaultTableModel tableModel;

    private JScrollPane scrollPane;

    private JButton runButton;

    private JButton closeButton;

    public static void main(String [] args){

        FileWatcherFrame frame = new FileWatcherFrame();
        frame.setVisible(true);

    }

    private FileWatcherFrame(){

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setTitle("File Watcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize(1000, 400);

        String [] colNames = {"ID", "NAME", "DATE", "TYPE", "INFO"};
        tableModel = new DefaultTableModel(null, colNames);
        JTable table = new JTable(tableModel);
        table.setEnabled(false);


        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        DefaultTableCellRenderer firstTableCellRender = new DefaultTableCellRenderer();
        firstTableCellRender.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(firstTableCellRender);

        table.getColumnModel().getColumn(1).setMinWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setMaxWidth(200);

        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setMaxWidth(100);

        table.getColumnModel().getColumn(3).setMinWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setMaxWidth(100);

        table.getColumnModel().getColumn(4).setMinWidth(500);
        table.getColumnModel().getColumn(4).setPreferredWidth(500);
        table.getColumnModel().getColumn(4).setMaxWidth(500);

        scrollPane = new JScrollPane(table);
        runButton = new JButton("Run");
        closeButton = new JButton("Close");

        initLayout();

    }

    private void initLayout(){

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        // 水平
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGap(5).addGroup(
                        layout.createParallelGroup().addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1000, GroupLayout.DEFAULT_SIZE)
                                .addGroup(GroupLayout.Alignment.TRAILING,
                                        layout.createSequentialGroup().addComponent(runButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                                .addGap(5)
                                                .addComponent(closeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                )
                ).addGap(5)
        ));
        layout.setHorizontalGroup(hGroup);

        // 垂直
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGap(5).addGroup(
                        layout.createParallelGroup().addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                ).addGap(5).addGroup(
                        layout.createParallelGroup().addComponent(runButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                .addComponent(closeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                ).addGap(5)
        ));

        layout.setVerticalGroup(vGroup);

        pack();
    }

    @Override
    public void setTableColumn(List<FileInformationColumn> columns){
        for(FileInformationColumn column : columns) {
            tableModel.addRow(column.getVector());
        }

    }

    @Override
    public void isInitialized() {
        SwingUtilities.invokeLater(() -> setVisible(true) );
    }
}
