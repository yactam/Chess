package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.Alliance.*;

public class Pawn extends Piece {

    private static final int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16};
    Pawn(int position, Alliance alliance) {
        super(position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.position + (currentCandidateOffset * this.pieceAlliance.getDirection());

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) continue;

            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                //TODO more work to do here
                legalMoves.add(new Move.MajorMove());
            } else if(currentCandidateOffset == 16 && this.isFirstMove
                    && ((BoardUtils.SECOND_ROW[this.position] && this.pieceAlliance.equals(BLACK))
                         || (BoardUtils.SEVENTH_ROW[position] && this.pieceAlliance.equals(WHITE)))) {
                    final int behindCandidateDestinationCoordinate = this.position + (this.pieceAlliance.getDirection() * 8);
                    if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()
                            && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        //TODO more work to do here
                        legalMoves.add(new Move.MajorMove());
                    }
            } else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHT_COLUMN[position] && pieceAlliance.equals(WHITE)
                            || BoardUtils.FIRST_COLUMN[position] && pieceAlliance.equals(BLACK)))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceAlliance != pieceOnCandidate.pieceAlliance) {
                        //TODO more work to do here
                        legalMoves.add(new Move.AttackMove());
                    }
                }
            } else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.EIGHT_COLUMN[position] && pieceAlliance.equals(BLACK)
                            || BoardUtils.FIRST_COLUMN[position] && pieceAlliance.equals(WHITE)))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceAlliance != pieceOnCandidate.pieceAlliance) {
                        //TODO more work to do here
                        legalMoves.add(new Move.AttackMove());
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
}
