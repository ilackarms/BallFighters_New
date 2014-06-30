package com.battlefighters.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public abstract class MyMathStuff {

    public static Vector2 toUnit(Vector2 vector){
        return(new Vector2(vector.x/vector.len(),
                vector.y/vector.len()));
    }
}