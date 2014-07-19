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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ballfighters.global.GameData;
import com.ballfighters.tween.SpriteAccessor;


public class CharacterSelectScreen implements Screen {

    private Stage stage;
    private Sprite characterSelectLabel;
    private SpriteBatch batch;
    private TweenManager tweenManager;

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Table.drawDebug(stage);

        stage.act(delta);
        stage.draw();
        batch.begin();
        characterSelectLabel.draw(batch);
        GameData.BLACKSCREEN.draw(batch);
        batch.end();

        tweenManager.update(delta);
    }

    public void resize(int i, int i1){

    }

    public void show(){
    	GameData.screen = this;

        initializeSprites();
        initializeStage();

        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.set(GameData.BLACKSCREEN,SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(0).start(tweenManager);
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
        stage.dispose();

    }

    protected void initializeSprites(){

        Texture menuTexture = new Texture(Gdx.files.internal("Labels/selectPlayer.png"));
        characterSelectLabel= new Sprite(menuTexture);
        characterSelectLabel.setSize(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/4);
        characterSelectLabel.setPosition(Gdx.graphics.getWidth()/2-characterSelectLabel.getWidth()/2,Gdx.graphics.getHeight()-characterSelectLabel.getHeight()-10f);

        GameData.BLACKSCREEN.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
    }

    protected void initializeStage() {

        Table table;
        TextureAtlas textureAtlasBoo, textureAtlasSwordGuy;
        Button booButton, swordGuyButton;
        Skin booSkin, swordGuySkin;

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        textureAtlasBoo = new TextureAtlas(Gdx.files.internal("Buttons/BooKnockoff.pack"));
        booSkin = new Skin(textureAtlasBoo);

        Button.ButtonStyle style1 = new Button.ButtonStyle();
        style1.up = booSkin.getDrawable("BooUp");
        style1.down = booSkin.getDrawable("BooDown");

        booButton = new Button(style1);
        booButton.pad(Gdx.graphics.getWidth()/25);
        booButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.LITTLE_BOO;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });


        textureAtlasSwordGuy = new TextureAtlas(Gdx.files.internal("Buttons/SwordGuyButton.pack"));
        swordGuySkin = new Skin(textureAtlasSwordGuy);

        Button.ButtonStyle style2 = new Button.ButtonStyle();
        style2.up = swordGuySkin.getDrawable("SwordGuyUp");
        style2.down = swordGuySkin.getDrawable("SwordGuyDown");

        swordGuyButton = new Button(style2);
        swordGuyButton.pad(Gdx.graphics.getWidth()/25);
        swordGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.SWORD_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });


        table.add(booButton).spaceRight(20f);
        table.add(swordGuyButton);
        //TODO: save characters unlocked, add them in as needed!
        stage.addActor(table);
    }

}
