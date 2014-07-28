package com.ballfighters.game.gamebody;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;
/**
 * Created by Dell_Owner on 7/27/2014.
 */
public class BombGuyShield  extends GameBody{

    GameBody parent;
    float duration;
    public Array<Body> worldBodies;
    public final float ATTRACTION = 5000f;

    public BombGuyShield(GameBody parent, Vector2 position, Vector2 velocity) {

        this.parent = parent;
        spriteWidth=20f;
        spriteHeight=20f;
        duration = 3.0f;
        float SPEED = 50000f;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        body.setLinearVelocity(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED);
        body.applyForceToCenter(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED, true);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        animator = new Animator("Sprites/BombGuyShield.png", 1, 5, 0.125f);

        //DESTROY AFTER 5 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);

        //for attraction purposes
        worldBodies = new Array<Body>();
    }

    public Body initializeBody(){

        float radius = 4.0f;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Shape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 15f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;

        body = GameData.WORLD.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    public UserDataBundle createUserDataBundle() {
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.destroyOnCollision = false;
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
        body.applyForceToCenter(body.getLinearVelocity(),true);
        body.setUserData(dataBundle);


        GameData.WORLD.getBodies(worldBodies);

        //attract all other bodies!
        for(Body attractedBody : worldBodies){
            if(attractedBody.getUserData()!=null && attractedBody.getUserData() instanceof UserDataBundle && this.body.isActive()){
                UserDataBundle bundle = (UserDataBundle) attractedBody.getUserData();
                if(!(bundle.baseObject).equals(this.parent)) {
                    Vector2 attractor = new Vector2(MyMathStuff.toUnit(attractedBody.getPosition().add(this.body.getPosition().scl(-1))).scl(-1*ATTRACTION));
                    System.out.println("Object: "+bundle.baseObject+" attracted to "+this+" in direction "+attractor);
                    attractedBody.applyForceToCenter(attractor,true);
                }
            }
        }
    }

    @Override
    public void getHit(Bullet bullet){
        kill();
    }
}