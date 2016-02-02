package edu.bsu.storygame.java;

import playn.java.LWJGLPlatform;

import edu.bsu.storygame.core.MonsterGame;

public class MonsterGameJava {

    public static void main(String[] args) {
        LWJGLPlatform.Config config = new LWJGLPlatform.Config();
        config.width = 800;
        config.height = 600;

        LWJGLPlatform plat = new LWJGLPlatform(config);
        new MonsterGame(plat);
        plat.start();
    }
}
