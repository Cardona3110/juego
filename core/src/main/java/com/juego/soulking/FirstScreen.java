package com.juego.soulking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Welcome screen displayed when the game starts. */
public class FirstScreen implements Screen {
    private static final float WORLD_WIDTH = 1024f;
    private static final float WORLD_HEIGHT = 1536f;
    private static final float SPLASH_TIME_SECONDS = 3f;

    private final SoulKingGame game;
    private final SpriteBatch batch;
    private final Viewport viewport;
    private Texture welcomeImage;
    private float elapsedTime;
    private boolean finished;

    public FirstScreen(SoulKingGame game) {
        this.game = game;
        batch = new SpriteBatch();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void show() {
        welcomeImage = new Texture(Gdx.files.internal("Bienvenida_img.png"));
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        if (elapsedTime >= SPLASH_TIME_SECONDS || Gdx.input.justTouched()) {
            openMenu();
            return;
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(welcomeImage, 0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (welcomeImage != null) {
            welcomeImage.dispose();
        }
    }

    private void openMenu() {
        if (finished) {
            return;
        }

        finished = true;
        game.setScreen(new MenuScreen(game));
        dispose();
    }
}
