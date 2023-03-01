package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.King;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class Player {

    protected final Board board;
    protected final King king;
    protected final Collection<Move> legalMoves;
    protected final Collection<Move> opponentMoves;
    private final boolean isInCheck;

    Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
        this.king = establishKing();
        this.board = board;
        this.legalMoves = legalMoves;
        this.opponentMoves = opponentMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.king.getPosition(), opponentMoves).isEmpty();
    }

    private static Collection<Move> calculateAttacksOnTile(Integer position, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(Move move: opponentMoves) {
            if(position.equals(move.getDestinationCoordinate())) attackMoves.add(move);
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing() {
        for(Piece piece : getActivePieces()) {
            if(piece.getPieceType().equals(Piece.PieceType.KING)) {
                return (King) piece;
            }
        }
        throw new RuntimeException("King is dead !!!!!!!!!");
    }

    public boolean isMoveLegal(Move move) {
        return legalMoves.contains(move);
    }

    public boolean isInCheck() {
        return isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves() {
        for(Move move : legalMoves) {
            MoveTransition transition = makeMove(move);
            if(transition.getStatus().isDone()) return true;
        }
        return false;
    }

    public boolean isInStaleMate() {
        return false;
    }

    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(Move move) {
        if(!isMoveLegal(move)) return new MoveTransition(board, move, MoveStatus.ILLEGAL_MOVE);

        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPosition(),
                                                                            transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()) return new MoveTransition(board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    private Piece getPlayerKing() {
        return king;
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

}
