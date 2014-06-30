package com.battlefighters.game.gamebody;

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
    public GameBody baseObject;

    public UserDataBundle(){
        this.sprite = new Sprite();
        this.rotatable = true;
        this.isDisposable = false;
        this.flaggedForDeletion = false;
        health = 100;
        this.isPlayer = false;
    }
}
