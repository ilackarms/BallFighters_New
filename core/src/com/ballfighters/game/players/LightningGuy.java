package com.ballfighters.game.players;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.gamebody.*;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;
import com.ballfighters.tween.BallTween;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 7/23/2014.
 */
public class LightningGuy extends Player {

   protected InputHandler inputHandler;
    public static final int MAX_HEALTH = 140;


    protected BallTween tween;


    public LightningGuy(Vector2 position) {

        name = "Lightning Guy";
        isAI = false;

        this.position = position;
        animator = new Animator("Sprites/LightningGuy.png", 4, 4);
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

                Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LightningGuySounds/LightningGuyProjectile.wav"));
                long soundID = fireSound.play();
                fireSound.setVolume(soundID, GameData.VOLUME);
                fireSound.dispose();

                Vector2 shot1Position = new Vector2(this.body.getPosition().x + radius * 2 * MyMathStuff.toUnit(clickPosition).x,
                        this.body.getPosition().y + radius * 2 * MyMathStuff.toUnit(clickPosition).y);
                Vector2 shot1Velocity = new Vector2(clickPosition.x, clickPosition.y);
                new LightningGuyProjectile(this, shot1Position, shot1Velocity, false);


                Vector2 rotatedClick = MyMathStuff.convertTo2D(clickPosition).rotate(60);
                shot1Position = new Vector2(this.body.getPosition().x + radius * 2 * MyMathStuff.toUnit(rotatedClick).x,
                        this.body.getPosition().y + radius * 2 * MyMathStuff.toUnit(rotatedClick).y);
                shot1Velocity = new Vector2(rotatedClick.x, rotatedClick.y);
                new LightningGuyProjectile(this, shot1Position, shot1Velocity, false);

                rotatedClick = rotatedClick.rotate(60);
                shot1Position = new Vector2(this.body.getPosition().x + radius * 2 * MyMathStuff.toUnit(rotatedClick).x,
                        this.body.getPosition().y + radius * 2 * MyMathStuff.toUnit(rotatedClick).y);
                shot1Velocity = new Vector2(rotatedClick.x, rotatedClick.y);
                new LightningGuyProjectile(this, shot1Position, shot1Velocity, false);

                rotatedClick = rotatedClick.rotate(60);
                shot1Position = new Vector2(this.body.getPosition().x + radius * 2 * MyMathStuff.toUnit(rotatedClick).x,
                        this.body.getPosition().y + radius * 2 * MyMathStuff.toUnit(rotatedClick).y);
                shot1Velocity = new Vector2(rotatedClick.x, rotatedClick.y);
                new LightningGuyProjectile(this, shot1Position, shot1Velocity, false);

                rotatedClick = rotatedClick.rotate(60);
                shot1Position = new Vector2(this.body.getPosition().x + radius * 2 * MyMathStuff.toUnit(rotatedClick).x,
                        this.body.getPosition().y + radius * 2 * MyMathStuff.toUnit(rotatedClick).y);
                shot1Velocity = new Vector2(rotatedClick.x, rotatedClick.y);
                new LightningGuyProjectile(this, shot1Position, shot1Velocity, false);

                rotatedClick = rotatedClick.rotate(60);
                shot1Position = new Vector2(this.body.getPosition().x + radius * 2 * MyMathStuff.toUnit(rotatedClick).x,
                        this.body.getPosition().y + radius * 2 * MyMathStuff.toUnit(rotatedClick).y);
                shot1Velocity = new Vector2(rotatedClick.x, rotatedClick.y);
                new LightningGuyProjectile(this, shot1Position, shot1Velocity, false);

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
            if(health>0) {
                new LaserGuyShield(this);

                Gdx.input.vibrate(50);
                Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LaserGuySounds/LaserGuyShield.wav"));
                long soundID = fireSound.play();
                fireSound.setVolume(soundID, GameData.VOLUME);
                fireSound.dispose();

                tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.BLUE, 1.2f).yoyo(1);
            }
        }
    }

    Boolean getHitSoundCoolDown = false;
    @Override
    public void getHit(Bullet bullet){
        if(!getHitSoundCoolDown) {
            Gdx.input.vibrate(100);
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
        if(ratio<0) ratio = 0;

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
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                new CollisionlessSprite(lastPosition, new Animator("Sprites/LightningGuyDeath.png",1,23,0.25f),12f,spriteWidth,spriteHeight);
            }
        }, 0.01f);
        super.kill();
        GameData.playMusic("Music/GameOver.mp3");
        Gdx.input.vibrate(1000);
        Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooDeath.wav"));
        long soundID = deathSound.play();
        deathSound .setVolume(soundID, GameData.VOLUME);
        deathSound.dispose();
        lastPosition.x = MyMathStuff.convertTo3D(body.getPosition()).x;
        lastPosition.y = MyMathStuff.convertTo3D(body.getPosition()).y;

    }




}
