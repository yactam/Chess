package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.pieces.Piece;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerMoves, Collection<Move> opponentMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.king.isFirstMove() && !this.isInCheck()) {
            // White King side castle
            if(!board.getTile(61).isTileOccupied() && !board.getTile(62).isTileOccupied()) {
                final Tile rookTile = board.getTile(63);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()
                    && Player.calculateAttacksOnTile(61, opponentMoves).isEmpty()
                    && Player.calculateAttacksOnTile(62, opponentMoves).isEmpty()
                    && rookTile.getPiece().getPieceType().equals(Piece.PieceType.ROOK)) {
                    kingCastles.add(new KingSideCastleMove(board, king, 62,
                            (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                }
            }
            // White Queen side castle
            if(!board.getTile(59).isTileOccupied() && !board.getTile(58).isTileOccupied()
                && !board.getTile(57).isTileOccupied()) {
                final Tile rookTile = board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    kingCastles.add(new QueenSideCastleMove(board, king, 58,
                            (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                }
            }
        }


        return ImmutableList.copyOf(kingCastles);
    }
}
