package com.ballfighters.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public abstract class MyMathStuff {

    public static Vector2 toUnit(Vector2 vector){
        return(new Vector2(vector.x/vector.len(),
                vector.y/vector.len()));
    }
    public static Vector3 toUnit(Vector3 vector){
        return(new Vector3(vector.x/vector.len(),
                vector.y/vector.len(),
                vector.z/vector.len()));
    }
}