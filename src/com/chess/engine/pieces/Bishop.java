package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};

    Bishop(int position, Alliance alliance) {
        super(position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.position;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                
                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)
                  || isEightColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) break;

                candidateDestinationCoordinate += candidateCoordinateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        Alliance pieceAlliance = pieceAtDestination.pieceAlliance;

                        if(this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break; // There is a piece blocking the bishop to move forward
                    }
                }
            }
        }

        return legalMoves;
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == 9 || candidateOffset == -7);
    }
}
