package com.ballfighters.game.gamebody;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.players.DeathGuyAI;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/26/2014.
 */
public class DeathGuyShield extends Bullet {

    Sprite sprite;
    GameBody target;

    public DeathGuyShield(GameBody parent, Vector2 position, Vector2 velocity, GameBody target) {

        this.parent = parent;
        this.target = target;
        spriteWidth=10.5f;
        spriteHeight=10.5f;
        duration = 3.5f;
        damage = 3;
        float SPEED = 50000f;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        body.setLinearVelocity(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED);
        body.applyForceToCenter(MyMathStuff.toUnit(velocity).x*SPEED,MyMathStuff.toUnit(velocity).y*SPEED, true);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        sprite = new Sprite(new Texture("Sprites/DeathGuyShield.png"));
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

        float radius = 5.5f;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Shape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
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
        dataBundle.sprite = sprite;
        if(parent instanceof DeathGuyAI) {
            body.applyForceToCenter(MyMathStuff.toUnit(target.body.getPosition().add(body.getPosition().scl(-1))).scl(1000000), true);
        }
        else {
            body.setLinearVelocity(MyMathStuff.toUnit(target.body.getPosition().add(body.getPosition().scl(-1))).scl(1000));
        }
        body.setUserData(dataBundle);
    }

    @Override
    public void getHit(Bullet bullet){
        kill();
    }
}
