package com.ballfighters.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.ballfighters.game.gamebody.Animator;
import com.ballfighters.global.GameData;
import com.ballfighters.tween.SpriteAccessor;

/**
 * Created by Dell_Owner on 6/22/2014.
 */
public class SplashScreen implements Screen {

    private SpriteBatch batch;
    private TweenManager tweenManager;
    private Animator animator;
    Sprite sprite;

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        animator.update(new Vector3(0,0,0));
        sprite = new Sprite(new TextureRegion(animator.currentFrame));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.begin();
        sprite.draw(batch);
        GameData.BLACKSCREEN.draw(batch);
        batch.end();
        tweenManager.update(delta);
    }

    public void resize(int width, int height){

    }

    public void show(){
    	GameData.screen = this;

        animator = new Animator("Backgrounds/SplashScreenAnimationSheet.png",1,4);

        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class,new SpriteAccessor());

        GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Music/GetLucky.mp3"));
        GameData.music.setVolume(GameData.VOLUME);
        GameData.music.play();
        GameData.music.setLooping(true);

        GameData.BLACKSCREEN.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Tween.set(GameData.BLACKSCREEN, SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(0).start(tweenManager);
        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).delay(6).target(1).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
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
        sprite.getTexture().dispose();
    }
}
