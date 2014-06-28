package com.battlefighters.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.battlefighters.tween.SpriteAccessor;

public class MainMenuScreen implements Screen {

    private TweenManager tweenManager;
    private Stage stage;


    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        Table.drawDebug(stage);

        stage.act(delta);
        stage.draw();


    }

    public void resize(int i, int i1){

    }

    public void show(){
        stage = new Stage();

        Table table;
        TextureAtlas textureAtlasPlay1, textureAtlasPlay2, textureAtlastExit;
        Button buttonPlay2, buttonPlay1, exitButton;
        Skin skin1, skin2, skin3;

        Gdx.input.setInputProcessor(stage);

        textureAtlasPlay1 = new TextureAtlas(Gdx.files.internal("Buttons/1PlayerButton.pack"));
        textureAtlasPlay2 = new TextureAtlas(Gdx.files.internal("Buttons/2PlayerButton.pack"));
        textureAtlastExit = new TextureAtlas(Gdx.files.internal("Buttons/ExitButton.pack"));
        skin1 = new Skin(textureAtlasPlay1);
        skin2 = new Skin(textureAtlasPlay2);
        skin3 = new Skin(textureAtlastExit);

        table = new Table();
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        Button.ButtonStyle style1 = new Button.ButtonStyle();
        style1.up = skin1.getDrawable("StartButton");
        style1.down = skin1.getDrawable("StartButtonPressed");
        Button.ButtonStyle style2 = new Button.ButtonStyle();
        style2.up = skin2.getDrawable("2PlayerStartButton");
        style2.down = skin2.getDrawable("2PlayerStartButtonPressed");
        Button.ButtonStyle style3 = new Button.ButtonStyle();
        style3.up = skin3.getDrawable("ExitUp");
        style3.down = skin3.getDrawable("ExitDown");

        buttonPlay1 = new Button(style1);
        buttonPlay1.pad(40);
        buttonPlay1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new CharacterSelectScreen());
            }
        });
        buttonPlay2 = new Button(style2);
        buttonPlay2.pad(40);
        exitButton = new Button(style3);
        exitButton.pad(40);
        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        Button blankButton = new Button();
        blankButton.setVisible(false);
        blankButton.pad(20);

        table.add(buttonPlay1);
        table.add(buttonPlay2);
        table.add(exitButton);
        table.debug();  //remove later
        stage.addActor(table);

        //fade in screen
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.set(stage.getActors().first(),SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(stage.getBatch(), SpriteAccessor.ALPHA, 3).target(1).start(tweenManager);

    }

    public void hide(){

    }

    public void pause(){

    }

    public void resume(){

    }

    public void dispose(){

    }
}
