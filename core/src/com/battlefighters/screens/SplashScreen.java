package com.battlefighters.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.battlefighters.global.GameData;
import com.battlefighters.tween.SpriteAccessor;

/**
 * Created by Dell_Owner on 6/22/2014.
 */
public class SplashScreen implements Screen {

    private SpriteBatch batch;
    private Sprite splash;
    private TweenManager tweenManager;

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);
        batch.begin();
        splash.draw(batch);
        batch.end();
    }

    public void resize(int width, int height){

    }

    public void show(){
        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class,new SpriteAccessor());

        Texture splashTexture = new Texture(Gdx.files.internal("Backgrounds/StartScreen.png"));
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Music/GetLucky.mp3"));
        GameData.music.setVolume(GameData.VOLUME);
        GameData.music.play();

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, 3).target(1).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, 3).delay(6).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1).delay(3).start(tweenManager);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        }).start(tweenManager);
    }

    public void hide(){
        dispose();
    }

    public void pause(){

    }

    public void resume(){

    }

    public void dispose(){
       batch.dispose();
        splash.getTexture().dispose();
    }
}
