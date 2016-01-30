package edu.bsu.storygame.android;

import playn.android.GameActivity;

import edu.bsu.storygame.core.MonsterGame;

public class MonsterGameActivity extends GameActivity {

  @Override public void main () {
    new MonsterGame(platform());
  }
}
