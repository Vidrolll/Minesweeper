package com.minesweeper.main;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    public MainWindow(Main main) {
        JFrame frame = new JFrame("Mine Sweeper");
        frame.setSize(new Dimension(800,800));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.add(main);
        frame.setVisible(true);
        main.start();
    }
}
