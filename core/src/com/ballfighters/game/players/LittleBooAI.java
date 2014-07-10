package com.ballfighters.game.players;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.gamebody.AIInputHandler;
import com.ballfighters.game.gamebody.Animator;
import com.ballfighters.game.gamebody.LittleBooProjectile;
import com.ballfighters.game.gamebody.UserDataBundle;
import com.ballfighters.game.players.LittleBoo;
import com.ballfighters.game.players.Player;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;
import com.ballfighters.tween.BallTween;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 7/6/2014.
 */
public class LittleBooAI extends Player {

    protected float radius = 6f;
    protected float density = 0.05f;
    protected float restitution = 0.5f;

    protected Boolean fireShotsOnCooldown;

    protected AIInputHandler aiInputHandler;
    protected BallTween tween;

    public LittleBooAI(Vector2 position) {
        this.position = position;
        animator = new Animator("Sprites/playerSpriteSheet.png", 4, 4);
        body = createBody();
        health = 100;
        spriteHeight = 14f;
        spriteWidth = 14f;
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0,0,0);
        ACCELERATION = 200f;
        tweenList = new ArrayList<BallTween>();

        tween = null;
        aiInputHandler = new AIInputHandler(this);//TODO
        fireShotsOnCooldown = false;
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
        if(!fireShotsOnCooldown) {
            fireShotsOnCooldown = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    fireShotsOnCooldown = false;
                }
            }, 0.15f);
            fireShots();
        }
    }

    @Override
    public void move(Vector2 inputDirection){
        if(inputDirection.len()!=0){
            inputDirection.x =  inputDirection.x * ACCELERATION;
            inputDirection.y = inputDirection.y * ACCELERATION;
            body.applyForceToCenter(inputDirection, true);
        }
    }

    @Override
    public void fireShots(){
        if (aiInputHandler.targetDirection.len()!=0) {
            int rand = MathUtils.random(5);
            Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/littleBooProjectile" + rand + ".wav"));
            long soundID = fireSound.play();
            fireSound.setVolume(soundID, GameData.VOLUME);
            Vector2 shot1Position  = new Vector2(this.body.getPosition().x+radius*2*MyMathStuff.toUnit(aiInputHandler.targetDirection).x,
                    this.body.getPosition().y+radius*2*MyMathStuff.toUnit(aiInputHandler.targetDirection).y);
            Vector2 shot1Velocity = aiInputHandler.targetDirection;
            LittleBooProjectile boo = new LittleBooProjectile(this, shot1Position, shot1Velocity,"Sprites/LittleBooAIProjectile.png");
            fireSound.dispose();

            tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.YELLOW, 1.2f).yoyo(1);
        }
    }

}
