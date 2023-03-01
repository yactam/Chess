package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int position;
    protected final Alliance pieceAlliance; // color
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final int position, final Alliance alliance) {
        this.pieceAlliance = alliance;
        this.position = position;
        isFirstMove = false;
        this.cachedHashCode = computeCachedHashCode();
    }

    private int computeCachedHashCode() {
        int ret = getPieceType().hashCode();
        ret = 31 * ret + pieceAlliance.hashCode();
        ret = 31 * ret + position;
        ret = 31 * ret + (isFirstMove? 1 : 0);
        return ret;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public Integer getPosition() {
        return position;
    }

    public Alliance getAlliance() {
        return pieceAlliance;
    }

    public abstract PieceType getPieceType();
    public abstract Piece movePiece(Move move);

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Piece piece)) return false;
        return piece.position == this.position && piece.pieceAlliance == this.pieceAlliance && piece.isFirstMove == piece.isFirstMove
                && piece.getPieceType() == this.getPieceType();
     }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public enum PieceType {

        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        ROOK("R"),
        QUEEN("Q"),
        KING("K");

        private final String pieceName;

        PieceType(String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

    }
}
