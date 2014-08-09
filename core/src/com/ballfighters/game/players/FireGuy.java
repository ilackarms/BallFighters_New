package com.ballfighters.game.players;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.gamebody.Animator;
import com.ballfighters.game.gamebody.Bullet;
import com.ballfighters.game.gamebody.InputHandler;
import com.ballfighters.game.gamebody.PlasmaGuyProjectile;
import com.ballfighters.global.AnimationPackage;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;
import com.ballfighters.screens.GameOverScreen;
import com.ballfighters.tween.BallTween;
import com.ballfighters.tween.SpriteAccessor;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 7/23/2014.
 */
public class FireGuy extends Player {

   protected InputHandler inputHandler;
    public static final int MAX_HEALTH = 100;


    protected BallTween tween;


    public FireGuy(Vector2 position) {

        name = "Plasma Guy";

        this.position = position;
        animator = new Animator("Sprites/plasmaGuy.png", 4, 4);
        inputDirection = new Vector2(0,0);

        radius = 5.5f;
        density = 0.5f;
        restitution = 0.5f;

        body = createBody();
        health = MAX_HEALTH;
        spriteHeight = 18f;
        spriteWidth = 13f;
        dataBundle = createUserDataBundle();
        inputHandler = new InputHandler(this);

        GestureDetector gestureDetector =new GestureDetector(new InputHandler(this));
        gestureDetector.setLongPressSeconds(0.5f);
        Gdx.input.setInputProcessor(gestureDetector);
        clickPosition = new Vector3(0,0,0);
        ACCELERATION = 3000f;
        PERMANENT_ACCELERATION = 3000f;
        tweenList = new ArrayList<BallTween>();

        tween = null;
        lastPosition = body.getPosition();
    }

    Boolean fireShotOnCoolDown = false;
    @Override
    public void fireShots(){
        if(health>0) {
            if (!fireShotOnCoolDown) {
                Gdx.input.vibrate(15);

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
                }, 0.1f);
            }
        }
    }


    @Override
    public void update() {
        super.update();
        inputDirection.x = 0;
        inputDirection.y = 0;
        inputHandler.directionHandler();
        animate();
        if (tween != null) {
            tween.update(sprite);
        }
        if (dataBundle.ghostMode) {
            sprite.setColor(sprite.getColor().r, sprite.getColor().g, sprite.getColor().b, 0.5f);
        }
    }

    @Override
    public void shield(){
        if(health>0) {

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    animator = new Animator("Sprites/plasmaGuyTeleport.png", 1, 6, 0.125f);
                }
            },0.125f);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    System.out.println("Position before tele:" +body.getPosition());
                    System.out.println("Mouse position:" +"("+Gdx.input.getX()+","+Gdx.input.getY()+")");
                    System.out.println("Uprojected Mouse position:" +GameData.camera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)));
                    Vector3 transform = GameData.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                    if(transform.len()==0) transform = new Vector3(0,0,0);
                    body.setTransform(MyMathStuff.convertTo2D(transform), 0);
                    System.out.println("Position after tele:" +body.getPosition());
                }
            },0.5f);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    animator = new Animator("Sprites/plasmaGuy.png", 4, 4);
                }
            },0.625f);


            Gdx.input.vibrate(50);
            Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PlasmaGuySounds/teleport.wav"));
            long soundID = fireSound.play();
            fireSound.setVolume(soundID, GameData.VOLUME);
            fireSound.dispose();

            tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.BLUE, 1.2f).yoyo(1);
        }
    }

    @Override
    public void getHit(Bullet bullet){
        Gdx.input.vibrate(100);
        tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.RED, 2.2f).yoyo(1);
        Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooHit.wav"));
        long soundID = hitSound.play();
        hitSound.setVolume(soundID, GameData.VOLUME);
        hitSound.dispose();

        health -= bullet.damage;
        float ratio = (float) health / (float) MAX_HEALTH;
        if(ratio<0) ratio = 0;
        System.out.println(health + "/" + MAX_HEALTH + "=" + ratio);
        GameData.PLAYER_1_HEALTH_BAR.setSize(Gdx.graphics.getWidth() / 6 * ratio, GameData.PLAYER_1_HEALTH_BAR.getHeight());
        if (health < 0) {
            kill();
        }
    }


    @Override
    public void move(Vector2 inputDirection){
        if(inputDirection.len()!=0){
            if(Gdx.app.getType()== Application.ApplicationType.Android) {
                inputDirection.x = inputHandler.inputDirection.x * ACCELERATION;
                inputDirection.y = inputHandler.inputDirection.y * ACCELERATION;
            }
            if(Gdx.app.getType()== Application.ApplicationType.Desktop) {
                inputDirection.x = MyMathStuff.toUnit(inputHandler.inputDirection).x * ACCELERATION;
                inputDirection.y = MyMathStuff.toUnit(inputHandler.inputDirection).y * ACCELERATION;
            }
            body.applyForceToCenter(inputDirection, true);
        }
    }

    @Override
    public void kill(){
        super.kill();
        GameData.playMusic("Music/GameOver.mp3");
        Gdx.input.vibrate(1000);
        Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooDeath.wav"));
        long soundID = deathSound.play();
        deathSound .setVolume(soundID, GameData.VOLUME);
        deathSound.dispose();
        lastPosition.x = MyMathStuff.convertTo3D(body.getPosition()).x;
        lastPosition.y = MyMathStuff.convertTo3D(body.getPosition()).y;

        Animator gameOverAnimation = new Animator("Sprites/GameOverAnimation.png",2,5);
        final AnimationPackage staticGameOverAnimation = new AnimationPackage(gameOverAnimation,64*2,32*2);
        staticGameOverAnimation.play();
        staticGameOverAnimation.position=new Vector2(lastPosition);
        GameData.staticAnimations.add(staticGameOverAnimation);


        //NEW GAME ON DEATH!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(GameData.screen));
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(4).start(GameData.tweenManager);
                        GameData.screen.dispose();
                        GameData.screen.hide();
                        GameData.staticAnimations.remove(staticGameOverAnimation);
                        GameData.staticAnimations = new ArrayList<AnimationPackage>();

                    }
                }).start(GameData.tweenManager);
            }
        }, 8);

    }




}
