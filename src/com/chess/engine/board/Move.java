package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece piece;
    final int destinationCoordinates;

    private Move(Board board, Piece piece, int destinationCoordinates) {
        this.board = board;
        this.piece = piece;
        this.destinationCoordinates = destinationCoordinates;
    }

    public Integer getDestinationCoordinate() {
        return destinationCoordinates;
    }

    public abstract Board execute();

    public static final class MajorMove extends Move {

        public MajorMove(Board board, Piece piece, int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    public static final class AttackMove extends Move {

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
}
