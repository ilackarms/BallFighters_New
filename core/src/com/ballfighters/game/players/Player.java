package com.ballfighters.game.players;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ballfighters.game.gamebody.GameBody;
import com.ballfighters.game.gamebody.UserDataBundle;
import com.ballfighters.global.GameData;
import com.ballfighters.tween.BallTween;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class Player extends GameBody {
    public int health;
    public float ACCELERATION;
    public Vector2 lastPosition;
    public float radius;
    public float density;
    public float restitution;

    protected BallTween tween;

    public void update(){
        super.update();
    }

    public void fireShots(){

    }

    public void move(Vector2 inputDirection){

    }

    public void getHit(){

    }

    public void shield(){

    }



    protected UserDataBundle createUserDataBundle(){
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.baseObject = this;
        returnDataBundle.flaggedForDeletion = false;
        returnDataBundle.health = health;
        returnDataBundle.rotatable = false;
        returnDataBundle.isPlayer = true;
        return returnDataBundle;
    }

    protected Body createBody() {
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
        body.setUserData(dataBundle);
        body.setTransform(position,0);
        return body;
    }
}
