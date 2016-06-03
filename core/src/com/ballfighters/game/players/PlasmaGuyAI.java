package com.ballfighters.game.players;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.ai.AIHandlerLaserGuy;
import com.ballfighters.game.gamebody.*;
import com.ballfighters.global.AnimationPackage;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;
import com.ballfighters.screens.ContinueScreen;
import com.ballfighters.screens.TestBattleScreen6;
import com.ballfighters.screens.TestBattleScreen7;
import com.ballfighters.tween.BallTween;
import com.ballfighters.tween.SpriteAccessor;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 7/23/2014.
 */
public class PlasmaGuyAI extends Player {


    protected float radius = 5.5f;
    protected float density = 5f;
    protected float restitution = 0.5f;
    public static final int MAX_HEALTH = 100;

    protected AIHandlerLaserGuy aiInputHandler;

    protected BallTween tween;

    public PlasmaGuyAI(Vector2 position) {

        name = "Plasma Guy";

        this.position = position;
        animator = new Animator("Sprites/plasmaGuy.png", 4, 4);
        inputDirection = new Vector2(0,0);
        body = createBody();
        health = MAX_HEALTH;
        spriteHeight = 18f;
        spriteWidth = 13f;
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0,0,0);
        ACCELERATION = 3000f;
        PERMANENT_ACCELERATION = 3000f;
        tweenList = new ArrayList<BallTween>();

        aiInputHandler = new AIHandlerLaserGuy(this);

        tween = null;
        lastPosition = body.getPosition();

    }

    protected UserDataBundle createUserDataBundle(){
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.baseObject = this;
        returnDataBundle.flaggedForDeletion = false;
        returnDataBundle.health = health;
        returnDataBundle.rotatable = false;
        returnDataBundle.isPlayer = true;
        return returnDataBundle;
    }

    protected Body createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        body = GameData.WORLD.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);
        shape.dispose();
        body.setUserData(dataBundle);
        body.setTransform(position,0);
        return body;
    }

    @Override
    public void update(){
        super.update();
        aiInputHandler.updateDirections();
        animate();
        if(tween!=null){
            tween.update(sprite);
        }
        inputDirection = aiInputHandler.inputDirection;
        clickPosition.x = aiInputHandler.targetDirection.x;
        clickPosition.y = aiInputHandler.targetDirection.y;
        move(inputDirection);
        fireShots();
    }

    @Override
    public void move(Vector2 inputDirection){
        if(inputDirection.len()!=0){
            inputDirection.x =  inputDirection.x * ACCELERATION;
            inputDirection.y = inputDirection.y * ACCELERATION;
            body.applyForceToCenter(inputDirection, true);
        }
    }

    Boolean fireShotOnCoolDown = false;
    @Override
    public void fireShots(){
        if(health>0 && !dataBundle.flaggedForDeletion) {
            if (!fireShotOnCoolDown) {
                Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PlasmaGuySounds/plasmaShot.wav"));
                long soundID = fireSound.play();
                fireSound.setVolume(soundID, GameData.VOLUME);
                fireSound.dispose();

                Vector2 shot1Position = new Vector2(this.body.getPosition().x + radius * 2 * MyMathStuff.toUnit(clickPosition).x,
                        this.body.getPosition().y + radius * 2 * MyMathStuff.toUnit(clickPosition).y);
                Vector2 shot1Velocity = new Vector2(clickPosition.x, clickPosition.y);
                new PlasmaGuyProjectile(this, shot1Position, shot1Velocity, true);

                tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.YELLOW, 1.2f).yoyo(1);

                fireShotOnCoolDown = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        fireShotOnCoolDown = false;
                    }
                }, 0.25f);
            }
        }
    }

    Boolean shieldOnCoolDown = false;
    @Override
    public void shield(){
        if(health>0) {
            if (!shieldOnCoolDown) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        animator = new Animator("Sprites/plasmaGuyTeleport.png", 1, 6, 0.125f);
                    }
                },0.125f);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Vector3 transform = GameData.camera.unproject(new Vector3(MathUtils.random(40, Gdx.graphics.getWidth() - 40), MathUtils.random(40,Gdx.graphics.getHeight()-40), 0));
                        if(transform.len()==0) transform = new Vector3(0,0,0);
                        body.setTransform(MyMathStuff.convertTo2D(transform), 0);
                    }
                },0.5f);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        animator = new Animator("Sprites/plasmaGuy.png", 4, 4);
                    }
                }, 0.625f);

                Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PlasmaGuySounds/teleport.wav"));
                long soundID = fireSound.play();
                fireSound.setVolume(soundID, GameData.VOLUME);
                fireSound.dispose();

                tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.BLUE, 1.2f).yoyo(1);

                fireShotOnCoolDown = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        fireShotOnCoolDown = false;
                    }
                }, 3.5f);
            }
        }
    }

    Boolean getHitSoundCoolDown = false;
    @Override
    public void getHit(Bullet bullet){
        if(!getHitSoundCoolDown) {
            Gdx.input.vibrate(10);
            tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.RED, 2.2f).yoyo(1);
            Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooHit.wav"));
            long soundID = hitSound.play();
            hitSound.setVolume(soundID, GameData.VOLUME);
            hitSound.dispose();
            getHitSoundCoolDown = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    getHitSoundCoolDown = false;
                }
            }, 0.5f);
        }

        health -= bullet.damage;
        float ratio = (float) health / (float) MAX_HEALTH;
        if(ratio<0) ratio = 0;

        GameData.PLAYER_2_HEALTH_BAR.setSize(Gdx.graphics.getWidth() / 6 * ratio, GameData.PLAYER_1_HEALTH_BAR.getHeight());
        if (health <= 0) {
            kill();
        }
    }

    @Override
    public void kill(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                new CollisionlessSprite(lastPosition, new Animator("Sprites/PlasmaGuyDeath.png",1,20,0.5f),12f,spriteWidth,spriteHeight);
            }
        }, 0.01f);
        super.kill();
        Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooDeath.wav"));
        long soundID = deathSound.play();
        deathSound .setVolume(soundID, GameData.VOLUME);
        deathSound.dispose();
        lastPosition = body.getPosition();


        final TestBattleScreen6 battleScreen = (TestBattleScreen6) GameData.screen;
        Gdx.input.vibrate(100);
        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).delay(2f).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(4).start(battleScreen.tweenManager);
                GameData.screen.dispose();
                GameData.screen.hide();
                GameData.staticAnimations = new ArrayList<AnimationPackage>();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ContinueScreen(new TestBattleScreen7(), name));
            }
        }).start(battleScreen.tweenManager);

    }

}

