package com.ballfighters.game.gamebody;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public abstract class GameBody {
    public Body body;
    public Sprite sprite;
    public Fixture fixture;
    public Vector2 position;
    public Vector2 inputDirection;
    public Animator animator;
    public Vector3 clickPosition;
    public UserDataBundle dataBundle;
    public float spriteHeight, spriteWidth;

    public void update(){
        animate();
    }

    public void dispose(){
        sprite.getTexture().dispose();
    }

    protected void animate(){
        //animation

        animator.update(clickPosition);
        sprite = new Sprite(new TextureRegion(animator.currentFrame));
        sprite.setSize(spriteHeight, spriteWidth);
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        dataBundle.sprite = sprite;
        body.setUserData(dataBundle);
    }

    public void kill(){
            dataBundle.flaggedForDeletion = true;
    }

}
