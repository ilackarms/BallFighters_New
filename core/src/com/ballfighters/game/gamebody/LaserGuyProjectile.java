package com.ballfighters.game.gamebody;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/20/2014.
 */
public class LaserGuyProjectile extends Bullet{
    public GameBody parent;
    public float angle;
    public final float HEIGHT=2.5f, WIDTH = 8f;
    public Vector2 direction;

    public Body initializeBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        PolygonShape shape= new PolygonShape();
        shape.setAsBox(WIDTH, HEIGHT);


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
        returnDataBundle.rotatable = true;
        returnDataBundle.baseObject = this;
        returnDataBundle.flaggedForDeletion = false;
        returnDataBundle.isPlayer = false;
        body.setUserData(returnDataBundle);
        return returnDataBundle;
    }



    public LaserGuyProjectile(GameBody parent, Vector2 position, Vector2 direction, final int blasts) {
        this.parent = parent;
        this.position = new Vector2(position).add(MyMathStuff.toUnit(direction).scl(WIDTH));
        this.angle = direction.angle();
        this.direction = direction;

        spriteWidth=HEIGHT*2;
        spriteHeight=WIDTH*2;
        float duration = 0.2f;
        damage = 4;

        body =  initializeBody();
        body.setTransform(position, 0);
        dataBundle = createUserDataBundle();

        sprite = new Sprite(new Texture("Sprites/LaserGuyProjectile.gif"));
        sprite.setSize(spriteHeight, spriteWidth);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        final Vector2 laserPosition = this.position;
        final Vector2 laserVelocity = direction;
        final GameBody laserParent = parent;
        final int laserBlasts = blasts-1;
        //repeat shot!
        if(blasts>0) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    new LaserGuyProjectile(laserParent,laserPosition,laserVelocity,laserBlasts);
                }
            }, duration / 12);
        }

        //DESTROY AFTER 5 SECONDS!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                kill();
            }
        }, duration);

        //make sure that dead lasers dont make new ones
        if((((UserDataBundle) parent.body.getUserData()).flaggedForDeletion)){
            killChain();
        }
    }

    @Override
    public void update(){
        body.setTransform(position,direction.angle());

        dataBundle.sprite = sprite;
        body.setUserData(dataBundle);
    }

    public void killChain(){
        kill();
        if(parent instanceof LaserGuyProjectile){
            ((LaserGuyProjectile) parent).killChain();
        }
    }
}
