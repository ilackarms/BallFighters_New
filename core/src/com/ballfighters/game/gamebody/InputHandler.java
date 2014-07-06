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
        //Android
        if(Gdx.app.getType()== Application.ApplicationType.Android) {
            inputDirection.x = 0;
            inputDirection.y = 0;
            inputDirection.x = Gdx.input.getAccelerometerY();
            inputDirection.y = -1*Gdx.input.getAccelerometerX()+5;
            player.move(inputDirection);
            System.out.println("X TILT ="+inputDirection.x+"             Y TILT ="+inputDirection.y);
        }

        //Desktop
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
        return false;
    }

    @Override
    public boolean keyUp(int i) {
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
