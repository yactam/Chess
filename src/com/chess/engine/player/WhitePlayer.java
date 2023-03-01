package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    Collection<Piece> getActivePieces() {
        return board.getWhitePieces();
    }

    @Override
    Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    Player getOpponent() {
        return this.board.blackPlayer();
    }
}
