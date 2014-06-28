package com.battlefighters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.battlefighters.global.GameData;


public class CharacterSelectScreen implements Screen {

    private Stage stage;

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Table.drawDebug(stage);

        stage.act(delta);
        stage.draw();


    }

    public void resize(int i, int i1){

    }

    public void show(){

        Table table;
        TextureAtlas textureAtlasBoo, textureAtlasPlay2, textureAtlastExit;
        Button booButton, buttonPlay2, exitButton;
        Skin booSkin, skin2, skin3;

        stage = new Stage();

        GameData.music.stop();
        GameData.music.dispose();
        GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenuMusic.ogg"));
        GameData.music.setVolume(GameData.VOLUME);
        GameData.music.play();

        Gdx.input.setInputProcessor(stage);

        textureAtlasBoo = new TextureAtlas(Gdx.files.internal("Buttons/BooKnockoff.pack"));
        textureAtlasPlay2 = new TextureAtlas(Gdx.files.internal("Buttons/2PlayerButton.pack"));
        textureAtlastExit = new TextureAtlas(Gdx.files.internal("Buttons/ExitButton.pack"));
        booSkin = new Skin(textureAtlasBoo);
        skin2 = new Skin(textureAtlasPlay2);
        skin3 = new Skin(textureAtlastExit);

        table = new Table();
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        Button.ButtonStyle style1 = new Button.ButtonStyle();
        style1.up = booSkin.getDrawable("BooUp");
        style1.down = booSkin.getDrawable("BooDown");
        Button.ButtonStyle style2 = new Button.ButtonStyle();
        style2.up = skin2.getDrawable("2PlayerStartButton");
        style2.down = skin2.getDrawable("2PlayerStartButtonPressed");
        Button.ButtonStyle style3 = new Button.ButtonStyle();
        style3.up = skin3.getDrawable("ExitUp");
        style3.down = skin3.getDrawable("ExitDown");

        booButton = new Button(style1);
        booButton.pad(40);
        booButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameData.music.stop();
                GameData.music.dispose();
                GameData.music = Gdx.audio.newMusic(Gdx.files.internal("MainMenuMusic.ogg"));
                GameData.music.setVolume(GameData.VOLUME);
                GameData.music.play();
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

        table.add(booButton);
        //TODO: save characters unlocked, add them in as needed!
//        table.add(buttonPlay2);
//        table.add(exitButton);
//        table.debug();  //remove later
        stage.addActor(table);
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
