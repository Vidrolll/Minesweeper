package com.minesweeper.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Board {
    int[] board;
    int boardSize;
    boolean lose;

    BufferedImage flag;

    Color[] colors = {
            new Color(0, 0, 255),
            new Color(0, 127, 0),
            new Color(255, 0, 0),
            new Color(0, 0, 127),
            new Color(127, 0, 0),
            new Color(0, 127, 127),
            new Color(0, 0, 0),
            new Color(127, 127, 127),
    };

    public Board(int size, int percentage) {
        boardSize = size;
        board = new int[boardSize*boardSize];
        try {
            flag = ImageIO.read(Objects.requireNonNull(Board.class.getResourceAsStream("/flag.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setupBombs(percentage);
    }

    public void setupBombs(int percentage) {
        int bombs = (int)((float)board.length*((float)percentage/100.0));
        for(int i = 0; i < bombs; i++) {
            int x,y;
            do {
                x = (int)(Math.random()*boardSize);
                y = (int)(Math.random()*boardSize);
            } while(board[y*boardSize+x]==1);
            board[y*boardSize+x]=1;
        }
    }

    public int adjacentBombs(int x, int y) {
        int bombs = 0;
        for(int xTile = -1; xTile < 2; xTile++) {
            for(int yTile = -1; yTile < 2; yTile++) {
                if(y+yTile >= boardSize || y+yTile < 0) continue;
                if(x+xTile >= boardSize || x+xTile < 0) continue;
                if(board[(y+yTile)*boardSize+(x+xTile)]==1||
                        board[(y+yTile)*boardSize+(x+xTile)]==4)
                    bombs++;
            }
        }
        return bombs;
    }

    public void revealTile(int x, int y) {
        board[y*boardSize+x] = 2;
        if(adjacentBombs(x,y) == 0) {
            for(int xTile = -1; xTile < 2; xTile++) {
                for(int yTile = -1; yTile < 2; yTile++) {
                    if(y+yTile >= boardSize || y+yTile < 0) continue;
                    if(x+xTile >= boardSize || x+xTile < 0) continue;
                    if(board[(y+yTile)*boardSize+(x+xTile)]==0) {
                        revealTile(x + xTile, y + yTile);
                    }
                }
            }
        }
    }

    public int textOffset(Graphics2D g) {
        int textWidth = g.getFontMetrics().stringWidth("1");
        return ((800/boardSize)-textWidth)/2;
    }
    public int textOffsetHeight(Graphics2D g) {
        int textHeight = g.getFontMetrics().getHeight();
        return ((800/boardSize)-textHeight)/2;
    }
    public void render(Graphics2D g) {
        int tileSize = 800/boardSize;
        for(int i = 0; i < board.length; i++) {
            int x = i%boardSize;
            int y = i/boardSize;
            g.setFont(new Font("SansSerif", Font.PLAIN, tileSize));
            int xBox = mouseX/(800/boardSize);
            int yBox = mouseY/(800/boardSize);
            if((x+y)%2==0) {
                g.setColor(new Color(64,64,64));
                if(x==xBox&&y==yBox)
                    g.setColor(new Color(64,64,255));
                g.fillRect(x*tileSize,y*tileSize,tileSize,tileSize);
            } else {
                g.setColor(new Color(32,32,32));
                if(x==xBox&&y==yBox)
                    g.setColor(new Color(32,32,192));
                g.fillRect(x*tileSize,y*tileSize,tileSize,tileSize);
            }
            if(board[i]==2) {
                if((x+y)%2==0) {
                    g.setColor(new Color(255,255,255));
                    g.fillRect(x*tileSize,y*tileSize,tileSize,tileSize);
                } else {
                    g.setColor(new Color(192,192,192));
                    g.fillRect(x*tileSize,y*tileSize,tileSize,tileSize);
                }
                if(adjacentBombs(x,y) != 0) {
                    g.setColor(colors[adjacentBombs(x,y)-1]);
                    g.drawString(""+adjacentBombs(x,y),x*tileSize+textOffset(g),(y+1)*tileSize+(textOffsetHeight(g)));
                }
            }
            if(board[i]==1&&lose) {
                g.setColor(new Color(255,0,0));
                g.fillRect(x*tileSize,y*tileSize,tileSize,tileSize);
            }
            if(board[i]==3||board[i]==4) {
                g.setColor(new Color(0,255,0));
//                g.fillRect(x*tileSize,y*tileSize,tileSize,tileSize);
                g.drawImage(flag,x*tileSize,y*tileSize,tileSize,tileSize,null);
            }
        }
    }

    boolean firstClick = true;
    static int mouseX,mouseY;
    public void input(KeyEvent e) {
        if(lose) return;
        if(e.getID() != KeyEvent.KEY_PRESSED) return;
        if(e.getKeyCode() == KeyEvent.VK_Q) {
            int xBox = mouseX/(800/boardSize);
            int yBox = mouseY/(800/boardSize);
            if(board[yBox*boardSize+xBox]==0)
                board[yBox*boardSize+xBox]=3;
            else if(board[yBox*boardSize+xBox]==1)
                board[yBox*boardSize+xBox]=4;
            else if(board[yBox*boardSize+xBox]==3)
                board[yBox*boardSize+xBox]=0;
            else if(board[yBox*boardSize+xBox]==4)
                board[yBox*boardSize+xBox]=1;
        }
        if(e.getKeyCode() == KeyEvent.VK_E) {
            int xBox = mouseX/(800/boardSize);
            int yBox = mouseY/(800/boardSize);
            if(board[yBox*boardSize+xBox]==3||board[yBox*boardSize+xBox]==4)
                return;
            if(board[yBox*boardSize+xBox]==1) {
                if(firstClick) {
                    board[yBox*boardSize+xBox] = 0;
                    int xNew, yNew;
                    do {
                        xNew = (int)(Math.random()*boardSize);
                        yNew = (int)(Math.random()*boardSize);
                    } while (board[yNew*boardSize+xNew]==1||xNew==xBox||yNew==yBox);
                    board[yNew*boardSize+xNew] = 1;
                } else {
                    lose = true;
                    return;
                }
            }
            if(firstClick)
                if(adjacentBombs(xBox,yBox)!=0) shuffleAdjacentBombs(xBox,yBox);
            revealTile(xBox,yBox);
            firstClick = false;
        }
    }
    public void input(MouseEvent e) {
        if(lose) return;
        int x = e.getX();
        int y = e.getY();
        Board.mouseX = x;
        Board.mouseY = y;
        if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == 3) {
            int xBox = x/(800/boardSize);
            int yBox = y/(800/boardSize);
            if(board[yBox*boardSize+xBox]==0)
                board[yBox*boardSize+xBox]=3;
            else if(board[yBox*boardSize+xBox]==1)
                board[yBox*boardSize+xBox]=4;
            else if(board[yBox*boardSize+xBox]==3)
                board[yBox*boardSize+xBox]=0;
            else if(board[yBox*boardSize+xBox]==4)
                board[yBox*boardSize+xBox]=1;
        }
        if(e.getID() == MouseEvent.MOUSE_CLICKED && e.getButton() == 1) {
            int xBox = x/(800/boardSize);
            int yBox = y/(800/boardSize);
            if(board[yBox*boardSize+xBox]==3||board[yBox*boardSize+xBox]==4)
                return;
            if(board[yBox*boardSize+xBox]==1) {
                if(firstClick) {
                    board[yBox*boardSize+xBox] = 0;
                    int xNew, yNew;
                    do {
                        xNew = (int)(Math.random()*boardSize);
                        yNew = (int)(Math.random()*boardSize);
                    } while (board[yNew*boardSize+xNew]==1||xNew==xBox||yNew==yBox);
                    board[yNew*boardSize+xNew] = 1;
                } else {
                    lose = true;
                    return;
                }
            }
            if(firstClick)
                if(adjacentBombs(xBox,yBox)!=0) shuffleAdjacentBombs(xBox,yBox);
            revealTile(xBox,yBox);
            firstClick = false;
        }
    }
    public void shuffleAdjacentBombs(int x, int y) {
        do {
            for(int xTile = -1; xTile < 2; xTile++) {
                for(int yTile = -1; yTile < 2; yTile++) {
                    if(y+yTile >= boardSize || y+yTile < 0) continue;
                    if(x+xTile >= boardSize || x+xTile < 0) continue;
                    if(board[(y+yTile)*boardSize+(x+xTile)]==1||
                            board[(y+yTile)*boardSize+(x+xTile)]==4) {
                        int xNew, yNew;
                        board[(y+yTile)*boardSize+(x+xTile)] = 0;
                        do {
                            xNew = (int)(Math.random()*boardSize);
                            yNew = (int)(Math.random()*boardSize);
                        } while (board[yNew*boardSize+xNew]==1||xNew==x||yNew==y);
                        board[yNew*boardSize+xNew] = 1;
                    }
                }
            }
        } while(adjacentBombs(x,y)!=0);
    }
}
