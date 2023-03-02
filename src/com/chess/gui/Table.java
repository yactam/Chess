package com.chess.gui;

import com.chess.engine.board.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.chess.engine.board.BoardUtils.*;

public class Table {

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final Board chessBoard;

    public Table() {
        JFrame gameFrame = new JFrame("Chess");
        final JMenuBar tableMenuBar = new JMenuBar();
        populateMenuBar(tableMenuBar);
        gameFrame.setJMenuBar(tableMenuBar);

        chessBoard = Board.createStandardBoard();

        BoardPanel boardPanel = new BoardPanel();
        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(boardPanel, BorderLayout.CENTER);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setSize(OUTER_FRAME_DIMENSION);
        gameFrame.setVisible(true);
    }

    private void populateMenuBar(JMenuBar tableMenuBar) {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("open up that pgn file!");
            }
        });

        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        tableMenuBar.add(fileMenu);
    }

    private class BoardPanel extends JPanel {
        private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
        final ArrayList<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            boardTiles = new ArrayList<>();
            for(int i = 0; i < NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }

    private class TilePanel extends JPanel {
        private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
        private final int tileId;

        private final Color lightTileColor = Color.LIGHT_GRAY;
        private final Color darkTileColor = Color.BLACK;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            validate();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if(board.getTile(tileId).isTileOccupied()) {
                try {
                    String defaultPath = "art/simple/";
                    BufferedImage image = ImageIO.read(new File(defaultPath + board.getTile(tileId).getPiece().getAlliance().toString().charAt(0)
                    + board.getTile(tileId).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private void assignTileColor() {
            if(EIGHT_RANK[this.tileId] || SIXTH_RANK[tileId] || FOURTH_RANK[tileId] || SECOND_RANK[tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(SEVENTH_RANK[tileId] || FIFTH_RANK[tileId] || THIRD_RANK[tileId] || FIRST_RANK[tileId]) {
                setBackground(this.tileId % 2 == 1 ? lightTileColor : darkTileColor);
            }
        }


    }

}
