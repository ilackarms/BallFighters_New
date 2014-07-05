package com.ballfighters.game.gamebody;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/4/2014.
 */
public class LittleBooProjectile extends Bullet {
    Body body;

    public LittleBooProjectile(Vector2 position, Vector2 velocity) {
        spriteWidth=5f;
        spriteHeight=5f;
        duration = 5f;
        damage = 6;

        this.position = position;
        animator = new Animator("Sprites/littleboo.png", 4, 4);
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        body.setLinearVelocity(velocity);
        dataBundle = createUserDataBundle();

        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);

        clickPosition = new Vector3(0,0,0); //necessary so we don't get errors in update()
        animator = new Animator("Sprites/LittleBooProjectile.png",1,1);


        //DESTROY AFTER 5 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration/5);
    }


    public Body initializeBody(){
        float radius = 2.5f;
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
        animator.update();
        sprite = new Sprite(new TextureRegion(animator.currentFrame));
        sprite.setSize(spriteHeight, spriteWidth);
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        dataBundle.sprite = sprite;
        body.setUserData(dataBundle);
    }
}
