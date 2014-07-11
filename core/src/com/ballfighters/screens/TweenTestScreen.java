package com.ballfighters.screens;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.ballfighters.game.gamebody.Animator;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/6/2014.
 */
public class TweenTestScreen implements Screen{
    private static TweenManager manager;
    public Sprite sprite;
    public SpriteBatch batch;
    public Animator animator;
    public TweenOnMyOwn tweenOnMyOwn;

    public long debugtime;

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        manager.update(delta);

        animator.update();
        sprite = new Sprite(animator.currentFrame);
        tweenOnMyOwn.update(sprite);
        sprite.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        sprite.setSize(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);

        batch.begin();
        sprite.draw(batch);
        batch.end();
    }
    public void show(){
        GameData.screen = this;

        batch = new SpriteBatch();

        manager = new TweenManager();

        animator = new Animator("Sprites/death_scythe.png",4,4);

        float[] color = {1,0,0,1};
        tweenOnMyOwn =new TweenOnMyOwn(animator , TweenOnMyOwn.COLOR, color, 0.2f).yoyo(1);
        debugtime = TimeUtils.millis();


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

    private class TweenOnMyOwn{
        /**
         * THE IDEA IS TO USE THE PRE/PPOST UPDATE BEFORE/AFTER THE ANIMATOR UPDATE IN RENDER!!!!
         */
        public static final int COLOR = 0;
        public static final int WIDTH = 1;
        public static final int HEIGHT = 2;
        public static final int RED = 3;
        public static final int GREEN = 4;
        public static final int BLUE= 5;
        public static final int POSITION= 6;

        Animator target;
        int tweenType;
        float[] initialTween;
        float[] tweenFrom;
        float[] tweenTo;
        float[] tweenStep;
        float duration;

        private Boolean initialStep;
        private float delay;
        private int loops;
        private int yoyos;

        public TweenOnMyOwn(Animator target, int tweenType, float[] tweenTo, float duration){
            this.target = target;
            this.tweenType = tweenType;
            this.tweenTo = new float[tweenTo.length];
            initialTween = new float[tweenTo.length];
            tweenFrom = new float[tweenTo.length];
            tweenStep = new float[tweenTo.length];


            switch(tweenType){
                case COLOR:
                    Sprite currentFrame = new Sprite(target.currentFrame);
                    tweenFrom[0] = currentFrame.getColor().r;
                    tweenFrom[1] = currentFrame.getColor().g;
                    tweenFrom[2] = currentFrame.getColor().b;
                    tweenFrom[3] = currentFrame.getColor().a;
            }

            System.arraycopy(tweenTo,0,this.tweenTo,0,tweenTo.length);
            System.arraycopy(tweenFrom,0,initialTween,0,tweenTo.length);
            this.duration = duration/3.0f;

            initialStep = true;
            loops = 0;
            delay = 0;
        }

        public void update(Sprite sprite) {
            if(initialStep){
                for(int i=0; i<tweenTo.length; i++){
                    tweenStep[i] = (tweenTo[i]-tweenFrom[i])/(duration/Gdx.graphics.getDeltaTime());
                    initialStep=false;
                }
            }
            if(loops>0 && reachedTarget()){
                loops--;
                System.arraycopy(initialTween,0,tweenFrom,0,initialTween.length);
            }
            if(yoyos>0 && reachedTarget()){
                yoyos--;
                System.out.println("YOYO!");
                float[] temp = new float[tweenFrom.length];
                System.arraycopy(initialTween,0,temp,0,tweenFrom.length);
                System.arraycopy(tweenTo,0,initialTween,0,tweenFrom.length);
                System.arraycopy(temp,0,tweenTo,0,tweenFrom.length);
                for(int i=0;i<tweenStep.length;i++){
                    tweenStep[i]*=-1;
                }
            }
            switch (tweenType) {
                case COLOR:
                    if (!reachedTarget()) {
                        for (int i = 0; i < tweenFrom.length; i++) {
                            tweenFrom[0] += tweenStep[0];
                            tweenFrom[1] += tweenStep[1];
                            tweenFrom[2] += tweenStep[2];
                            tweenFrom[3] += tweenStep[3];
                        }
                        sprite.setColor(tweenFrom[0], tweenFrom[1], tweenFrom[2], tweenFrom[3]);
                    }
            }
        }

        public Boolean reachedTarget(){
            Boolean reached = false;
            for(int i=0; i<tweenFrom.length; i++) {
                if (tweenStep[i] < 0) {
                    if (tweenFrom[i] <= tweenTo[i]) {
//                        System.out.println("TIME PASSED SINCE COMPLETION!: "+TimeUtils.timeSinceMillis(debugtime));
                        return true;
                    }
                }
                if (tweenStep[i] > 0) {
                    if (tweenFrom[i] >= tweenTo[i]) {
//                        System.out.println("TIME PASSED SINCE COMPLETION!: "+TimeUtils.timeSinceMillis(debugtime));
                        return true;
                    }
                }
            }
             return reached;
        }

        public TweenOnMyOwn loop(int times){
            yoyos = 0;
           loops = times;
            return this;
        }
        public TweenOnMyOwn yoyo(int times){
            loops=0;
           yoyos = times;
            return this;
        }
        public TweenOnMyOwn delay(float time){
            delay=time;
            return this;
        }

    }

}
