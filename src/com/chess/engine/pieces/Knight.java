package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.BoardUtils.*;
import static com.chess.engine.board.Move.*;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};
    Knight(final int position, final Alliance alliance) {
        super(position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for(int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.position + currentCandidate;
            if(isValidTileCoordinate(candidateDestinationCoordinate)) {
                if(isFirstColumnExclusion(this.position, currentCandidate)
                    || isSecondColumnExclusion(this.position, currentCandidate)
                        || isSevenColumnExclusion(this.position, currentCandidate)
                        || isEightColumnExclusion(this.position, currentCandidate)) continue;
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    Alliance pieceAlliance = pieceAtDestination.pieceAlliance;

                    if(this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition]
                && (candidateOffset == -17 || candidateOffset == -10 || candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition]
                && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSevenColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition]
                && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition]
                && (candidateOffset == -15 || candidateOffset == -6 || candidateOffset == 10 || candidateOffset == 17);
    }

}
