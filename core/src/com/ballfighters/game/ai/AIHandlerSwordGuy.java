package com.ballfighters.game.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.players.Player;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 7/19/2014.
 */
public class AIHandlerSwordGuy {
    public Vector2 inputDirection;
    public Vector2 targetDirection;
    public Vector3 mousePosition;
    public Player aiPlayer;
    public int state;
    public int stateDuration;

    public static final int CHARGING1 = 0;
    public static final int SIDESTEPPING_RIGHT = 1;
    public static final int SIDESTEPPING_LEFT = 2;
    public static final int TRACKING = 3;
    public static final int HESITATING = 4;
    public static final int CHARGING2 = 5;
    public static final int CHARGING3 = 6;
    public static final int CHARGING4 = 7;
    public static final int CHARGING5 = 8;
    public static final int CHARGING6 = 9;


    public AIHandlerSwordGuy(Player aiPlayer) {
        this.aiPlayer = aiPlayer;
        inputDirection = new Vector2(0, 0);
        targetDirection = new Vector2(0, 0);
        mousePosition = new Vector3(0, 0, 0);
        stateDuration = 4;
        changeState();
    }

    protected void changeState(){
        if (aiPlayer.body.isActive()) {
            state = MathUtils.random(0, 9);
            //change state after 5 SECONDS!
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    changeState();
                    if (state == TRACKING || state == HESITATING) aiPlayer.shield();
                }
            }, stateDuration / 4);
        }
    }


    public void updateDirections() {
        switch (state) {
            case CHARGING1:
            case CHARGING2:
            case CHARGING3:
            case CHARGING4:
            case CHARGING5:
            case CHARGING6:
                inputDirection.x = (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x);
                inputDirection.y = (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y);
                targetDirection.x = (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x) * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y) * MathUtils.random(0.8f, 1.2f);
                break;
            case SIDESTEPPING_RIGHT:
                targetDirection.x = (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x) * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y) * MathUtils.random(0.8f, 1.2f);
                inputDirection = MyMathStuff.findPerpendicularVectorOne(GameData.PLAYER_1.inputDirection);
                break;
            case SIDESTEPPING_LEFT:
                targetDirection.x = (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x) * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y) * MathUtils.random(0.8f, 1.2f);
                inputDirection = MyMathStuff.findPerpendicularVectorTwo(GameData.PLAYER_1.inputDirection);
                break;
            case HESITATING:
                inputDirection.x=0;
                inputDirection.y=0;
                targetDirection.x = (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x) * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y) * MathUtils.random(0.8f, 1.2f);
                break;
            case TRACKING:
                targetDirection.x = (GameData.PLAYER_1.body.getPosition().x - aiPlayer.body.getPosition().x) * MathUtils.random(0.8f, 1.2f);
                targetDirection.y = (GameData.PLAYER_1.body.getPosition().y - aiPlayer.body.getPosition().y) * MathUtils.random(0.8f, 1.2f);
                inputDirection = GameData.PLAYER_1.body.getLinearVelocity();
                break;
        }
    }


}
