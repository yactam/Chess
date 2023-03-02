package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

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

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    @Override
    public int hashCode() {
        int ret = destinationCoordinates;
        ret = 31 * ret + piece.hashCode();
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(! (o instanceof Move move)) return false;
        return move.piece.equals(piece) && move.destinationCoordinates == destinationCoordinates;
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

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(! (o instanceof AttackMove attackMove)) return false;
            return super.equals(attackMove) && attackedPiece.equals(attackMove.attackedPiece);
        }
    }

    public static final class PawnMove extends Move {

        public PawnMove(Board board, Piece piece, int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }

        @Override
        public Board execute() {
            Builder builder = new Builder();
            for(Piece piece : board.currentPlayer().getActivePieces()) {
                if(!this.piece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(Piece piece : board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            Pawn movedPawn = (Pawn) piece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
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

        @Override
        public Board execute() {
            Builder builder = new Builder();
            for(Piece piece : board.currentPlayer().getActivePieces()) {
                if(!this.piece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(Piece piece : board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            Pawn movedPawn = (Pawn) piece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassant(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart, castleRookDestination;
        public CastleMove(Board board, Piece movePiece, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movePiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(Piece piece : board.currentPlayer().getActivePieces()) {
                if(!this.piece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for(Piece piece : board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(piece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, castleRook.getAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(Board board, Piece piece, int destinationCoordinates, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destinationCoordinates, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(Board board, Piece piece, int destinationCoordinates, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destinationCoordinates, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0-0";
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
        public static final NullMove NULL_MOVE = new NullMove();
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
