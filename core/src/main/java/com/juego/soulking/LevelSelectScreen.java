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

/** Screen for choosing a specific level. */
public class LevelSelectScreen implements Screen {
    private static final float WORLD_WIDTH = 1280f;
    private static final float WORLD_HEIGHT = 720f;
    private static final float BUTTON_WIDTH = 190f;
    private static final float BUTTON_HEIGHT = 120f;
    private static final int LEVEL_COUNT = 4;

    private final SoulKingGame game;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;
    private final Viewport viewport;
    private final Vector2 touchPosition;
    private final Rectangle[] levelButtons;
    private final Rectangle backButton;
    private Texture background;

    public LevelSelectScreen(SoulKingGame game) {
        this.game = game;
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = new BitmapFont();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        touchPosition = new Vector2();
        levelButtons = new Rectangle[LEVEL_COUNT];
        backButton = new Rectangle(40f, 40f, 190f, 70f);

        float startX = 170f;
        float gap = 80f;
        for (int i = 0; i < LEVEL_COUNT; i++) {
            levelButtons[i] = new Rectangle(startX + i * (BUTTON_WIDTH + gap), 300f, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
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
        Gdx.gl.glClearColor(0.04f, 0.05f, 0.07f, 1f);
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
        shapes.setColor(0.14f, 0.16f, 0.19f, 0.92f);
        for (Rectangle button : levelButtons) {
            shapes.rect(button.x, button.y, button.width, button.height);
        }
        shapes.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(0.82f, 0.74f, 0.48f, 1f);
        for (Rectangle button : levelButtons) {
            shapes.rect(button.x, button.y, button.width, button.height);
        }
        shapes.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        shapes.end();
    }

    private void drawText() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(3.4f);
        font.draw(batch, "Niveles", 0f, 590f, WORLD_WIDTH, Align.center, false);

        font.getData().setScale(2.4f);
        for (int i = 0; i < LEVEL_COUNT; i++) {
            Rectangle button = levelButtons[i];
            font.draw(batch, String.valueOf(i + 1), button.x, button.y + 76f, button.width, Align.center, false);
        }

        font.getData().setScale(1f);
        for (int i = 0; i < LEVEL_COUNT; i++) {
            Rectangle button = levelButtons[i];
            int bestScore = game.getBestScore(i + 1);
            String scoreText = bestScore > 0 ? "Mejor: " + bestScore : "Mejor: --";
            font.draw(batch, scoreText, button.x, button.y + 34f, button.width, Align.center, false);
        }

        font.getData().setScale(1.7f);
        font.draw(batch, "Volver", backButton.x, backButton.y + 46f, backButton.width, Align.center, false);
        batch.end();
    }

    private void handleTouch(float x, float y) {
        for (int i = 0; i < LEVEL_COUNT; i++) {
            if (levelButtons[i].contains(x, y)) {
                game.openLevel(i + 1);
                return;
            }
        }

        if (backButton.contains(x, y)) {
            game.openMainMenu();
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
