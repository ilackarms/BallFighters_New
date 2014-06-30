package com.battlefighters.game.gamebody;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.battlefighters.global.GameData;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class GamePolygon {
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    PolygonShape shape;
    Body body;
    Fixture fixture;

    public GamePolygon(Vector2 position, Vector2[] vertices){
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        //polygon shape
        shape = new PolygonShape();
        shape.set(vertices);

        //fixture definition
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;

        fixture = GameData.WORLD.createBody(bodyDef).createFixture(fixtureDef);

        shape.dispose();
    }
}
