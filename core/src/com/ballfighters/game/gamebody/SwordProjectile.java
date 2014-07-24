package com.ballfighters.game.gamebody;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/15/2014.
 */
public class SwordProjectile extends Bullet {

    public Vector2 displacement;
    public float angle;
    public int direction;

    public SwordProjectile (GameBody parent, Vector2  displacement, float angle) {

        this.parent = parent;
        this.displacement = new Vector2(displacement);
        this.position = new Vector2(parent.body.getPosition().x+displacement.x,parent.body.getPosition().y+displacement.y);
        this.angle = angle;

        System.out.println("DISPLACEMENT: ("+displacement.x+","+displacement.y+")");
        System.out.println("ANGLE: (" + MyMathStuff.convertTo2D(parent.clickPosition).angle()+")");
        if(displacement.x>=0){
            direction = 1;
        }else {
            direction = -1;
        }
        if(MyMathStuff.convertTo2D(parent.clickPosition).angle()>45 && MyMathStuff.convertTo2D(parent.clickPosition).angle()<135){
            direction*=-1;
        }

        spriteWidth=29f;
        spriteHeight=6f;
        duration = 0.25f;
        damage = 6;

        body =  initializeBody();
        body.setTransform(position, angle);
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0, 0, 0);//necessary so we don't get errors in update()

        sprite = new Sprite(new Texture("Sprites/Sword.png"));
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
        displacement.rotate(direction*-1000*Gdx.graphics.getDeltaTime());
        angle = ((float) Math.atan2(-1*displacement.x,displacement.y));
        body.setTransform(parent.body.getPosition().x+displacement.x,parent.body.getPosition().y+displacement.y,angle);
        body.setLinearVelocity(parent.body.getLinearVelocity());
        body.setUserData(dataBundle);
    }

    public Body initializeBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        PolygonShape shape = new PolygonShape();
        Vector2 vertices[] = {new Vector2(2.5f,10),new Vector2(-2.5f,10),new Vector2(-2.5f,-10),new Vector2(2.5f,-10)};
        shape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density =0.1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;

        body = GameData.WORLD.createBody(bodyDef);
        body.createFixture(fixtureDef);

        shape.dispose();

        /**
         * TODO:
         * TRY MAKING IT A STATIC OR KINEMATIC OBJECT ALWAYS CENTERED ROTATING ARUOND THE BODY BUT WE JUST UPDATE IT MANUALLY..?
         * collision should still work i think
         */


        return body;
    }

    public UserDataBundle createUserDataBundle() {
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.destroyOnCollision = false;
        returnDataBundle.damage = damage;
        returnDataBundle.rotatable = true;
        returnDataBundle.baseObject = this;
        returnDataBundle.flaggedForDeletion = false;
        returnDataBundle.rotatable = true;
        returnDataBundle.isPlayer = false;
        body.setUserData(returnDataBundle);

        return returnDataBundle;
    }
}
