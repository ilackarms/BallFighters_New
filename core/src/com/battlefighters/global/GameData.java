package com.battlefighters.global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Dell_Owner on 6/26/2014.
 */
public final class GameData {
    public static Music music;
    public static float VOLUME = 0.05f;
    public static String player;
    public static Sprite BLACKSCREEN =new Sprite(new Texture(Gdx.files.internal("Backgrounds/blackScreen.png")));
    public static World WORLD;

    public static void playMusic(String filename){
        GameData.music.stop();
        GameData.music.dispose();
        GameData.music = Gdx.audio.newMusic(Gdx.files.internal(filename));
        GameData.music.setVolume(GameData.VOLUME);
        GameData.music.play();
    }

    public static void fadeIn(){

    }
}
