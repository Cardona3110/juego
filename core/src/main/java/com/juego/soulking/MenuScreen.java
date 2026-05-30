package com.juego.soulking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Main menu displayed after the welcome screen. */
public class MenuScreen implements Screen {
    private static final float WORLD_WIDTH = 1280f;
    private static final float WORLD_HEIGHT = 720f;
    private static final float BUTTON_WIDTH = 360f;
    private static final float BUTTON_HEIGHT = 80f;

    private final SoulKingGame game;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;
    private final Viewport viewport;
    private final Vector2 touchPosition;
    private final Rectangle playButton;
    private final Rectangle levelsButton;
    private final Rectangle exitButton;
    private Texture background;

    public MenuScreen(SoulKingGame game) {
        this.game = game;
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = new BitmapFont();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        touchPosition = new Vector2();

        float buttonX = (WORLD_WIDTH - BUTTON_WIDTH) / 2f;
        playButton = new Rectangle(buttonX, 360f, BUTTON_WIDTH, BUTTON_HEIGHT);
        levelsButton = new Rectangle(buttonX, 250f, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton = new Rectangle(buttonX, 140f, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal("Menu_img.jpg"));

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touchPosition.set(screenX, screenY);
                viewport.unproject(touchPosition);
                handleTouch(touchPosition.x, touchPosition.y);
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.06f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        drawBackground();
        drawButtons();
        drawText();
    }

    private void drawBackground() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();
    }

    private void drawButtons() {
        shapes.setProjectionMatrix(viewport.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.18f, 0.20f, 0.24f, 1f);
        shapes.rect(playButton.x, playButton.y, playButton.width, playButton.height);
        shapes.rect(levelsButton.x, levelsButton.y, levelsButton.width, levelsButton.height);
        shapes.rect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(0.82f, 0.74f, 0.48f, 1f);
        shapes.rect(playButton.x, playButton.y, playButton.width, playButton.height);
        shapes.rect(levelsButton.x, levelsButton.y, levelsButton.width, levelsButton.height);
        shapes.rect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        shapes.end();
    }

    private void drawText() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(4f);
        font.draw(batch, "SoulKing", 0f, 590f, WORLD_WIDTH, Align.center, false);

        font.getData().setScale(2.2f);
        drawButtonText("Jugar", playButton);
        drawButtonText("Niveles", levelsButton);
        drawButtonText("Salir", exitButton);
        batch.end();
    }

    private void drawButtonText(String text, Rectangle button) {
        font.draw(batch, text, button.x, button.y + 52f, button.width, Align.center, false);
    }

    private void handleTouch(float x, float y) {
        if (playButton.contains(x, y)) {
            game.play();
        } else if (levelsButton.contains(x, y)) {
            game.openLevelsMenu();
        } else if (exitButton.contains(x, y)) {
            Gdx.app.exit();
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
        shapes.dispose();
        font.dispose();
        if (background != null) {
            background.dispose();
        }
    }
}
