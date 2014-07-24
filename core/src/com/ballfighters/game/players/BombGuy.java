package com.ballfighters.game.players;

import com.badlogic.gdx.math.Vector2;
import com.ballfighters.game.gamebody.InputHandler;
import com.ballfighters.tween.BallTween;

/**
 * Created by Dell_Owner on 7/23/2014.
 */
public class BombGuy extends Player {

    protected InputHandler inputHandler;
    protected float radius = 5.5f;
    protected float density = 0.5f;
    protected float restitution = 0.5f;
    public static final int MAX_HEALTH = 120;

    protected BallTween tween;

    public BombGuy(Vector2 position){

    }
}
