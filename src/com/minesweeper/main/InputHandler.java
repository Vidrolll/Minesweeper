package com.minesweeper.main;

import java.awt.event.*;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
    Main main;

    public InputHandler(Main main) {
        this.main = main;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        main.input(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        main.input(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        main.input(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        main.input(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        main.input(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        main.input(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        main.input(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        main.input(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        main.input(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        main.input(e);
    }
}
