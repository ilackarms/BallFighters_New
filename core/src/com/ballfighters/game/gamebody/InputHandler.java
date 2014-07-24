package com.ballfighters.game.gamebody;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.players.Player;
import com.ballfighters.game.players.SwordGuy;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/2/2014.
 */
public class InputHandler implements GestureDetector.GestureListener, InputProcessor {

    public Vector2 inputDirection;
    public Vector3 targetDirection;
    public Player player;
    public Vector3 mousePosition;
    public Boolean shieldDelay;

    public InputHandler(Player player){//todo: make the constructor accept a set of keys as the up/down/left/right input
        inputDirection = new Vector2(0,0);
        this.player = player;
        targetDirection = new Vector3(0,0,0);
        mousePosition = new Vector3(0,0,0);
        shieldDelay = false;

        //set Back button handler
        Gdx.input.setCatchBackKey(true);
    }


    public void directionHandler(){
        //Android
        if(Gdx.app.getType()== Application.ApplicationType.Android) {
            inputDirection.x = 0;
            inputDirection.y = 0;
            inputDirection.x = Gdx.input.getAccelerometerY();
            inputDirection.y = -1*Gdx.input.getAccelerometerX()+5;
            player.move(inputDirection);
//            System.out.println("X TILT ="+inputDirection.x+"             Y TILT ="+inputDirection.y);
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
        public boolean touchDown(float v, float v2, int i, int i2) {
            return false;
        }

        @Override
        public boolean tap(float v, float v2, int i, int i2) {
            targetDirection.x = Gdx.input.getX();
            targetDirection.y = Gdx.input.getY();
            mousePosition = GameData.camera.unproject(targetDirection);
            player.clickPosition.x = mousePosition.x - player.body.getPosition().x;
            player.clickPosition.y = mousePosition.y - player.body.getPosition().y;
            player.fireShots();
            return false;
        }

        @Override
        public boolean longPress(float v, float v2) {
            if(!shieldDelay) {
                Gdx.input.vibrate(50);
                targetDirection.x = Gdx.input.getX();
                targetDirection.y = Gdx.input.getY();
                mousePosition = GameData.camera.unproject(targetDirection);
                player.clickPosition.x = mousePosition.x - player.body.getPosition().x;
                player.clickPosition.y = mousePosition.y - player.body.getPosition().y;
                player.shield();
                if(!(player instanceof SwordGuy)) shieldDelay = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        shieldDelay = false;
                    }
                },5);
            }
            return false;
        }

        @Override
        public boolean fling(float v, float v2, int i) {
// todo: find something to do with flings
//    Vector2 flingVelocity = MyMathStuff.toUnit(new Vector2(v,v2));
//            flingVelocity.x*=900;
//            flingVelocity.y*=-900;
//            player.body.setLinearVelocity(flingVelocity);
            return false;
        }

        @Override
        public boolean pan(float v, float v2, float v3, float v4) {
            return false;
        }

        @Override
        public boolean panStop(float v, float v2, int i, int i2) {
            return false;
        }

        @Override
        public boolean zoom(float v, float v2) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 vector2, Vector2 vector22, Vector2 vector23, Vector2 vector24) {
            return false;
        }

    public boolean keyDown(int i) {
        if(i == Input.Keys.BACK){
            //do nothing?
            System.out.println("TRY AGAIN FUCKER!!");
        }
        return false;
    }

    public boolean keyUp(int i) {
        return false;
    }

    public boolean keyTyped(char c) {
        return false;
    }

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

    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    public boolean scrolled(int i) {
        return false;
    }
}
