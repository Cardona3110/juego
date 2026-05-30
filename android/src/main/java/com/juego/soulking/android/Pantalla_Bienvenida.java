package com.juego.soulking.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

public class Pantalla_Bienvenida implements Screen {

    private Stage stageLogo;
    public static Texture logoTexture;

    public Pantalla_Bienvenida(AndroidLauncher androidLauncher) {
    }

    @Override
    public void show() {
        logoTexture = new Texture(Gdx.files.internal("Bienvenida_img.png"));

        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Image image = new Image(logoTexture);

        image.setFillParent(true);

        image.setScaling(Scaling.fillY);

        stageLogo = new Stage();
        stageLogo.addActor(image);

        image.addAction(Actions.sequence(
            Actions.alpha(0),
            Actions.fadeIn(3.0f),
            Actions.delay(2.0f)
        ));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stageLogo.act(delta);
        stageLogo.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        logoTexture.dispose();
        stageLogo.dispose();
    }
}
