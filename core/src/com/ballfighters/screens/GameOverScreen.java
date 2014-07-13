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

/**
 * Created by Dell_Owner on 7/13/2014.
 */
public class GameOverScreen implements Screen {

    private Stage stage;
    private Sprite gameOverLabel;
    private SpriteBatch batch;
    private TweenManager tweenManager;
    private Screen continueScreen;

    public GameOverScreen(Screen screen){
       super();
        this.continueScreen = screen;
    }


    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Table.drawDebug(stage);
        batch.begin();
        gameOverLabel.draw(batch);
        GameData.BLACKSCREEN.draw(batch);
        batch.end();

        stage.draw();
        stage.act(delta);

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

        gameOverLabel= new Sprite(new Texture(Gdx.files.internal("Backgrounds/GameOverBackground.png")));
        gameOverLabel.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        gameOverLabel.setPosition(Gdx.graphics.getWidth()/2-gameOverLabel.getWidth()/2,10f);

        GameData.BLACKSCREEN.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
    }

    protected void initializeStage() {

        Table table;
        TextureAtlas newGameButtonTextureAtlas, continueButtonTextureAtlas, exitButtonAtlas;
        Button newGameButton, continueButton, exitButton;
        Skin newGameButtonSkin, continueButtonSkin, exitButtonSkin;

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2);

        newGameButtonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/NewGameButton.pack"));
        newGameButtonSkin = new Skin(newGameButtonTextureAtlas);

        Button.ButtonStyle style1 = new Button.ButtonStyle();
        style1.up = newGameButtonSkin.getDrawable("NewGameUp");
        style1.down = newGameButtonSkin.getDrawable("NewGameDown");

        newGameButton = new Button(style1);
        newGameButton.pad(Gdx.graphics.getHeight()/25);
        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new CharacterSelectScreen());
                    }
                }).start(tweenManager);
            }
        });

        continueButtonTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/ContinueButton.pack"));
        continueButtonSkin = new Skin(continueButtonTextureAtlas);

        Button.ButtonStyle style2 = new Button.ButtonStyle();
        style2.up = continueButtonSkin.getDrawable("ContinueButtonUp");
        style2.down = continueButtonSkin.getDrawable("ContinueButtonDown");

        continueButton = new Button(style2);
        continueButton.pad(Gdx.graphics.getHeight()/25);
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        if (continueScreen instanceof TestBattleScreen) {//todo: add in all screens
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                        }
                    }
                }).start(tweenManager);
            }
        });

        exitButtonAtlas = new TextureAtlas(Gdx.files.internal("Buttons/ExitButton.pack"));
        exitButtonSkin = new Skin(exitButtonAtlas);

        Button.ButtonStyle style3 = new Button.ButtonStyle();
        style3.up = exitButtonSkin.getDrawable("ExitUp");
        style3.down = exitButtonSkin.getDrawable("ExitDown");

        exitButton = new Button(style3);
        exitButton.pad(Gdx.graphics.getHeight()/25);
        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        Gdx.app.exit();
                    }
                }).start(tweenManager);
            }
        });


        table.add(newGameButton);
        table.row();

        table.row();
        table.add(continueButton);
        table.row();
        table.row();
        table.add(exitButton);



        stage.addActor(table);
    }

}
