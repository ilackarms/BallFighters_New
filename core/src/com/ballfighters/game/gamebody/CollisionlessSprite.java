package com.ballfighters.game.gamebody;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 8/5/2014.
 */
public class CollisionlessSprite extends GameBody {

    float duration,  radius;
    Boolean rotationEnabled;

    public CollisionlessSprite(Vector2 position, Animator animator, float duration, float radius) {

        spriteWidth=15f;
        spriteHeight=15f;
        this.duration=duration;
        this.radius= radius;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeStaticBody();
        body.setTransform(position, 0);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        this.animator = animator;

        rotationEnabled = false;

        //DESTROY AFTER 0.75 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);
    }

    public CollisionlessSprite( Vector2 position, Animator animator, float duration, float width, float height) {

        spriteWidth=width;
        spriteHeight=height;
        this.duration=duration;
        this.radius= 5f;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeStaticBody();
        body.setTransform(position, 0);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        this.animator = animator;

        //DESTROY AFTER 0.75 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);

        rotationEnabled = false;
    }

    public CollisionlessSprite( Vector2 position, Animator animator, float duration, float width, float height, Vector2 velocity) {

        velocity.x = velocity.x/2;
        velocity.y = velocity.y/2;

        spriteWidth=width;
        spriteHeight=height;
        this.duration=duration;
        this.radius= 5f;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeKinematicBody(velocity);
        body.setTransform(position, velocity.angle()* MathUtils.degreesToRadians);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        this.animator = animator;

        //DESTROY AFTER 0.75 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);

        rotationEnabled = true;
        dataBundle.rotatable = true;
    }



    public Body initializeStaticBody(){

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

    public Body initializeKinematicBody(Vector2 velocity){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        Shape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;

        body = GameData.WORLD.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);

        body.setLinearVelocity(velocity);

        shape.dispose();

        return body;
    }

    public UserDataBundle createUserDataBundle() {
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.destroyOnCollision = false;
        returnDataBundle.rotatable = true;
        returnDataBundle.baseObject = this;
        returnDataBundle.flaggedForDeletion = false;
        returnDataBundle.isPlayer = false;
        returnDataBundle.ghostMode = true;
        body.setUserData(returnDataBundle);
        return returnDataBundle;
    }

    @Override
    public void update(){
        super.update();
        dataBundle.sprite = sprite;
        body.setUserData(dataBundle);
    }

    @Override
    public void getHit(Bullet bullet){
        kill();
    }
}
