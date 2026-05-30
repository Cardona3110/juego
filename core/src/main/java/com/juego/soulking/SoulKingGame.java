package com.juego.soulking;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SoulKingGame extends Game {
    private static final String PREFERENCES_NAME = "SoulKingProgress";
    private static final String INTRO_SEEN_KEY = "introSeen";
    private static final String CURRENT_LEVEL_KEY = "currentLevel";
    private static final int FIRST_LEVEL = 1;

    private Preferences preferences;

    @Override
    public void create() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        setScreen(new FirstScreen(this));
    }

    public void play() {
        if (hasSeenIntro()) {
            openCurrentLevel();
        } else {
            setScreen(new StoryIntroScreen(this));
        }
    }

    public void finishIntro() {
        preferences.putBoolean(INTRO_SEEN_KEY, true);
        preferences.putInteger(CURRENT_LEVEL_KEY, getCurrentLevel());
        preferences.flush();
        openCurrentLevel();
    }

    public void openCurrentLevel() {
        setScreen(new LevelScreen(this, getCurrentLevel()));
    }

    public void openLevel(int level) {
        saveCurrentLevel(level);
        setScreen(new LevelScreen(this, level));
    }

    public void openLevelsMenu() {
        setScreen(new LevelSelectScreen(this));
    }

    public void openMainMenu() {
        setScreen(new MenuScreen(this));
    }

    public int getCurrentLevel() {
        return preferences.getInteger(CURRENT_LEVEL_KEY, FIRST_LEVEL);
    }

    public void saveCurrentLevel(int level) {
        preferences.putInteger(CURRENT_LEVEL_KEY, level);
        preferences.flush();
    }

    public void resetProgress() {
        preferences.clear();
        preferences.flush();
    }

    public int getBestScore(int level) {
        return preferences.getInteger(getBestScoreKey(level), 0);
    }

    public boolean saveBestScore(int level, int score) {
        int bestScore = getBestScore(level);
        if (score <= bestScore) {
            return false;
        }

        preferences.putInteger(getBestScoreKey(level), score);
        preferences.flush();
        return true;
    }

    private boolean hasSeenIntro() {
        return preferences.getBoolean(INTRO_SEEN_KEY, false);
    }

    private String getBestScoreKey(int level) {
        return "bestScoreLevel" + level;
    }
}
