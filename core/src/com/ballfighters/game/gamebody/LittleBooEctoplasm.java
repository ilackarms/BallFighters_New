package com.ballfighters.game.gamebody;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/27/2014.
 */
public class LittleBooEctoplasm  extends Bullet {


    public LittleBooEctoplasm(GameBody parent, Vector2 position, Vector2 velocity) {

        this.parent = parent;
        spriteWidth=12f;
        spriteHeight=12f;
        duration = 1.5f;
        damage = 4;
        float SPEED = 50000f;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        body.setLinearVelocity(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED);
        body.applyForceToCenter(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED, true);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        animator = new Animator("Sprites/ectoplasm.png", 1, 7, 0.125f);

        //DESTROY AFTER 5 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);

    }

    public Body initializeBody(){

        float radius = 5.0f;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Shape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;

        body = GameData.WORLD.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    public UserDataBundle createUserDataBundle() {
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.destroyOnCollision = true;
        returnDataBundle.damage = damage;
        returnDataBundle.rotatable = true;
        returnDataBundle.baseObject = this;
        returnDataBundle.flaggedForDeletion = false;
        returnDataBundle.rotatable = true;
        returnDataBundle.isPlayer = false;
        body.setUserData(returnDataBundle);
        return returnDataBundle;
    }

    @Override
    public void update(){
        animate();
        dataBundle.sprite = sprite;
        body.setUserData(dataBundle);
    }

    @Override
    public void getHit(Bullet bullet){
        kill();
    }

    @Override
    public void kill(){
//        final Vector2 explosionlocation = new Vector2(this.body.getPosition());
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/BombGuySounds/BombGuyExplosion.wav"));
//                long soundID = fireSound.play();
//                fireSound.setVolume(soundID, GameData.VOLUME);
//                fireSound.dispose();
//                new BombGuyExplosion(parent, explosionlocation);
//            }
//        },0.01f);

        super.kill();
    }
}
