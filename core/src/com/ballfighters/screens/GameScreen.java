package com.ballfighters.screens;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.ballfighters.game.world.BallWorld;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/22/2014.
 */
public class GameScreen implements Screen {
    public TweenManager tweenManager;

    private BallWorld ballWorld;
    private Sprite background;
    private SpriteBatch batch, bgBatch, fadeOut;


    private int track;

    public void render(float delta){
//
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        //animate background
//        GameData.ANIMATEDBG.update();
//        background = new Sprite(GameData.ANIMATEDBG.currentFrame);
//        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//        bgBatch.begin();
//        //draw background
//        background.draw(bgBatch);
//        //draw health bars
//        GameData.PLAYER_1_HEALTH_BAR_BOX.draw(bgBatch);
//        GameData.PLAYER_1_HEALTH_BAR.draw(bgBatch);
//        GameData.PLAYER_2_HEALTH_BAR_BOX.draw(bgBatch);
//        GameData.PLAYER_2_HEALTH_BAR.draw(bgBatch);
//        bgBatch.end();
//
//        ballWorld.update();
//        ballWorld.render();
//
//        fadeOut.begin();
//        GameData.BLACKSCREEN.draw(fadeOut);
//        fadeOut.end();
//
//        tweenManager.update(delta);
//
//        //should be called in update method:
//        loopMusic();
    }
    public void show(){
//        GameData.screen = this;
//        GameData.initializeHealthBars();
//        track= MathUtils.random(0, 5);
//        batch = new SpriteBatch();
//        GameData.batch = batch;
//        bgBatch = new SpriteBatch();
//        fadeOut = new SpriteBatch();
//
//        ballWorld = new BallWorld(batch);
//
//        tweenManager = new TweenManager();
//        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
//
//        Tween.set(GameData.BLACKSCREEN,SpriteAccessor.ALPHA).target(0).start(tweenManager);
//        startMusic();
    }
    public void hide(){
        dispose();
    }
    public void pause(){

    }
    public void resume(){

    }
    public void resize(int width, int height){
//        ballWorld.camera.viewportWidth = width/4f;
//        ballWorld.camera.viewportHeight = height/4f;
//        ballWorld.camera.update();
    }
    public void dispose(){
    }

    public void startMusic() {
        track = MathUtils.random(0, 5);
        switch (track % 5) {
            case 5:
                GameData.playMusic("Music/ff3boss.ogg");
            case 1:
                GameData.playMusic("Music/dkc3tree[1].ogg");
            case 3:
                GameData.playMusic("Music/grnhill[1].ogg");
            case 4:
                GameData.playMusic("Music/WilyStronghold1.mp3");
        }
    }

    public void loopMusic() {
        track = MathUtils.random(0, 5);
        if(!GameData.music.isPlaying()) {
            switch (track % 5) {
                case 0:
                    GameData.playMusic("Music/ff3boss.ogg");
                case 1:
                    GameData.playMusic("Music/dkc3tree[1].ogg");
                case 3:
                    GameData.playMusic("Music/grnhill[1].ogg");
                case 4:
                    GameData.playMusic("Music/WilyStronghold1.mp3");
            }
        }
    }
}
