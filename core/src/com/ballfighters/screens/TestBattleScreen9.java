package com.ballfighters.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.ballfighters.game.world.BallWorld8;
import com.ballfighters.game.world.BallWorld9;
import com.ballfighters.global.GameData;
import com.ballfighters.tween.SpriteAccessor;

/**
 * Created by Dell_Owner on 7/13/2014.
 */
public class TestBattleScreen9 extends GameScreen {

    private BallWorld9 ballWorld;
    private Sprite background;
    private SpriteBatch batch, bgBatch, fadeOut;
    public TweenManager tweenManager;

    Label infoText1;
    Label infoText2;


    private int track;

    public void render(float delta){



        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //animate background
        GameData.ANIMATEDBG.update();
        background = new Sprite(GameData.ANIMATEDBG.currentFrame);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        bgBatch.begin();
        //draw background
        background.draw(bgBatch);
        //draw health bars
        GameData.PLAYER_1_HEALTH_BAR_BOX.draw(bgBatch);
        GameData.PLAYER_1_HEALTH_BAR.draw(bgBatch);
        GameData.PLAYER_2_HEALTH_BAR_BOX.draw(bgBatch);
        GameData.PLAYER_2_HEALTH_BAR.draw(bgBatch);
        infoText1.draw(bgBatch,1);
        infoText2.draw(bgBatch,1);
        bgBatch.end();

        ballWorld.update();
        ballWorld.render();

        fadeOut.begin();
        GameData.BLACKSCREEN.draw(fadeOut);
        fadeOut.end();

        tweenManager.update(delta);

        //should be called in update method:
        loopMusic();
    }
    public void show(){
        GameData.screen = this;
        GameData.initializeHealthBars();
        track= MathUtils.random(0, 5);
        batch = new SpriteBatch();
        GameData.batch = batch;
        bgBatch = new SpriteBatch();
        fadeOut = new SpriteBatch();

        ballWorld = new BallWorld9(batch);

        tweenManager = new TweenManager();
        GameData.tweenManager = tweenManager;
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Tween.set(GameData.BLACKSCREEN,SpriteAccessor.ALPHA).target(0).start(tweenManager);
        startMusic();

        Skin labelSkin = new Skin(Gdx.files.internal("Labels/Fonts/uiskin.json"));
        labelSkin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        labelSkin.getFont("default-font").setMarkupEnabled(true);
        labelSkin.getFont("default-font").setScale(1.0f);

        infoText1 = new Label(ballWorld.player1.name,labelSkin);
        infoText2 = new Label(ballWorld.player2.name,labelSkin);
        infoText1 .setAlignment(Align.bottom | Align.left);
        infoText2 .setAlignment(Align.bottom | Align.left);
        infoText1.setPosition(0,Gdx.graphics.getHeight()-infoText1.getHeight()-GameData.PLAYER_1_HEALTH_BAR.getHeight());
        infoText2.setPosition(Gdx.graphics.getWidth()-infoText2.getWidth(),Gdx.graphics.getHeight()-infoText2.getHeight()-GameData.PLAYER_2_HEALTH_BAR.getHeight());

    }

    public void hide(){
        dispose();
    }
    public void pause(){

    }
    public void resume(){

    }
    public void resize(int width, int height){
        ballWorld.camera.viewportWidth = width/6f;
        ballWorld.camera.viewportHeight = height/6f;
        ballWorld.camera.update();
    }
    public void dispose(){
        GameData.PLAYER_1_HEALTH_BAR.getTexture().dispose();
        GameData.PLAYER_1_HEALTH_BAR_BOX.getTexture().dispose();
        GameData.PLAYER_2_HEALTH_BAR.getTexture().dispose();
        GameData.PLAYER_2_HEALTH_BAR_BOX.getTexture().dispose();
    }

    public void startMusic() {
        GameData.playMusic("Music/MegamanStage2.mp3");
//        track = MathUtils.random(0, 5);
//        switch (track % 5) {
//            case 5:
//                GameData.playMusic("Music/ff3boss.ogg");
//            case 1:
//                GameData.playMusic("Music/dkc3tree[1].ogg");
//            case 3:
//                GameData.playMusic("Music/grnhill[1].ogg");
//            case 4:
//                GameData.playMusic("Music/WilyStronghold1.mp3");
//        }
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
