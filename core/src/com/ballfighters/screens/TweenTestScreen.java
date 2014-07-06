package com.ballfighters.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/6/2014.
 */
public class TweenTestScreen implements Screen{
    private static TweenManager manager;
    public Sprite sprite;
    public SpriteBatch batch;

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        manager.update(delta);

        batch.begin();
        sprite.draw(batch);
        batch.end();
    }
    public void show(){
        GameData.screen = this;

        batch = new SpriteBatch();

        manager = new TweenManager();
        sprite = new Sprite(new Texture(Gdx.files.internal("Sprites/playerSpriteSheet.png")));
        sprite.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        sprite.setSize(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);
        sprite.setOriginCenter();

        Tween.setCombinedAttributesLimit(4);
        Tween.registerAccessor(Sprite.class, new TestAccessor());

//        Tween.to(sprite,TestAccessor.COLOR,1f).repeatYoyo(4,1f).target(1,0,0,1).start(manager);
        Tween.to(sprite,TestAccessor.WIDTH,0.3f).repeatYoyo(111,0).target(sprite.getWidth()*1.1f).start(manager);
        Tween.to(sprite,TestAccessor.HEIGHT,0.3f).repeatYoyo(111,0).target(sprite.getHeight()*1.1f).start(manager);
        Tween.to(sprite,TestAccessor.RED,0.3f).repeatYoyo(111,0).target(1).start(manager);
        Tween.to(sprite,TestAccessor.GREEN,0.3f).repeatYoyo(111,0).target(0).start(manager);
        Tween.to(sprite,TestAccessor.BLUE,0.3f).repeatYoyo(111,0).target(0).start(manager);



    }
    public void hide(){
        dispose();
    }
    public void pause(){

    }
    public void resume(){

    }
    public void resize(int width, int height){

    }
    public void dispose(){

    }

    private class TestAccessor implements TweenAccessor<Sprite>{
        public static final int COLOR = 0;
        public static final int WIDTH = 1;
        public static final int HEIGHT = 2;
        public static final int RED = 3;
        public static final int GREEN = 4;
        public static final int BLUE= 5;
        @Override
        public int getValues(Sprite target, int tweenType, float[] returnValues) {
            switch (tweenType){
                case RED:
                    returnValues[0] = target.getColor().r;
                    return 1;
                case GREEN:
                    returnValues[0] = target.getColor().g;
                    return 1;
                case BLUE:
                    returnValues[0] = target.getColor().b;
                    return 1;
                case COLOR:
                    returnValues[0] = target.getColor().r;
                    returnValues[1] = target.getColor().g;
                    returnValues[2] = target.getColor().b;
                    returnValues[3] = target.getColor().a;
                    return 1;
                case WIDTH:
                    returnValues[0] = target.getWidth();
                    return 1;
                case HEIGHT:
                    returnValues[0] = target.getHeight();
                    return 1;
            }
            return 0;
        }

        @Override
        public void setValues(Sprite target, int tweenType, float[] newValues) {
            switch (tweenType) {
                case RED:target.setColor(newValues[0], target.getColor().g, target.getColor().b, target.getColor().a);
                    break;
                case GREEN:target.setColor(target.getColor().r, newValues[0], target.getColor().b, target.getColor().a);
                    break;
                case BLUE:target.setColor(target.getColor().r, target.getColor().g, newValues[0], target.getColor().a);
                    break;
                case COLOR:
                    target.setColor(newValues[0], newValues[1], newValues[2], newValues[3]);
                    break;
                case WIDTH:
                    target.setSize(newValues[0],target.getHeight());
                    break;
                case HEIGHT:
                    target.setSize(target.getWidth(),newValues[0]);
                    break;
            }
        }
    }
}
