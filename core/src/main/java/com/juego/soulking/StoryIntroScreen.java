package com.juego.soulking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Story introduction shown only before the first level starts for the first time. */
public class StoryIntroScreen implements Screen {
    private static final float WORLD_WIDTH = 1280f;
    private static final float WORLD_HEIGHT = 720f;
    private static final float TEXT_WIDTH = 920f;
    private static final float MESSAGE_TIME_SECONDS = 4f;

    private final String[] messages = {
        "Un enemigo intento robar las almas del reino.",
        "Logramos derrotarlo, pero las almas que habia conseguido quedaron esparcidas.",
        "Ahora tu mision es recuperarlas antes de que la oscuridad vuelva a levantarse.",
        "Cada nivel guarda una parte del reino. Encuentra las almas y restaura su luz."
    };

    private final SoulKingGame game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Viewport viewport;
    private final Vector2 touchPosition;
    private int messageIndex;
    private float elapsedTime;

    public StoryIntroScreen(SoulKingGame game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        touchPosition = new Vector2();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touchPosition.set(screenX, screenY);
                viewport.unproject(touchPosition);
                nextMessage();
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        if (elapsedTime >= MESSAGE_TIME_SECONDS) {
            nextMessage();
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.2f);
        font.draw(
            batch,
            messages[messageIndex],
            (WORLD_WIDTH - TEXT_WIDTH) / 2f,
            390f,
            TEXT_WIDTH,
            Align.center,
            true
        );

        font.getData().setScale(1.2f);
        font.draw(batch, "Toca para continuar", 0f, 90f, WORLD_WIDTH, Align.center, false);
        batch.end();
    }

    private void nextMessage() {
        elapsedTime = 0f;
        messageIndex++;

        if (messageIndex >= messages.length) {
            game.finishIntro();
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }

        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
