package com.ballfighters.global;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.gamebody.Animator;

/**
 * Created by Dell_Owner on 7/10/2014.
 */
public class AnimationPackage {

    public Animator animator;
    public Boolean playing;
    public float duration;
    public Vector2 position;
    public float width, height;

    public AnimationPackage(Animator animator, float width, float height){
        playing = false;
        duration = -1;
        position = new Vector2(0,0);
        this.animator=animator;
        this.width = width;
        this.height = height;
    }

    public AnimationPackage play(){
       playing = true;
        return this;
    }

    public AnimationPackage stop(){
       playing = false;
        return this;
    }

    public AnimationPackage setDuration(float duration){
        this.duration = duration;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                stop();
            }
        },duration);
        return this;
    }

    public AnimationPackage setPosition(Vector2 position){
        this.position = position;
        return this;
    }

    public void update(){
        animator.update();
    }
}
