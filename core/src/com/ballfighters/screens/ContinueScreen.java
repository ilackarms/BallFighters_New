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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.ballfighters.global.AnimationPackage;
import com.ballfighters.global.GameData;
import com.ballfighters.tween.SpriteAccessor;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 8/3/2014.
 */
public class ContinueScreen implements Screen {

    private TweenManager tweenManager;
    private Stage stage;
    private SpriteBatch blackScreenBatch;
    private Boolean touched;
    private Screen screen;
    private String name;

    public ContinueScreen(Screen screen, String name){
        super();
        this.screen = screen;
        this.name = name;
    }

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
                    ((Game) Gdx.app.getApplicationListener()).setScreen(screen);
                }
            }).start(tweenManager);
        }
    }

    public void resize(int i, int i1){

    }

    public void show(){

        GameData.staticAnimations = new ArrayList<AnimationPackage>();

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

        Label infoText1 = new Label("You have defeated " + name +
                "!!\n\n" +
                "Tap to continue.",labelSkin);
        infoText1 .setAlignment(Align.bottom | Align.left);

        Image arrow = new Image(new Texture(Gdx.files.internal("Labels/Arrow.png")));

        table.row();
        table.add(infoText1).spaceTop(50f);
        table.row();
        table.add(arrow);
        table.row();

//        table.debug();  //remove later
        stage.addActor(table);

    }
}
