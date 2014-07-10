package com.ballfighters.game.players;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ballfighters.game.gamebody.*;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;
import com.ballfighters.tween.BallTween;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class LittleBoo extends Player {


    protected InputHandler inputHandler;
    protected float radius = 6f;
    protected float density = 0.05f;
    protected float restitution = 0.5f;
    public static final int MAX_HEALTH = 100;

    Sprite healthBarBox, healthBar;

    protected BallTween tween;

    public LittleBoo(Vector2 position) {


    	this.position = position;
        animator = new Animator("Sprites/littleboo.png", 4, 4);
        inputDirection = new Vector2(0,0);
        body = createBody();
        health = 100;
        spriteHeight = 13f;
        spriteWidth = 13f;
        dataBundle = createUserDataBundle();
        inputHandler = new InputHandler(this);
        GestureDetector gestureDetector =new GestureDetector(new InputHandler(this));
        gestureDetector.setLongPressSeconds(0.5f);
        Gdx.input.setInputProcessor(gestureDetector);
        clickPosition = new Vector3(0,0,0);
        ACCELERATION = 300f;
        tweenList = new ArrayList<BallTween>();

        tween = null;
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
    public void fireShots(){
        Gdx.input.vibrate(15);
        int rand = MathUtils.random(5);
        Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/littleBooProjectile" + rand + ".wav"));
        long soundID = fireSound.play();
        fireSound.setVolume(soundID, GameData.VOLUME);
        Vector2 shot1Position = new Vector2(this.body.getPosition().x+radius*2*MyMathStuff.toUnit(clickPosition).x,
                this.body.getPosition().y+radius*2*MyMathStuff.toUnit(clickPosition).y);
        Vector2 shot1Velocity = new Vector2(clickPosition.x,clickPosition.y);
        new LittleBooProjectile(this, shot1Position,shot1Velocity);
        fireSound.dispose();

        tween = new BallTween(animator,BallTween.COLOR,BallTween.Colors.YELLOW,1.2f).yoyo(1);
    }

    @Override
    public void shield(){
        Vector2 shieldDisplacement = MyMathStuff.toUnit(new Vector2(clickPosition.x,clickPosition.y));
        shieldDisplacement.x*=10;
        shieldDisplacement.y*=10;
        Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooShield.wav"));
        long soundID = fireSound.play();
        fireSound.setVolume(soundID, GameData.VOLUME);
        float angle = ((float) Math.atan2(shieldDisplacement.y,shieldDisplacement.x));
        new LittleBooShield(this, shieldDisplacement, angle);
        fireSound.dispose();

        tween = new BallTween(animator,BallTween.COLOR,BallTween.Colors.BLUE,1.2f).yoyo(1);
    }

    @Override
    public void getHit(Bullet bullet){
        Gdx.input.vibrate(1000);
        tween = new BallTween(animator,BallTween.COLOR,BallTween.Colors.RED,2.2f).yoyo(1);
        Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooShield.wav"));
        long soundID = hitSound .play();
        hitSound .setVolume(soundID, GameData.VOLUME);
        hitSound.dispose();

        health-=bullet.damage;
        float ratio = (float) health/ (float) MAX_HEALTH;
        System.out.println(health+"/"+MAX_HEALTH+"="+ratio);
        GameData.PLAYER_1_HEALTH_BAR.setSize(Gdx.graphics.getWidth()/6*ratio,GameData.PLAYER_1_HEALTH_BAR.getHeight());
        if(health<0){
            Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooShield.wav"));
            soundID = deathSound.play();
            deathSound .setVolume(soundID, GameData.VOLUME);
            deathSound.dispose();
            kill();
        }
    }

}
