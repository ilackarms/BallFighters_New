package com.ballfighters.game.players;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.gamebody.*;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;
import com.ballfighters.tween.BallTween;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 7/15/2014.
 */
public class SwordGuy extends Player {


    protected InputHandler inputHandler;
    protected float radius = 5.5f;
    protected float density = 5f;
    protected float restitution = 0.5f;
    public static final int MAX_HEALTH = 150;

    public SwordGuy(Vector2 position) {

        name = "Sword Guy";
        isAI = false;

        this.position = position;
        animator = new Animator("Sprites/SwordGuy.png", 4, 4);
        inputDirection = new Vector2(0,0);
        body = createBody();
        health = MAX_HEALTH;
        spriteHeight = 13f;
        spriteWidth = 13f;
        dataBundle = createUserDataBundle();
        inputHandler = new InputHandler(this);
        GestureDetector gestureDetector =new GestureDetector(new InputHandler(this));
        gestureDetector.setLongPressSeconds(0.5f);
        Gdx.input.setInputProcessor(gestureDetector);
        clickPosition = new Vector3(0,0,0);
        ACCELERATION = 60000f;
        PERMANENT_ACCELERATION = 60000f;
        tweenList = new ArrayList<BallTween>();

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
        inputDirection.x = 0;
        inputDirection.y = 0;
        inputHandler.directionHandler();
        animate();
        if (tween != null) {
            tween.update(sprite);
        }
        if(dataBundle.ghostMode){
            sprite.setColor(sprite.getColor().r,sprite.getColor().g,sprite.getColor().b,0.5f);
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

    Boolean fireShotOnCoolDown = false;
    @Override
    public void fireShots(){
        if(health>0) {
            if (!fireShotOnCoolDown) {
                Gdx.input.vibrate(15);

                int rand = MathUtils.random(1, 3);
                Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/SwordGuySounds/SwordProjectile" + rand + ".wav"));
                long soundID = fireSound.play();
                fireSound.setVolume(soundID, GameData.VOLUME);
                fireSound.dispose();

                Vector2 swordDisplacement = MyMathStuff.toUnit(new Vector2(clickPosition.x, clickPosition.y));
                swordDisplacement.x *= 10;
                swordDisplacement.y *= 10;
                int direction;
                if (swordDisplacement.x >= 0) {
                    direction = 1;
                } else {
                    direction = -1;
                }
                swordDisplacement.rotate(60 * direction);
                float angle = ((float) Math.atan2(-1 * swordDisplacement.x, swordDisplacement.y));
                new SwordProjectile(this, swordDisplacement, angle);

                tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.YELLOW, 1.2f).yoyo(1);

                fireShotOnCoolDown = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        fireShotOnCoolDown = false;
                    }
                }, 0.01f);
            }
        }
    }

    @Override
    public void shield(){
        if(health>0) {
            Vector2 shieldDisplacement = MyMathStuff.toUnit(new Vector2(clickPosition.x, clickPosition.y));
            shieldDisplacement.x *= 10;
            shieldDisplacement.y *= 10;
            float angle = ((float) Math.atan2(shieldDisplacement.y, shieldDisplacement.x));
            new SwordGuyShield(this, shieldDisplacement, angle);

            Gdx.input.vibrate(50);
            Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/SwordGuySounds/Shield.wav"));
            long soundID = fireSound.play();
            fireSound.setVolume(soundID, GameData.VOLUME);
            fireSound.dispose();

            tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.BLUE, 1.2f).yoyo(1);
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

        GameData.PLAYER_1_HEALTH_BAR.setSize(Gdx.graphics.getWidth() / 6 * ratio, GameData.PLAYER_1_HEALTH_BAR.getHeight());
        if (health <= 0) {
            kill();
        }
    }

    @Override
    public void kill(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                new CollisionlessSprite(lastPosition, new Animator("Sprites/SwordGuyDeath.png",1,18,0.5f),12f,spriteWidth,spriteHeight);
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
