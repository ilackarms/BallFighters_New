package com.ballfighters.game.players;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ballfighters.game.gamebody.Animator;
import com.ballfighters.game.gamebody.InputHandler;
import com.ballfighters.game.gamebody.UserDataBundle;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class LittleBoo extends Player {


	private InputHandler inputHandler;
    private float radius = 6f;
    private float density = 0.05f;
    private float restitution = 0.5f;

    public LittleBoo(Vector2 position) {
    	this.position = position;
        animator = new Animator("Sprites/littleboo.png", 4, 4);
        inputDirection = new Vector2(0,0);
        body = createBody();
        health = 100;
        spriteHeight = 15f;
        spriteWidth = 15f;
        dataBundle = createUserDataBundle();
        inputHandler = new InputHandler(this);
        Gdx.input.setInputProcessor(inputHandler);
        clickPosition = new Vector3(0,0,0);
        ACCELERATION = 300f;

    }

    private UserDataBundle createUserDataBundle(){
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.baseObject = this;
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
        body.setUserData(dataBundle);
        body.setTransform(position,0);
        return body;
    }
    
    @Override
    public void update(){
    	super.update();
        inputDirection.x=0;
        inputDirection.y=0;
//        move(inputHandler.inputDirection);
        inputHandler.directionHandler();
        animate();
    }

    @Override
    public void move(Vector2 inputDirection){
    	if(inputDirection.len()!=0){
            if(Gdx.app.getType()== Application.ApplicationType.Android) {
                inputDirection.x = inputHandler.inputDirection.x * ACCELERATION;
                inputDirection.y = inputHandler.inputDirection.y * ACCELERATION;
            }
            if(Gdx.app.getType()== Application.ApplicationType.Desktop) {
                inputDirection.x = MyMathStuff.toUnit(inputHandler.inputDirection).x * ACCELERATION;
                inputDirection.y = MyMathStuff.toUnit(inputHandler.inputDirection).y * ACCELERATION;
            }
        body.applyForceToCenter(inputDirection, true);
    	}
    }

}
