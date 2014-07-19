package com.ballfighters.global;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ballfighters.game.gamebody.Animator;
import com.ballfighters.game.players.Player;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 6/26/2014.
 */
public final class GameData {
    public static Music music;
    public static float VOLUME = 0.20f;
    public static Sprite BLACKSCREEN =new Sprite(new Texture(Gdx.files.internal("Backgrounds/blackScreen.png")));
    public static World WORLD;
    public static SpriteBatch batch;
    public static Screen screen;
    public static Camera camera;
    public static Player PLAYER;
    public static Animator ANIMATEDBG;
    public static int PLAYER_CHOICE;

    public static final int LITTLE_BOO=0;
    public static final int SWORD_GUY=1;


    public static final Vector2 PLAYER_1_HEALTH_BAR_LOCATION = new Vector2(5,Gdx.graphics.getHeight()-25);
    public static final Vector2 HEALTH_BAR_SIZE = new Vector2(Gdx.graphics.getWidth()/6,Gdx.graphics.getHeight()/35);
    public static Sprite PLAYER_1_HEALTH_BAR = new Sprite(new Texture(Gdx.files.internal("Sprites/HealthBar.png")));
    public static Sprite PLAYER_1_HEALTH_BAR_BOX = new Sprite(new Texture(Gdx.files.internal("Sprites/HealthBarBox.png")));

    public static final Vector2 PLAYER_2_HEALTH_BAR_LOCATION = new Vector2(Gdx.graphics.getWidth()*5/6,Gdx.graphics.getHeight()-25);
    public static Sprite PLAYER_2_HEALTH_BAR = new Sprite(new Texture(Gdx.files.internal("Sprites/HealthBar.png")));
    public static Sprite PLAYER_2_HEALTH_BAR_BOX = new Sprite(new Texture(Gdx.files.internal("Sprites/HealthBarBox.png")));


    public static ArrayList<AnimationPackage> staticAnimations = new ArrayList<AnimationPackage>();

    public static Music playMusic(String filename){
        GameData.music.stop();
        GameData.music.dispose();
        GameData.music = Gdx.audio.newMusic(Gdx.files.internal(filename));
        GameData.music.setVolume(GameData.VOLUME);
        GameData.music.play();
        return GameData.music;
    }

    public static void fadeIn(){

    }

    public static void initializeHealthBars(){
        PLAYER_1_HEALTH_BAR.setPosition(PLAYER_1_HEALTH_BAR_LOCATION.x,PLAYER_1_HEALTH_BAR_LOCATION.y);
        PLAYER_1_HEALTH_BAR.setSize(HEALTH_BAR_SIZE.x,HEALTH_BAR_SIZE.y);
        PLAYER_1_HEALTH_BAR_BOX.setPosition(PLAYER_1_HEALTH_BAR_LOCATION.x,PLAYER_1_HEALTH_BAR_LOCATION.y);
        PLAYER_1_HEALTH_BAR_BOX.setSize(HEALTH_BAR_SIZE.x,HEALTH_BAR_SIZE.y);
        PLAYER_2_HEALTH_BAR.setPosition(PLAYER_2_HEALTH_BAR_LOCATION.x,PLAYER_2_HEALTH_BAR_LOCATION.y);
        PLAYER_2_HEALTH_BAR.setSize(HEALTH_BAR_SIZE.x,HEALTH_BAR_SIZE.y);
        PLAYER_2_HEALTH_BAR_BOX.setPosition(PLAYER_2_HEALTH_BAR_LOCATION.x,PLAYER_2_HEALTH_BAR_LOCATION.y);
        PLAYER_2_HEALTH_BAR_BOX.setSize(HEALTH_BAR_SIZE.x,HEALTH_BAR_SIZE.y);
    }

    public static void renderStaticAnimations(){
        for(AnimationPackage animationPackage : staticAnimations){
            if(animationPackage.playing) {
                animationPackage.update();
                Sprite sprite = new Sprite(animationPackage.animator.currentFrame);
                sprite.setSize(animationPackage.width,animationPackage.height);
                sprite.setPosition(animationPackage.position.x, animationPackage.position.y);
                sprite.draw(batch);
            }
        }
    }


}
