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
public class NerdGuyProjectile extends Bullet {

    Vector2 velocity;
    float SPEED;

    public NerdGuyProjectile(GameBody parent, Vector2 position, Vector2 velocity) {

        this.parent = parent;
        this.velocity = velocity;
        spriteWidth=7f;
        spriteHeight=7f;
        duration = 1.5f;
        damage = 1;
        SPEED = 50000f;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        body.setLinearVelocity(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED);
        body.applyForceToCenter(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED, true);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        animator = new Animator("Sprites/NerdGuyProjectile.png", 1, 1, 1f);

        //DESTROY AFTER 5 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);

    }

    public Body initializeBody(){

        float radius = 3.0f;
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
        body.setLinearVelocity(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED);

        sprite.setRotation(velocity.angle());
        dataBundle.sprite = sprite;
        body.setLinearVelocity(velocity);
        body.setUserData(dataBundle);
    }

    @Override
    public void getHit(Bullet bullet){
        kill();
    }

    @Override
    public void kill(){
        final Vector2 explosionLocation = new Vector2(this.body.getPosition());
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                new CollisionlessSprite(explosionLocation, new Animator("Sprites/NerdGuyProjectileDeath.png",1,2,0.125f),0.25f,5f);
            }
        },0.01f);

        super.kill();
    }

}