package com.minesweeper.main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Main extends Canvas implements Runnable {
    Thread thread;
    boolean running;

    Board board;

    int size = 40;

    public Main() {
        new MainWindow(this);

        this.addKeyListener(new InputHandler(this));
        this.addMouseListener(new InputHandler(this));
        this.addMouseMotionListener(new InputHandler(this));

        board = new Board(size,69);
    }

    @Override
    public void run() {
        this.requestFocus();
        double ticks = 60;
        double ns = 1000000000/ticks;
        double delta = 0;
        long lastTime = System.nanoTime();
        while(running) {
            long now = System.nanoTime();
            delta += (now-lastTime)/ns;
            lastTime = now;
            while(delta>=1) {
                update();
                render();
                delta--;
            }
        }
        stop();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {

    }
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(2);
            return;
        }
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();

        if(board!=null)
            board.render(g);

        g.dispose();
        bs.show();
    }

    public void input(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            board = new Board(size,20);
        }
        if(board != null)
            board.input(e);
    }
    public void input(MouseEvent e) {
        if(board != null)
            board.input(e);
    }

    public static void main(String[] args) {
        new Main();
    }
}
