package com.ballfighters.game.gamebody;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class Animator {
    public int rows, cols;
    public Texture walkSheet;
    public TextureRegion[] walkFrames;
    public TextureRegion currentFrame;
    public Animation moveAnimation;
    public float frameRate = 0.50f;
    public float stateTime;

    public Animator(String file, int rows, int cols){
        this.rows=rows;
        this.cols=cols;
        walkSheet = new Texture(Gdx.files.internal(file));
        walkFrames = new TextureRegion[rows*cols];
        int index=0;
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/cols, walkSheet.getHeight()/rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        moveAnimation = new Animation(frameRate, walkFrames);
        stateTime = 0f;
        currentFrame = moveAnimation.getKeyFrame((0 % (frameRate*cols)), true);
    }

    public Animator(String file, int rows, int cols, float frameRate){
        this.rows=rows;
        this.cols=cols;
        this.frameRate = frameRate;
        walkSheet = new Texture(Gdx.files.internal(file));
        walkFrames = new TextureRegion[rows*cols];
        int index=0;
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/cols, walkSheet.getHeight()/rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        moveAnimation = new Animation(frameRate, walkFrames);
        stateTime = 0f;
        currentFrame = moveAnimation.getKeyFrame((0 % (frameRate*cols)), true);
    }

    public void update(Vector3 direction){
        direction = MyMathStuff.toUnit(direction);
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        float state=0; //= stateTime;
        if(direction.x>=0  && Math.abs(direction.x) > Math.abs(direction.y)){
            state = rows*0;
        }
        if(direction.x<0  && Math.abs(direction.x) > Math.abs(direction.y)){
            state = rows*1;
        }
        if(direction.y<=0  && Math.abs(direction.y) > Math.abs(direction.x)){
            state = rows*2;
        }
        if(direction.y>0  && Math.abs(direction.y) > Math.abs(direction.x)){
            state = rows*3;
        }
        currentFrame = moveAnimation.getKeyFrame((stateTime % (frameRate*cols)) + state*frameRate , true);

    }

    public void update(){//go through all frames, row/col doesn't matter
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = moveAnimation.getKeyFrame((stateTime), true);
    }



}
