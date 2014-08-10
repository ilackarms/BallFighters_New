package com.ballfighters.game.players;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.gamebody.GameBody;
import com.ballfighters.game.gamebody.UserDataBundle;
import com.ballfighters.global.GameData;
import com.ballfighters.screens.GameOverScreen;
import com.ballfighters.tween.BallTween;
import com.ballfighters.tween.SpriteAccessor;

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
    public float PERMANENT_ACCELERATION;
    public int slowStacks;
    public String name;
    public Boolean isAI=true;

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

    public void gooHit(){
        slowStacks++;
        ACCELERATION/=2;
        body.setLinearVelocity(body.getLinearVelocity().scl(0.5f));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                slowStacks--;
                if(slowStacks<=0){
                    ACCELERATION = PERMANENT_ACCELERATION;
                }
            }
        },2);
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

    @Override
    public void kill(){
        super.kill();

        if(!isAI) {

            //NEW GAME ON DEATH!
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    GameData.BLACKSCREEN = new Sprite(new Texture(Gdx.files.internal("Backgrounds/blackScreen.png")));
                    GameData.BLACKSCREEN.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    Tween.set(GameData.BLACKSCREEN, SpriteAccessor.ALPHA).target(0).start(GameData.tweenManager);
                    Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(GameData.screen));
                            Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(4).start(GameData.tweenManager);
                            GameData.screen.dispose();
                            GameData.screen.hide();
                        }
                    }).start(GameData.tweenManager);
                }
            }, 8);
            health = 0;
        }
    }
}
