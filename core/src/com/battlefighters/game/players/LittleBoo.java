package com.battlefighters.game.players;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.battlefighters.game.gamebody.Animator;
import com.battlefighters.game.gamebody.GameBody;
import com.battlefighters.game.gamebody.PlayerTypes;
import com.battlefighters.game.gamebody.UserDataBundle;
import com.battlefighters.global.GameData;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class LittleBoo extends Player {



    private float radius = 6f;
    private float density = 0.05f;
    private float restitution = 0.5f;

    public LittleBoo(Vector2 position) {
        animator = new Animator("Sprites/littleboo.png", 4, 4);
        inputDirection = new Vector2(0,0);
        appliedForce =new Vector2(0,0);
        body = createBody();
        health = 100;
        spriteHeight = 12f;
        spriteWidth = 12f;
        dataBundle = createUserDataBundle();
    }

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
        shape.setPosition(position);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        body = GameData.WORLD.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);
        shape.dispose();
        body.setUserData(dataBundle);
        return body;
    }
}
