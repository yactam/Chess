package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerMoves, Collection<Move> opponentMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.king.isFirstMove() && !this.isInCheck()) {
            // White King side castle
            if(!board.getTile(5).isTileOccupied() && !board.getTile(6).isTileOccupied()) {
                final Tile rookTile = board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()
                        && Player.calculateAttacksOnTile(5, opponentMoves).isEmpty()
                        && Player.calculateAttacksOnTile(6, opponentMoves).isEmpty()
                        && rookTile.getPiece().getPieceType().equals(Piece.PieceType.ROOK)) {
                    kingCastles.add(new KingSideCastleMove(board, king, 6,
                            (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                }
            }
            // White Queen side castle
            if(!board.getTile(2).isTileOccupied() && !board.getTile(3).isTileOccupied()
                    && !board.getTile(1).isTileOccupied()) {
                final Tile rookTile = board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                        Player.calculateAttacksOnTile(2, opponentMoves).isEmpty() &&
                        Player.calculateAttacksOnTile(3, opponentMoves).isEmpty() &&
                        rookTile.getPiece().getPieceType().equals(Piece.PieceType.ROOK)) {
                    kingCastles.add(new QueenSideCastleMove(board, king, 2,
                            (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
