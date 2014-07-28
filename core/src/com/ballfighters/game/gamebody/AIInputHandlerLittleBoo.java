package com.ballfighters.game.gamebody;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.players.Player;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/7/2014.
 */
public class AIInputHandlerLittleBoo {
    public Vector2 inputDirection;
    public Vector2 targetDirection;
    public Vector3 mousePosition;
    public Player aiPlayer;
    public int state;
    public int stateDuration;

    public static final int CHARGING = 0;
    public static final int RETREATING = 1;
    public static final int SIDESTEPPING_RIGHT = 2;
    public static final int SIDESTEPPING_LEFT = 3;
    public static final int HESITATING = 4;
    public static final int TRACKING = 5;


    public AIInputHandlerLittleBoo(Player aiPlayer) {//todo: make the constructor accept a set of keys as the up/down/left/right input
        this.aiPlayer = aiPlayer;
        inputDirection = new Vector2(0, 0);
        targetDirection = new Vector2(0, 0);
        mousePosition = new Vector3(0, 0, 0);
        stateDuration = 4;
        changeState();
    }

    protected void changeState(){
        if (aiPlayer.body.isActive()) {
            state = MathUtils.random(0,5);
            System.out.println("STATE: "+state);
            //change state after 5 SECONDS!
            Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        changeState();
                    }
            }, stateDuration/4);
        }
    }


    public void updateDirections() {
        switch (state) {
            case CHARGING:
                inputDirection.x = (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x);
                inputDirection.y = (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y);
                targetDirection.x = inputDirection.x * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = inputDirection.y * MathUtils.random(0.8f, 1.2f);
                break;
            case RETREATING:
                inputDirection.x = -1 * (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x);
                inputDirection.y = -1 * (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y);
                targetDirection.x = -1 * inputDirection.x * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = -1 * inputDirection.y * MathUtils.random(0.8f, 1.2f);
                break;
            case SIDESTEPPING_RIGHT:
                targetDirection.x = inputDirection.x * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = inputDirection.y * MathUtils.random(0.8f, 1.2f);
                inputDirection = MyMathStuff.findPerpendicularVectorOne(GameData.PLAYER_1.inputDirection);
                break;
            case SIDESTEPPING_LEFT:
                targetDirection.x = inputDirection.x * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = inputDirection.y * MathUtils.random(0.8f, 1.2f);
                inputDirection = MyMathStuff.findPerpendicularVectorTwo(GameData.PLAYER_1.inputDirection);
                break;
            case HESITATING:
                inputDirection.x=0;
                inputDirection.y=0;
                targetDirection.x = inputDirection.x * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = inputDirection.y * MathUtils.random(0.8f, 1.2f);
                break;
            case TRACKING:
                targetDirection.x = (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x) * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y) * MathUtils.random(0.8f, 1.2f);
                inputDirection = GameData.PLAYER_1.body.getLinearVelocity();
                break;
        }
    }


 }
