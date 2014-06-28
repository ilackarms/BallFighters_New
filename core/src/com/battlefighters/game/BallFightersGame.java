package com.battlefighters.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.battlefighters.global.GameData;
import com.battlefighters.screens.SplashScreen;

public class BallFightersGame extends Game {
	SpriteBatch batch;
	Texture img;
    Music music;

	public static final String TITLE = "Ball Fighters Extreme v0.1";
	@Override
	public void create () {
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
