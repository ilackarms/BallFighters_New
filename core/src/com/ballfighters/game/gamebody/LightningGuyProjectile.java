package com.ballfighters.game.gamebody;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/27/2014.
 */
public class LightningGuyProjectile extends Bullet {

    LightningGuyProjectile self;
    Boolean reboundShot;
    float radius;
    Vector2 constantVelocity;

    public LightningGuyProjectile(GameBody parent, Vector2 position, final Vector2 velocity, final Boolean reboundShot) {

        this.parent = parent;
        spriteWidth=7f;
        spriteHeight=10f;
        duration = 0.75f;

        this.reboundShot = false;

        if(!reboundShot){
            duration = 0.5f;
        }
        damage = 3;
        float SPEED = 50000f;
        self = this;
        constantVelocity = velocity;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        body.setLinearVelocity(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED);
        body.applyForceToCenter(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED, true);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        animator = new Animator("Sprites/LightningProjectile2.png", 1, 14, 0.125f);

        //rebound AFTER 2.5 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                ((LightningGuyProjectile) dataBundle.baseObject).reboundShot = true;
            }
        }, duration/2);

        //DESTROY AFTER 5 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);

//        System.out.println(this+ "info: " +
//                "position: "+position+"\n"+
//                "velocity: "+velocity+"\n"+
//                "body: "+body+"\n" +
//                "sprite:"+sprite+"\n");
    }

    public Body initializeBody(){

        radius = 3.0f;
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
        returnDataBundle.rotatable = false;
        returnDataBundle.isPlayer = false;
        body.setUserData(returnDataBundle);
        return returnDataBundle;
    }

    @Override
    public void update(){
        animate();
        if(reboundShot){
            constantVelocity = parent.body.getPosition().add(body.getPosition().scl(-1)).scl(6000);
        }
        sprite.setRotation(constantVelocity.angle());
        dataBundle.sprite = sprite;
        body.setLinearVelocity(constantVelocity);
        body.setUserData(dataBundle);
    }

    @Override
    public void getHit(Bullet bullet){
        kill();
    }

}
