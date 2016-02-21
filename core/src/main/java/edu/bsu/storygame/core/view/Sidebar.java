package edu.bsu.storygame.core.view;

import edu.bsu.storygame.core.assets.Typeface;
import edu.bsu.storygame.core.model.GameContext;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Colors;

    public class Sidebar extends Group {
        public Sidebar(GameContext context) {
            super(AxisLayout.vertical());
            addStyles(Style.BACKGROUND.is(Background.solid(Colors.BLACK)));
            add(playerArea(context, 0)).setConstraint(AxisLayout.stretched(1.5f));
            add(playerArea(context, 1)).setConstraint(AxisLayout.stretched(1.5f));
        }

        public Group playerArea(GameContext context, int playerNum){
            Group group = new Group(AxisLayout.vertical());
            if (playerNum == 1) {
                group.addStyles(Style.BACKGROUND.is(Background.solid(Colors.RED)));
            } else if(playerNum == 0){
                group.addStyles(Style.BACKGROUND.is(Background.solid(Colors.BLUE)));
            }
            group.add(new Label(context.players.get(playerNum).getName())
                    .addStyles(Style.FONT.is(Typeface.OXYGEN.in(context.game).atSize(0.04f))));
            group.add(new Label("Skill: An Arbitrary Skill")
                    .addStyles(Style.FONT.is(Typeface.OXYGEN.in(context.game).atSize(0.03f))));
            group.add(new Label("Skill: Another Arbitrary Skill")
                    .addStyles(Style.FONT.is(Typeface.OXYGEN.in(context.game).atSize(0.03f))));
            group.setConstraint(AxisLayout.stretched(1.5f));
            return group;
        }
    }
