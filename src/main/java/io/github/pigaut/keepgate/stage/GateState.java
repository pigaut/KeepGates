package io.github.pigaut.keepgate.stage;

public enum GateState {

    OPENED(false),
    CLOSED(false),
    OPENING(true),
    CLOSING(true);

    private final boolean transition;

    GateState(boolean transition) {
        this.transition = transition;
    }

    public boolean isTransition() {
        return transition;
    }

}
