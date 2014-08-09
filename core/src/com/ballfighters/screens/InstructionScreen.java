package com.ballfighters.screens;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.ballfighters.global.GameData;
import com.ballfighters.tween.SpriteAccessor;

/**
 * Created by Dell_Owner on 8/3/2014.
 */
public class InstructionScreen  implements Screen {

    private TweenManager tweenManager;
    private Stage stage;
    private SpriteBatch blackScreenBatch;
    private Boolean touched;

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

//        Table.drawDebug(stage);


        stage.act(delta);
        stage.draw();

        blackScreenBatch.begin();
        GameData.BLACKSCREEN.draw(blackScreenBatch);
        blackScreenBatch.end();

        if(Gdx.input.isTouched() && !touched){
            touched=true;
            Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                @Override
                public void onEvent(int i, BaseTween<?> baseTween) {
                    Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
                }
            }).start(tweenManager);
        }
    }

    public void resize(int i, int i1){

    }

    public void show(){
        GameData.screen = this;

        touched = false;

        initializeSprites();
        initializeStage();

        //fade in screen
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.set(GameData.BLACKSCREEN,SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(0).start(tweenManager);

        blackScreenBatch = new SpriteBatch();
    }

    public void hide(){
        dispose();
    }

    public void pause(){

    }

    public void resume(){

    }

    public void dispose(){
        stage.dispose();
    }

    protected void initializeSprites(){

        GameData.BLACKSCREEN.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    protected void initializeStage(){
        stage = new Stage();

        Table table;

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        Skin labelSkin = new Skin(Gdx.files.internal("Labels/Fonts/uiskin.json"));
        labelSkin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        labelSkin.getFont("default-font").setMarkupEnabled(true);
        labelSkin.getFont("default-font").setScale(2.5f);

        Label infoText1 = new Label("Ballfighters Instructions: \n" +
                "Tilt your device to move. \n" +
                "Fast tap to fire in the direction of your touch.\n" +
                "Do a long touch to activate your special ability. \n" +
                "Every character's special is different.\n" +
                "Attack your enemy and evade his attacks to win.\n" +
                "GLHF\n\n" +
                "Press anywhere to continue.",labelSkin);
        infoText1 .setAlignment(Align.bottom | Align.left);

        table.row();
        table.add(infoText1).spaceTop(50f);
        table.row();

//        table.debug();  //remove later
        stage.addActor(table);

    }
}
