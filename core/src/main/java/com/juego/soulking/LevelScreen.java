package com.juego.soulking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Playable platform levels: jump, collect souls, and reach the portal. */
public class LevelScreen implements Screen {
    private static final float VIEW_WIDTH = 1280f;
    private static final float VIEW_HEIGHT = 720f;
    private static final float GRAVITY = -1800f;
    private static final float MOVE_SPEED = 430f;
    private static final float JUMP_SPEED = 780f;
    private static final int LAST_LEVEL = 4;

    private final SoulKingGame game;
    private final int level;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;
    private final Viewport viewport;
    private final Rectangle player;
    private final Vector2 velocity;
    private final Array<Rectangle> platforms;
    private final Array<Rectangle> souls;
    private final Array<Boolean> collectedSouls;
    private final Rectangle portal;
    private final Rectangle backControl;
    private final Rectangle forwardControl;
    private final Rectangle jumpControl;

    private Texture backgroundTexture;
    private Texture playerTexture;
    private Texture soulTexture;
    private Texture portalTexture;
    private String backgroundFile;
    private float worldWidth;
    private boolean grounded;
    private boolean levelFinished;
    private boolean newBestScore;
    private float elapsedTime;
    private int collectedCount;
    private int finalScore;

    public LevelScreen(SoulKingGame game, int level) {
        this.game = game;
        this.level = level;
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = new BitmapFont();
        viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT);
        player = new Rectangle(90f, 155f, 54f, 72f);
        velocity = new Vector2();
        platforms = new Array<>();
        souls = new Array<>();
        collectedSouls = new Array<>();
        portal = new Rectangle();
        backControl = new Rectangle(35f, 35f, 115f, 95f);
        forwardControl = new Rectangle(175f, 35f, 115f, 95f);
        jumpControl = new Rectangle(1130f, 35f, 115f, 95f);

        createLevel();
    }

    @Override
    public void show() {
        game.saveCurrentLevel(level);
        backgroundTexture = new Texture(Gdx.files.internal(backgroundFile));
        playerTexture = new Texture(Gdx.files.internal("personaje_jugador.png"));
        soulTexture = new Texture(Gdx.files.internal("alma_sin_fondo.png"));
        portalTexture = new Texture(Gdx.files.internal("portal_sin_fondo.png"));
    }

    @Override
    public void render(float delta) {
        if (level > 2) {
            drawComingSoonLevel();
            return;
        }

        update(Math.min(delta, 1 / 30f));

        Gdx.gl.glClearColor(0.02f, 0.03f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        updateCamera();
        drawWorld();
        drawHud();
        drawTouchControls();
    }

    private void createLevel() {
        if (level == 2) {
            createLevelTwo();
        } else {
            createLevelOne();
        }
    }

    private void createLevelOne() {
        backgroundFile = "fondo_nivel_1.jpg";
        worldWidth = 4400f;
        portal.set(4230f, 150f, 100f, 135f);

        platforms.add(new Rectangle(0f, 70f, 660f, 70f));
        platforms.add(new Rectangle(760f, 145f, 250f, 45f));
        platforms.add(new Rectangle(1120f, 250f, 260f, 45f));
        platforms.add(new Rectangle(1510f, 160f, 320f, 45f));
        platforms.add(new Rectangle(1940f, 300f, 240f, 45f));
        platforms.add(new Rectangle(2290f, 220f, 330f, 45f));
        platforms.add(new Rectangle(2720f, 120f, 260f, 45f));
        platforms.add(new Rectangle(3130f, 250f, 300f, 45f));
        platforms.add(new Rectangle(3600f, 165f, 280f, 45f));
        platforms.add(new Rectangle(4050f, 70f, 350f, 70f));

        addSoul(810f, 225f);
        addSoul(1200f, 330f);
        addSoul(1625f, 240f);
        addSoul(2020f, 380f);
        addSoul(2415f, 300f);
        addSoul(3235f, 330f);
        addSoul(3690f, 245f);
    }

    private void createLevelTwo() {
        backgroundFile = "fondo_nivel_2.jpg";
        worldWidth = 5000f;
        portal.set(4825f, 330f, 105f, 140f);

        platforms.add(new Rectangle(0f, 70f, 560f, 70f));
        platforms.add(new Rectangle(680f, 190f, 230f, 45f));
        platforms.add(new Rectangle(1040f, 320f, 220f, 45f));
        platforms.add(new Rectangle(1390f, 210f, 260f, 45f));
        platforms.add(new Rectangle(1780f, 110f, 220f, 45f));
        platforms.add(new Rectangle(2150f, 250f, 260f, 45f));
        platforms.add(new Rectangle(2540f, 380f, 220f, 45f));
        platforms.add(new Rectangle(2910f, 260f, 310f, 45f));
        platforms.add(new Rectangle(3370f, 150f, 240f, 45f));
        platforms.add(new Rectangle(3740f, 290f, 250f, 45f));
        platforms.add(new Rectangle(4150f, 420f, 260f, 45f));
        platforms.add(new Rectangle(4650f, 250f, 350f, 70f));

        addSoul(735f, 270f);
        addSoul(1115f, 400f);
        addSoul(1490f, 290f);
        addSoul(2225f, 330f);
        addSoul(2600f, 460f);
        addSoul(3030f, 340f);
        addSoul(3830f, 370f);
        addSoul(4245f, 500f);
    }

    private void addSoul(float x, float y) {
        souls.add(new Rectangle(x, y, 48f, 54f));
        collectedSouls.add(false);
    }

    private void update(float delta) {
        if (levelFinished) {
            if (shouldContinueAfterLevelFinished()) {
                int nextLevel = Math.min(level + 1, LAST_LEVEL);
                game.saveCurrentLevel(nextLevel);
                game.openCurrentLevel();
            }
            return;
        }

        elapsedTime += delta;
        velocity.x = getMoveDirection() * MOVE_SPEED;
        if (shouldJump() && grounded) {
            velocity.y = JUMP_SPEED;
            grounded = false;
        }

        velocity.y += GRAVITY * delta;
        moveHorizontally(delta);
        moveVertically(delta);
        collectSouls();
        checkPortal();
        keepPlayerInWorld();
    }

    private float getMoveDirection() {
        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);

        for (int i = 0; i < 5; i++) {
            if (Gdx.input.isTouched(i)) {
                Vector2 touch = screenToCameraPosition(Gdx.input.getX(i), Gdx.input.getY(i));
                left = left || backControl.contains(touch.x, touch.y);
                right = right || forwardControl.contains(touch.x, touch.y);
            }
        }

        if (left && !right) return -1f;
        if (right && !left) return 1f;
        return 0f;
    }

    private boolean shouldJump() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            return true;
        }

        for (int i = 0; i < 5; i++) {
            if (Gdx.input.isTouched(i)) {
                Vector2 touch = screenToCameraPosition(Gdx.input.getX(i), Gdx.input.getY(i));
                if (jumpControl.contains(touch.x, touch.y)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Vector2 screenToCameraPosition(int screenX, int screenY) {
        Vector2 touch = new Vector2(screenX, screenY);
        viewport.unproject(touch);
        touch.x -= viewport.getCamera().position.x - VIEW_WIDTH / 2f;
        return touch;
    }

    private void moveHorizontally(float delta) {
        player.x += velocity.x * delta;
        for (Rectangle platform : platforms) {
            if (player.overlaps(platform)) {
                player.x = velocity.x > 0f ? platform.x - player.width : platform.x + platform.width;
            }
        }
    }

    private void moveVertically(float delta) {
        player.y += velocity.y * delta;
        grounded = false;

        for (Rectangle platform : platforms) {
            if (player.overlaps(platform)) {
                if (velocity.y <= 0f) {
                    player.y = platform.y + platform.height;
                    grounded = true;
                } else {
                    player.y = platform.y - player.height;
                }
                velocity.y = 0f;
            }
        }
    }

    private void collectSouls() {
        for (int i = 0; i < souls.size; i++) {
            if (!collectedSouls.get(i) && player.overlaps(souls.get(i))) {
                collectedSouls.set(i, true);
                collectedCount++;
            }
        }
    }

    private void checkPortal() {
        if (collectedCount == souls.size && player.overlaps(portal)) {
            levelFinished = true;
            finalScore = calculateScore();
            newBestScore = game.saveBestScore(level, finalScore);
        }
    }

    private int calculateScore() {
        int timePenalty = (int)(elapsedTime * 100f);
        int soulBonus = collectedCount * 500;
        return Math.max(100, 10000 + soulBonus - timePenalty);
    }

    private String formatTime(float seconds) {
        int totalSeconds = (int)seconds;
        int minutes = totalSeconds / 60;
        int remainingSeconds = totalSeconds % 60;
        return minutes + ":" + (remainingSeconds < 10 ? "0" : "") + remainingSeconds;
    }

    private void keepPlayerInWorld() {
        player.x = MathUtils.clamp(player.x, 0f, worldWidth - player.width);
        if (player.y < -180f) {
            player.setPosition(90f, 155f);
            velocity.setZero();
        }
    }

    private void updateCamera() {
        float cameraX = MathUtils.clamp(player.x + player.width / 2f, VIEW_WIDTH / 2f, worldWidth - VIEW_WIDTH / 2f);
        viewport.getCamera().position.set(cameraX, VIEW_HEIGHT / 2f, 0f);
        viewport.getCamera().update();
    }

    private void drawWorld() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0f, 0f, worldWidth, VIEW_HEIGHT);
        batch.draw(portalTexture, portal.x, portal.y, portal.width, portal.height);
        for (int i = 0; i < souls.size; i++) {
            if (!collectedSouls.get(i)) {
                Rectangle soul = souls.get(i);
                batch.draw(soulTexture, soul.x, soul.y, soul.width, soul.height);
            }
        }
        batch.draw(playerTexture, player.x, player.y, player.width, player.height);
        batch.end();

        shapes.setProjectionMatrix(viewport.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(level == 2 ? new Color(0.11f, 0.13f, 0.20f, 1f) : new Color(0.18f, 0.12f, 0.09f, 1f));
        for (Rectangle platform : platforms) {
            shapes.rect(platform.x, platform.y, platform.width, platform.height);
        }
        shapes.end();
    }

    private void drawHud() {
        float left = viewport.getCamera().position.x - VIEW_WIDTH / 2f;
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.8f);
        font.draw(
            batch,
            "Nivel " + level + "   Almas: " + collectedCount + "/" + souls.size + "   Tiempo: " + formatTime(elapsedTime),
            left + 30f,
            680f
        );

        if (collectedCount < souls.size) {
            font.getData().setScale(1.1f);
            font.draw(batch, "Recolecta todas las almas para abrir el portal", left, 45f, VIEW_WIDTH, Align.center, false);
        }

        if (levelFinished) {
            font.getData().setScale(2.2f);
            font.draw(batch, "Nivel completado", left, 420f, VIEW_WIDTH, Align.center, false);
            font.getData().setScale(1.4f);
            font.draw(batch, "Puntaje: " + finalScore, left, 370f, VIEW_WIDTH, Align.center, false);
            if (newBestScore) {
                font.draw(batch, "Nuevo mejor puntaje", left, 330f, VIEW_WIDTH, Align.center, false);
            }
            font.getData().setScale(1.2f);
            font.draw(batch, "Toca para continuar", left, 285f, VIEW_WIDTH, Align.center, false);
        }
        batch.end();
    }

    private void drawTouchControls() {
        float left = viewport.getCamera().position.x - VIEW_WIDTH / 2f;

        shapes.setProjectionMatrix(viewport.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        drawControlFill(backControl, left, isTouchingControl(backControl));
        drawControlFill(forwardControl, left, isTouchingControl(forwardControl));
        drawControlFill(jumpControl, left, isTouchingControl(jumpControl));
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(1f, 1f, 1f, 0.85f);
        shapes.rect(left + backControl.x, backControl.y, backControl.width, backControl.height);
        shapes.rect(left + forwardControl.x, forwardControl.y, forwardControl.width, forwardControl.height);
        shapes.rect(left + jumpControl.x, jumpControl.y, jumpControl.width, jumpControl.height);
        shapes.end();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.2f);
        font.draw(batch, "<", left + backControl.x, backControl.y + 63f, backControl.width, Align.center, false);
        font.draw(batch, ">", left + forwardControl.x, forwardControl.y + 63f, forwardControl.width, Align.center, false);
        font.getData().setScale(1.4f);
        font.draw(batch, "Saltar", left + jumpControl.x, jumpControl.y + 58f, jumpControl.width, Align.center, false);
        batch.end();
    }

    private void drawControlFill(Rectangle control, float cameraLeft, boolean active) {
        shapes.setColor(active ? new Color(0.82f, 0.74f, 0.48f, 0.72f) : new Color(0.08f, 0.09f, 0.11f, 0.62f));
        shapes.rect(cameraLeft + control.x, control.y, control.width, control.height);
    }

    private boolean isTouchingControl(Rectangle control) {
        for (int i = 0; i < 5; i++) {
            if (Gdx.input.isTouched(i)) {
                Vector2 touch = screenToCameraPosition(Gdx.input.getX(i), Gdx.input.getY(i));
                if (control.contains(touch.x, touch.y)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAnyControlTouched() {
        return isTouchingControl(backControl) || isTouchingControl(forwardControl) || isTouchingControl(jumpControl);
    }

    private boolean shouldContinueAfterLevelFinished() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (Gdx.input.justTouched() && !isAnyControlTouched());
    }

    private void drawComingSoonLevel() {
        Gdx.gl.glClearColor(0.04f, 0.05f, 0.07f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(3f);
        font.draw(batch, "Nivel " + level, 0f, 390f, VIEW_WIDTH, Align.center, false);
        font.getData().setScale(1.4f);
        font.draw(batch, "Proximamente", 0f, 310f, VIEW_WIDTH, Align.center, false);
        batch.end();
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
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapes.dispose();
        font.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (playerTexture != null) playerTexture.dispose();
        if (soulTexture != null) soulTexture.dispose();
        if (portalTexture != null) portalTexture.dispose();
    }
}
