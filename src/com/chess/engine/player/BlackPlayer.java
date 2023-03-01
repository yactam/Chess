package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    Player getOpponent() {
        return board.whitePlayer();
    }
}
