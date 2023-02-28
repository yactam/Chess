package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int position;
    protected final Alliance pieceAlliance; // color
    protected final boolean isFirstMove;

    Piece(final int position, final Alliance alliance) {
        this.pieceAlliance = alliance;
        this.position = position;
        isFirstMove = false;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);
}
