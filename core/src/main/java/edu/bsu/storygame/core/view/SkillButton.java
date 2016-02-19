package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.MonsterGame;
import tripleplay.ui.Button;
import tripleplay.ui.Constraints;

public class SkillButton extends Button {


    public SkillButton(String text, MonsterGame game) {
        super(text);
        setConstraint(Constraints.fixedSize(game.bounds.width() * 0.1f, game.bounds.height() * 0.1f));
    }
}
