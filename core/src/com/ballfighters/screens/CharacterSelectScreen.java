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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ballfighters.global.GameData;
import com.ballfighters.tween.SpriteAccessor;


public class CharacterSelectScreen implements Screen {

    private Stage stage;
    private Sprite characterSelectLabel1, characterSelectLabel2;
    private SpriteBatch batch;
    private TweenManager tweenManager;

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Table.drawDebug(stage);

        stage.act(delta);
        stage.draw();
        batch.begin();
        characterSelectLabel1.draw(batch);
        characterSelectLabel2.draw(batch);
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
        characterSelectLabel1 = new Sprite(menuTexture);
        characterSelectLabel1.setSize(Gdx.graphics.getWidth()/9,Gdx.graphics.getHeight()/2);
        characterSelectLabel1.setPosition(0, Gdx.graphics.getHeight() - characterSelectLabel1.getHeight() - 10f);
        characterSelectLabel2 = new Sprite(new Texture(Gdx.files.internal("Labels/selectFighter.png")));
        characterSelectLabel2.setSize(Gdx.graphics.getWidth()/9,Gdx.graphics.getHeight()/2);
        characterSelectLabel2.setPosition(Gdx.graphics.getWidth()-characterSelectLabel2.getWidth(), Gdx.graphics.getHeight() - characterSelectLabel2.getHeight() - 10f);

        GameData.BLACKSCREEN.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
    }

    protected void initializeStage() {

        Table table;
        TextureAtlas textureAtlasBoo, textureAtlasSwordGuy, textureAtlasLaserGuy;
        Button booButton, swordGuyButton, laserGuyButton;
        Skin booSkin, swordGuySkin, laserGuySkin;

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        Skin labelSkin = new Skin(Gdx.files.internal("Labels/Fonts/uiskin.json"));
        labelSkin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        labelSkin.getFont("default-font").setMarkupEnabled(true);
        labelSkin.getFont("default-font").setScale(2.5f);


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
        Label ghostLabel = new Label("Ghost Guy\t",labelSkin);
        ghostLabel.setAlignment(Align.bottom | Align.left);



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
        Label swordLabel = new Label("Sword Guy",labelSkin);
        swordLabel.setAlignment(Align.bottom | Align.left);



        textureAtlasLaserGuy = new TextureAtlas(Gdx.files.internal("Buttons/LaserGuy.pack"));
        laserGuySkin = new Skin(textureAtlasLaserGuy );

        Button.ButtonStyle style3 = new Button.ButtonStyle();
        style3.up = laserGuySkin.getDrawable("LaserGuyUp");
        style3.down = laserGuySkin.getDrawable("LaserGuyDown");

        laserGuyButton = new Button(style3);
        laserGuyButton.pad(Gdx.graphics.getWidth()/25);
        laserGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.LASER_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });
        Label laserLabel = new Label("Laser Guy",labelSkin);
        laserLabel.setAlignment(Align.bottom | Align.left);




        TextureAtlas deathGuyTextureAtlas;
        Skin deathGuySkin;
        Button deathGuyButton;
        Button.ButtonStyle deathGuyButtonStyle;



        deathGuyTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/DeathGuyButton.pack"));
        deathGuySkin = new Skin(deathGuyTextureAtlas );

        deathGuyButtonStyle = new Button.ButtonStyle();
        deathGuyButtonStyle.up = deathGuySkin.getDrawable("DeathGuyUp");
        deathGuyButtonStyle.down = deathGuySkin.getDrawable("DeathGuyDown");

        deathGuyButton = new Button(deathGuyButtonStyle);
        deathGuyButton.pad(Gdx.graphics.getWidth()/25);
        deathGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.DEATH_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });
        Label deathLabel = new Label("Reaper Guy",labelSkin);
        deathLabel.setAlignment(Align.bottom | Align.left);



        TextureAtlas bombGuyTextureAtlas;
        Skin bombGuySkin;
        Button bombGuyButton;
        Button.ButtonStyle bombGuyButtonStyle;

        bombGuyTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/BombGuyButton.pack"));
        bombGuySkin = new Skin(bombGuyTextureAtlas );

        bombGuyButtonStyle = new Button.ButtonStyle();
        bombGuyButtonStyle.up = bombGuySkin.getDrawable("BombGuyUp");
        bombGuyButtonStyle.down = bombGuySkin.getDrawable("BombGuyDown");

        bombGuyButton = new Button(bombGuyButtonStyle);
        bombGuyButton.pad(Gdx.graphics.getWidth()/25);
        bombGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.BOMB_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });
        Label bombLabel = new Label("Bomb Guy",labelSkin);
        bombLabel.setAlignment(Align.bottom | Align.left);


        TextureAtlas plasmaGuyTextureAtlas;
        Skin plasmaGuySkin;
        Button plasmaGuyButton;
        Button.ButtonStyle plasmaGuyButtonStyle;

        plasmaGuyTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/PlasmaGuyButton.pack"));
        plasmaGuySkin = new Skin(plasmaGuyTextureAtlas );

        plasmaGuyButtonStyle = new Button.ButtonStyle();
        plasmaGuyButtonStyle.up = plasmaGuySkin.getDrawable("PlasmaGuyUp");
        plasmaGuyButtonStyle.down = plasmaGuySkin.getDrawable("PlasmaGuyDown");

        plasmaGuyButton = new Button(plasmaGuyButtonStyle);
        plasmaGuyButton.pad(Gdx.graphics.getWidth()/25);
        plasmaGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.PLASMA_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });
        Label plasmaLabel = new Label("Plasma Guy",labelSkin);
        plasmaLabel.setAlignment(Align.bottom | Align.left);


        TextureAtlas lightningGuyTextureAtlas;
        Skin lightningGuySkin;
        Button lightningGuyButton;
        Button.ButtonStyle lightningGuyButtonStyle;

        lightningGuyTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/LightningGuyButton.pack"));
        lightningGuySkin = new Skin(lightningGuyTextureAtlas );

        lightningGuyButtonStyle = new Button.ButtonStyle();
        lightningGuyButtonStyle.up = lightningGuySkin.getDrawable("LightningGuyUp");
        lightningGuyButtonStyle.down = lightningGuySkin.getDrawable("LightningGuyDown");

        lightningGuyButton = new Button(lightningGuyButtonStyle);
        lightningGuyButton.pad(Gdx.graphics.getWidth()/25);
        lightningGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.LIGHTNING_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });
        Label lightningLabel = new Label("Lightning Guy",labelSkin);
        lightningLabel.setAlignment(Align.bottom | Align.left);


        TextureAtlas fireGuyTextureAtlas;
        Skin fireGuySkin;
        Button fireGuyButton;
        Button.ButtonStyle fireGuyButtonStyle;

        fireGuyTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/FireGuyButton.pack"));
        fireGuySkin = new Skin(fireGuyTextureAtlas );

        fireGuyButtonStyle = new Button.ButtonStyle();
        fireGuyButtonStyle.up = fireGuySkin.getDrawable("FireGuyUp");
        fireGuyButtonStyle.down = fireGuySkin.getDrawable("FireGuyDown");

        fireGuyButton = new Button(fireGuyButtonStyle);
        fireGuyButton.pad(Gdx.graphics.getWidth()/25);
        fireGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.FIRE_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });
        Label fireLabel = new Label("Fire Guy",labelSkin);
        fireLabel.setAlignment(Align.bottom | Align.left);


        TextureAtlas plantGuyTextureAtlas;
        Skin plantGuySkin;
        Button plantGuyButton;
        Button.ButtonStyle plantGuyButtonStyle;

        plantGuyTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/PlantGuyButton.pack"));
        plantGuySkin = new Skin(plantGuyTextureAtlas );

        plantGuyButtonStyle = new Button.ButtonStyle();
        plantGuyButtonStyle.up = plantGuySkin.getDrawable("PlantGuyUp");
        plantGuyButtonStyle.down = plantGuySkin.getDrawable("PlantGuyDown");

        plantGuyButton = new Button(plantGuyButtonStyle);
        plantGuyButton.pad(Gdx.graphics.getWidth()/25);
        plantGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.PLANT_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });
        Label plantLabel = new Label("Plant Guy",labelSkin);
        plantLabel.setAlignment(Align.bottom | Align.left);


        TextureAtlas nerdGuyTextureAtlas;
        Skin nerdGuySkin;
        Button nerdGuyButton;
        Button.ButtonStyle nerdGuyButtonStyle;

        nerdGuyTextureAtlas = new TextureAtlas(Gdx.files.internal("Buttons/NerdGuyButton.pack"));
        nerdGuySkin = new Skin(nerdGuyTextureAtlas );

        nerdGuyButtonStyle = new Button.ButtonStyle();
        nerdGuyButtonStyle.up = nerdGuySkin.getDrawable("NerdGuyUp");
        nerdGuyButtonStyle.down = nerdGuySkin.getDrawable("NerdGuyDown");

        nerdGuyButton = new Button(nerdGuyButtonStyle);
        nerdGuyButton.pad(Gdx.graphics.getWidth()/25);
        nerdGuyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
                        GameData.PLAYER_CHOICE = GameData.NERD_GUY;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new TestBattleScreen());
                    }
                }).start(tweenManager);
            }
        });
        Label nerdLabel = new Label("Nerd Guy",labelSkin);
        nerdLabel.setAlignment(Align.bottom | Align.left);



        table.add(booButton);
        table.add(ghostLabel);

        table.add(plasmaGuyButton);
        table.add(plasmaLabel).row();

        table.add(swordGuyButton);
        table.add(swordLabel);

        table.add(lightningGuyButton);
        table.add(lightningLabel).row();

        table.add(laserGuyButton);
        table.add(laserLabel);

        table.add(fireGuyButton);
        table.add(fireLabel).row();

        table.add(bombGuyButton);
        table.add(bombLabel);
        table.add(plantGuyButton);
        table.add(plantLabel).row();

        table.add(deathGuyButton);
        table.add(deathLabel);

        table.add(nerdGuyButton);
        table.add(nerdLabel).row();


        //TODO: save characters unlocked, add them in as needed!
        stage.addActor(table);
    }

}
