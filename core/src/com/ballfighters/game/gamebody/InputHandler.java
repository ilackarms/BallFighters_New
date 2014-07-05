package com.ballfighters.game.gamebody;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ballfighters.game.players.Player;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/2/2014.
 */
public class InputHandler implements InputProcessor {

    public Vector2 inputDirection;
    public Vector3 targetDirection;
    public Player player;
    public Vector3 mousePosition;

    public InputHandler(Player player){//todo: make the constructor accept a set of keys as the up/down/left/right input
        inputDirection = new Vector2(0,0);
        this.player = player;
        targetDirection = new Vector3(0,0,0);
        mousePosition = new Vector3(0,0,0);
    }


    public void directionHandler(){
        if(Gdx.app.getType()== Application.ApplicationType.Android) {
//
//            if (Gdx.input.getAccelerometerX() < -0.5f) {
//                inputDirection.y = 1;
//            }
//            if (Gdx.input.getAccelerometerY() < -0.5f) {
//                inputDirection.x = -1;
//            }
//            if (Gdx.input.getAccelerometerX() > 0.5f) {
//                inputDirection.y = -1;
//            }
//            if (Gdx.input.getAccelerometerY() > 0.5f) {
//                inputDirection.x = 1;
//            }
            inputDirection.x = 0;
            inputDirection.y = 0;
            inputDirection.x = Gdx.input.getAccelerometerY();
            inputDirection.y = -1*Gdx.input.getAccelerometerX()+5;
            player.move(inputDirection);
            System.out.println("X TILT ="+inputDirection.x+"             Y TILT ="+inputDirection.y);
        }
        if(Gdx.app.getType()== Application.ApplicationType.Desktop) {
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                inputDirection.x = 0;
                inputDirection.y = 1;
                player.move(inputDirection);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                inputDirection.x = 0;
                inputDirection.y = -1;
                player.move(inputDirection);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                inputDirection.x = -1;
                inputDirection.y = 0;
                player.move(inputDirection);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                inputDirection.x = 1;
                inputDirection.y = 0;
                player.move(inputDirection);
            }
        }
    }

    @Override
    public boolean keyDown(int i) {
//        switch(i){
//            case Input.Keys.UP:
////            	System.out.println("KEYPRESSED: UP");
//                inputDirection.y = 1;
//                player.move(inputDirection);
//                break;
//            case Input.Keys.DOWN:
////            	System.out.println("KEYPRESSED: DOWN");
//                inputDirection.y = -1;
//                player.move(inputDirection);
//                break;
//            case Input.Keys.LEFT:
////            	System.out.println("KEYPRESSED: LEFT");
//                inputDirection.x = -1;
//                player.move(inputDirection);
//                break;
//            case Input.Keys.RIGHT:
////            	System.out.println("KEYPRESSED: RIGHT");
//                inputDirection.x = 1;
//                player.move(inputDirection);
//                break;
//        }
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        switch(i){
            case Input.Keys.UP:
                if(!Gdx.input.isButtonPressed(Input.Keys.DOWN)){
                    System.out.println("released: UP");
                    inputDirection.y = 0;
                }
                break;
            case Input.Keys.DOWN:
                if(!Gdx.input.isButtonPressed(Input.Keys.UP)){
                    System.out.println("released: DOWN");
                    inputDirection.y = 0;
                }
                break;
            case Input.Keys.LEFT:
                if(!Gdx.input.isButtonPressed(Input.Keys.RIGHT)){
                    System.out.println("released: LEFT");
                    inputDirection.x = 0;
                }
                break;
            case Input.Keys.RIGHT:
                if(!Gdx.input.isButtonPressed(Input.Keys.LEFT)){
                    System.out.println("released: RIGHT");
                    inputDirection.x = 0;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        targetDirection.x = Gdx.input.getX();
        targetDirection.y = Gdx.input.getY();
        mousePosition = GameData.camera.unproject(targetDirection);
        player.clickPosition.x = mousePosition.x - player.body.getPosition().x;
        player.clickPosition.y = mousePosition.y - player.body.getPosition().y;
        player.fireShots();
        System.out.println("Fire direction is:" +player.clickPosition.x+","+player.clickPosition.y);
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
