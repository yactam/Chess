package com.chess.engine.player;

import com.chess.engine.board.Move;

public enum MoveStatus {

    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },

    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    IS_DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    };

    public abstract boolean isDone();
}
