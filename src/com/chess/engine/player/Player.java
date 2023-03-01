package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.King;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;


public abstract class Player {

    protected final Board board;
    protected final King king;
    protected final Collection<Move> legalMoves;
    protected final Collection<Move> opponentMoves;

    Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
        this.king = establishKing();
        this.board = board;
        this.legalMoves = legalMoves;
        this.opponentMoves = opponentMoves;
    }

    private King establishKing() {
        for(Piece piece : getActivePieces()) {
            if(piece.getPieceType().equals(Piece.PieceType.KING)) {
                return (King) piece;
            }
        }
        throw new RuntimeException("King is dead !!!!!!!!!");
    }

    abstract Collection<Piece> getActivePieces();

}
