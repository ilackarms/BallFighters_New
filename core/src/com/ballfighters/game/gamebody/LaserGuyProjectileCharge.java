package com.ballfighters.game.gamebody;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/4/2014.
 */
public class LaserGuyProjectileCharge extends GameBody {

    GameBody parent;
    float duration;

    public LaserGuyProjectileCharge(GameBody parent, Vector2 position, Vector2 velocity) {

        this.parent = parent;
        spriteWidth=15f;
        spriteHeight=15f;
        duration = 0.75f;

        this.position = position;
        inputDirection = new Vector2(0, 0);
        body =  initializeBody();
        body.setTransform(position, 0);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        animator = new Animator("Sprites/LaserGuyProjectileCharge.png", 1, 6, 0.125f);

        final Vector2 laserPosition = this.position;
        final Vector2 laserVelocity = velocity;
        final GameBody laserParent = parent;
        //DESTROY AFTER 0.75 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                new LaserGuyProjectile(laserParent, laserPosition , laserVelocity,30);
                kill();
            }
        }, duration);
    }



    public Body initializeBody(){

        float radius = 5f;
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
