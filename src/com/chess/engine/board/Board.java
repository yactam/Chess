package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;

import java.util.*;

import static com.chess.engine.Alliance.BLACK;
import static com.chess.engine.Alliance.WHITE;
import static com.chess.engine.board.BoardUtils.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final Collection<Move> whiteStandardLegalMoves;
    private final Collection<Move> blackStandardLegalMoves;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(gameBoard, WHITE);
        this.blackPieces = calculateActivePieces(gameBoard, BLACK);

        this.blackStandardLegalMoves = calculateLegalMoves(blackPieces);
        this.whiteStandardLegalMoves = calculateLegalMoves(whitePieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();

        for(Piece piece : pieces) {
            Collection<Move> moves = piece.calculateLegalMoves(this);
            legalMoves.addAll(moves);
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(Tile tile : gameBoard) {
            Piece piece = tile.getPiece();
            if(piece != null && piece.getAlliance().equals(alliance)) {
                activePieces.add(piece);
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    private static List<Tile> createGameBoard(Builder builder) {
        final Tile[] tiles = new Tile[NUM_TILES];
        for(int i = 0; i < NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        // Black
        builder.setPiece(new Rook(0, BLACK));
        builder.setPiece(new Knight(1, BLACK));
        builder.setPiece(new Bishop(2, BLACK));
        builder.setPiece(new Queen(3, BLACK));
        builder.setPiece(new King(4, BLACK));
        builder.setPiece(new Bishop(5, BLACK));
        builder.setPiece(new Knight(6, BLACK));
        builder.setPiece(new Rook(7, BLACK));
        builder.setPiece(new Pawn(8, BLACK));
        builder.setPiece(new Pawn(9, BLACK));
        builder.setPiece(new Pawn(10, BLACK));
        builder.setPiece(new Pawn(11, BLACK));
        builder.setPiece(new Pawn(12, BLACK));
        builder.setPiece(new Pawn(13, BLACK));
        builder.setPiece(new Pawn(14, BLACK));
        builder.setPiece(new Pawn(15, BLACK));
        // White
        builder.setPiece(new Rook(56, WHITE));
        builder.setPiece(new Knight(57, WHITE));
        builder.setPiece(new Bishop(58, WHITE));
        builder.setPiece(new Queen(59, WHITE));
        builder.setPiece(new King(60, WHITE));
        builder.setPiece(new Bishop(61, WHITE));
        builder.setPiece(new Knight(62, WHITE));
        builder.setPiece(new Rook(63, WHITE));
        builder.setPiece(new Pawn(48, WHITE));
        builder.setPiece(new Pawn(49, WHITE));
        builder.setPiece(new Pawn(50, WHITE));
        builder.setPiece(new Pawn(51, WHITE));
        builder.setPiece(new Pawn(52, WHITE));
        builder.setPiece(new Pawn(53, WHITE));
        builder.setPiece(new Pawn(54, WHITE));
        builder.setPiece(new Pawn(55, WHITE));
        // Next to move is WHITE
        builder.setMoveMaker(WHITE);
        return builder.build();
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    public Collection<Piece> getWhitePieces() {
        return whitePieces;
    }

    public Collection<Piece> getBlackPieces() {
        return blackPieces;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < NUM_TILES; i++) {
            final String tileText = gameBoard.get(i).toString();
            stringBuilder.append(String.format("%3s", tileText));
            if((i + 1) % NUM_TILES_PER_ROW == 0) stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        public Builder() {
            boardConfig = new HashMap<>();
        }

        public Builder setPiece(Piece piece) {
            this.boardConfig.put(piece.getPosition(), piece);
            return this;
        }

        public Builder setMoveMaker(Alliance alliance) {
            this.nextMoveMaker = alliance;
            return this;
        }


        public Board build() {
            return new Board(this);
        }

    }
}
