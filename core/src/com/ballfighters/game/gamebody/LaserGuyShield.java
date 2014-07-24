package com.ballfighters.game.gamebody;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/22/2014.
 */
public class LaserGuyShield extends Bullet {
    public GameBody parent;


    public Body initializeBody(){

        float radius = 12f;
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

    public LaserGuyShield (GameBody parent) {
        this.parent = parent;
        position = new Vector2(parent.body.getPosition());
        spriteWidth = 18f;
        spriteHeight = 18f;
        float duration = 7f;
        damage = 5;

        body =  initializeBody();
        body.setTransform(position, 0);
        dataBundle = createUserDataBundle();

        animator = new Animator("Sprites/LaserGuyLightning.png",1,4,0.125f/2);
        inputDirection = new Vector2(0,0);
        clickPosition = new Vector3(0,0,0);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);
    }

    @Override
    public void update(){
        super.update();
        body.setTransform(parent.body.getPosition(),0);
        dataBundle.sprite = sprite;
        body.setUserData(dataBundle);
    }
}
