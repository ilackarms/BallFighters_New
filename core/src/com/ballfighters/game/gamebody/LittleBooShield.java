package com.ballfighters.game.gamebody;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/8/2014.
 */
public class LittleBooShield extends GameBody {
    GameBody parent;
    public float angle;
    public Vector2 displacement;

    public LittleBooShield (GameBody parent, Vector2 displacement, float angle) {
        this.parent = parent;
        this.displacement = new Vector2(displacement);
        this.position = new Vector2(parent.body.getPosition().x+displacement.x,parent.body.getPosition().y+displacement.y);
        this.angle = angle;

        spriteWidth=13f;
        spriteHeight=4f;
        float duration = 5f;


        body =  initializeBody();
        body.setTransform(position, 0);
        dataBundle = createUserDataBundle();

        sprite = new Sprite(new Texture("Sprites/LittleBooShield.png"));
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


    @Override
    public void update(){
        dataBundle.sprite = sprite;
        body.setTransform(parent.body.getPosition().x+displacement.x,parent.body.getPosition().y+displacement.y,angle);
        body.setLinearVelocity(parent.body.getLinearVelocity());
        body.setUserData(dataBundle);
    }

    public Body initializeBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        PolygonShape shape = new PolygonShape();
        Vector2 vertices[] = {
                new Vector2(0,-4),
                new Vector2(0.5f,-4),
                new Vector2(-2.5f,-4),
                new Vector2(0,0),
                new Vector2(0.5f,0),
                new Vector2(0,4),
                new Vector2(0.5f,4),
                new Vector2(-2.5f,4)
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

        return body;
    }

    public UserDataBundle createUserDataBundle() {
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.destroyOnCollision = false;
        returnDataBundle.damage = 0;
        returnDataBundle.rotatable = true;
        returnDataBundle.baseObject = this;
        returnDataBundle.flaggedForDeletion = false;
        returnDataBundle.isPlayer = false;
        returnDataBundle.followsPlayer = true;
        body.setUserData(returnDataBundle);
        return returnDataBundle;
    }

}
