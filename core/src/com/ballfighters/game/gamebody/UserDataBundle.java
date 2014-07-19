package com.ballfighters.game.gamebody;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class UserDataBundle {
    public Sprite sprite;
    public Boolean rotatable;
    public Boolean isDisposable;
    public Boolean flaggedForDeletion;
    public int health;
    public Boolean isPlayer;
    public Boolean destroyOnCollision;
    public Boolean followsPlayer;
    public GameBody baseObject;
    public int damage;
    public Boolean ghostMode;
    public Boolean draw;

    public UserDataBundle(){
        this.sprite = new Sprite();
        this.rotatable = true;
        this.isDisposable = false;
        destroyOnCollision = false;
        this.flaggedForDeletion = false;
        health = 100;
        this.isPlayer = false;
        followsPlayer = false;
        ghostMode = false;
        draw = true;
    }
}
