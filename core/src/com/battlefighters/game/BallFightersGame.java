package com.battlefighters.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.battlefighters.global.GameData;
import com.battlefighters.screens.SplashScreen;

public class BallFightersGame extends Game {
	SpriteBatch batch;
	Texture img;
    Music music;
    World world;

	public static final String TITLE = "Ball Fighters Extreme v0.1";
	@Override
	public void create () {
        world = new World(new Vector2(0,0), true);//TODO: make sure this doesn't make a new world every time
        GameData.WORLD = world;
        GdxNativesLoader.load();
        Gdx.app.log(TITLE,"create()");
        setScreen(new SplashScreen());
	}

	@Override
	public void render () {
        super.render();
	}

    @Override
    public void dispose(){
        super.dispose();
        Gdx.app.log(TITLE,"dispose()");
    }

    @Override
    public void resize(int width, int height){
        super.pause();
        Gdx.app.log(TITLE,"resize("+width+","+height+")");
    }

    @Override
    public  void pause(){
        super.pause();
    }

    @Override
    public void resume(){
        super.resume();
    }
}
