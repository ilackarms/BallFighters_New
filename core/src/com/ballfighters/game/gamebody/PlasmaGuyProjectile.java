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
public class PlasmaGuyProjectile extends Bullet {


    Boolean initialShot;
    float radius;
    Vector2 constantVelocity;

    public PlasmaGuyProjectile(GameBody parent, Vector2 position, Vector2 velocity, final Boolean initialShot) {

        this.parent = parent;
        spriteWidth=7f;
        spriteHeight=10f;
        duration = 1.0f;
        if(!initialShot){
            duration = 0.5f;
        }
        damage = 4;
        float SPEED = 50000f;
        this.initialShot = initialShot;
        constantVelocity = velocity;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        body.setLinearVelocity(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED);
        body.applyForceToCenter(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED, true);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        animator = new Animator("Sprites/plasmaShot.png", 1, 5, 0.125f);

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
        sprite.setRotation(constantVelocity.angle());
        dataBundle.sprite = sprite;
        body.setLinearVelocity(constantVelocity);
        body.setUserData(dataBundle);
    }

    @Override
    public void getHit(Bullet bullet){
        kill();
    }

    @Override
    public void kill(){
        if(initialShot) {

            final Vector2 explosionlocation = new Vector2(this.body.getPosition());
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PlasmaGuySounds/plasmaShot.wav"));
                    long soundID = fireSound.play();
                    fireSound.setVolume(soundID, GameData.VOLUME);
                    fireSound.dispose();

                    Vector2 direction1 = MyMathStuff.findPerpendicularVectorOne(body.getLinearVelocity());
                    Vector2 direction2 = MyMathStuff.findPerpendicularVectorTwo(body.getLinearVelocity());

                    if(direction1.len()<=0 || direction2.len()<=0 ){
                        direction1 = new Vector2(1,-1);
                        direction2 = new Vector2(-1,1);
                    }

                    new CollisionlessSprite(explosionlocation, new Animator("Sprites/plasmaShotExplosion.png",1,8,0.0625f),0.25f,5f);
                    new PlasmaGuyProjectile(parent, explosionlocation.add(MyMathStuff.toUnit(direction1).scl(2f*radius)), direction1, false);
                    new PlasmaGuyProjectile(parent, explosionlocation.add(MyMathStuff.toUnit(direction2).scl(2f*radius)), direction2, false);
                }
            }, 0.01f);
        }

        super.kill();
    }
}
