package com.ballfighters.game.gamebody;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/26/2014.
 */
public class DeathGuyProjectile  extends Bullet {

    Sprite sprite;

    public DeathGuyProjectile(GameBody parent, Vector2 position, Vector2 velocity, int direction) {

        this.parent = parent;
        spriteWidth=13f;
        spriteHeight=13f;
        duration = 1.5f;
        damage = 6;
        float SPEED = 50000f;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        body.setLinearVelocity(MyMathStuff.toUnit(velocity).x * SPEED, MyMathStuff.toUnit(velocity).y * SPEED);
        body.setAngularVelocity(15*direction);
        body.applyForceToCenter(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED, true);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        sprite = new Sprite(new Texture("Sprites/Scythe.png"));
        sprite.setSize(spriteHeight, spriteWidth);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        //DESTROY AFTER 5 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);
    }


    public Body initializeBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        PolygonShape shape = new PolygonShape();
        Vector2 vertices[] = {
                new Vector2(-3,-8),
                new Vector2(0,-4),
                new Vector2(-1,-1),
                new Vector2(5,6)
        };
        shape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;

        body = GameData.WORLD.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);

        shape.dispose();

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        shape = new PolygonShape();
        Vector2 vertices2[] = {
                new Vector2(1,3),
                new Vector2(1,6),
                new Vector2(-8,4),
                new Vector2(-7,5)
        };
        shape.set(vertices2);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;

        fixture = body.createFixture(fixtureDef);

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
        dataBundle.sprite = sprite;
        body.setUserData(dataBundle);
    }

    @Override
    public void getHit(Bullet bullet){
        kill();
    }
}
