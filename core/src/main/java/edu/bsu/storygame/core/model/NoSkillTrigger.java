package edu.bsu.storygame.core.model;

import static com.google.common.base.Preconditions.*;

public final class NoSkillTrigger {
    public final Conclusion conclusion;

    public NoSkillTrigger(Conclusion conclusion) {
        this.conclusion = checkNotNull(conclusion);
    }
}
