package com.battlefighters.game.gamebody;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.battlefighters.global.GameData;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class PlayerTypes {
    public static class LittleBoo{
        public float SPRITE_HEIGHT = 12f, SPRITE_WIDTH = 12F;
        public int health = 100;
        public Animator animator = new Animator("littleboo.png", 4, 4);
        public Vector2 inputDirection = new Vector2(0,0);
        public Vector2 appliedForce =new Vector2(0,0);
        public Body body = createBody();
        public Fixture fixture;




        private float radius = 6f;
        private float density = 0.05f;
        private float restitution = 0.5f;

        private UserDataBundle createUserDataBundle(){
            UserDataBundle returnDataBundle = new UserDataBundle();
            returnDataBundle.flaggedForDeletion = false;
            returnDataBundle.health = health;
            returnDataBundle.rotatable = false;
            returnDataBundle.isPlayer = true;
            return returnDataBundle;
        }
        private Body createBody() {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            CircleShape shape = new CircleShape();
            shape.setRadius(radius);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = density;
            fixtureDef.restitution = restitution;
            body = GameData.WORLD.createBody(bodyDef);
            fixture = body.createFixture(fixtureDef);
            shape.dispose();
            body.setUserData(createUserDataBundle());
            return body;
        }
    }
}