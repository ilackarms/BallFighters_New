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

    public static Vector2 findPerpendicularVectorOne(Vector2 vector){
        Vector2 returnVector = new Vector2(0,0);
        returnVector.x = vector.y;
        returnVector.y = -1*vector.x;
        return returnVector;
    }

    public static Vector2 findPerpendicularVectorTwo(Vector2 vector){
        Vector2 returnVector = new Vector2(0,0);
        returnVector.x = -1*vector.y;
        returnVector.y = vector.x;
        return returnVector;
    }

    public static Vector2 convertTo2D(Vector3 vector3){
        Vector2 returnVector = new Vector2(0,0);
        returnVector.x = vector3.x;
        returnVector.y = vector3.y;
        return returnVector;
    }

    public static Vector3 convertTo3D(Vector2 vector3){
        Vector3 returnVector = new Vector3(0,0,0);
        returnVector.x = vector3.x;
        returnVector.y = vector3.y;
        return returnVector;
    }
}