package com.watcher.ui;

import javax.swing.*;
import java.awt.*;

/**
 * File Watcher Swing UI.
 * Created by Zhao Jinyan on 2017/1/6.
 */
public class FileWatcherFrame extends JFrame {


    public static void main(String [] args){

        FileWatcherFrame frame = new FileWatcherFrame();
        frame.setVisible(true);

    }

    private FileWatcherFrame(){
        setTitle("File Watcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);


        Object [][] cellObjects = {{"test1", "test2"}, {"test3", "test4"}};
        String [] colNames = {"Col1", "Col2"};
        JTable table = new JTable(cellObjects, colNames);
        //table.setSize(1000, 400);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton runButton = new JButton("Run");
        JButton closeButton = new JButton("Close");

        // 水平
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGap(5).addGroup(
                        layout.createParallelGroup().addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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


}
