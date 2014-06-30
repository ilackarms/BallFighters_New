package com.battlefighters.game.gamebody;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.battlefighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class Animator {
    public int rows, cols;
    public Texture walkSheet;
    public TextureRegion[] walkFrames;
    public TextureRegion currentFrame;
    public SpriteBatch batch;
    public Animation moveAnimation;
    public float frameRate = 0.25f;
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
        batch = new SpriteBatch();
        stateTime = 0f;
    }

    public void update(Vector2 direction){
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


}
