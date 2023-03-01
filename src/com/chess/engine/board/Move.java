package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import static com.chess.engine.board.Board.*;

public abstract class Move {

    final Board board;
    final Piece piece;
    final int destinationCoordinates;
    public static final NullMove NULL_MOVE = new NullMove();

    private Move(Board board, Piece piece, int destinationCoordinates) {
        this.board = board;
        this.piece = piece;
        this.destinationCoordinates = destinationCoordinates;
    }

    public Integer getDestinationCoordinate() {
        return destinationCoordinates;
    }

    public Piece getMovePiece() {
        return piece;
    }

    public Board execute() {
        final Builder builder = new Builder();

        for(Piece piece : board.currentPlayer().getActivePieces()) {
            if(!this.piece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for(Piece piece : board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(piece.movePiece(this)); // move the piece (clear the tile)
        builder.setMoveMaker(board.currentPlayer().getOpponent().getAlliance()); // change the current player after the move is made

        return builder.build();
    }

    public static final class MajorMove extends Move {

        public MajorMove(Board board, Piece piece, int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static class AttackMove extends Move {

        final Piece attackedPiece;
        public AttackMove(Board board, Piece piece, int destinationCoordinates, Piece attackedPiece) {
            super(board, piece, destinationCoordinates);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    public static final class PawnMove extends Move {

        public PawnMove(Board board, Piece piece, int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(Board board, Piece piece, int destinationCoordinates, Piece attackedPiece) {
            super(board, piece, destinationCoordinates, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {

        public PawnEnPassantAttackMove(Board board, Piece piece, int destinationCoordinates, Piece attackedPiece) {
            super(board, piece, destinationCoordinates, attackedPiece);
        }
    }

    public static class PawnJump extends Move {

        public PawnJump(Board board, Piece piece, int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    static abstract class CastleMove extends Move {
        public CastleMove(Board board, Piece movePiece, int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);

        }
    }

    public static class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(Board board, Piece piece, int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(Board board, Piece piece, int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static class NullMove extends Move {

        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException();
        }
    }

    public static class MoveFactory {
        public static Move createMove(Board board, int currentCoordinate, int destinationCoordinate) {
            for(Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate && move.destinationCoordinates == destinationCoordinate)
                    return move;
            }
            return NULL_MOVE;
        }
    }

    private int getCurrentCoordinate() {
        return piece.getPosition();
    }
}
