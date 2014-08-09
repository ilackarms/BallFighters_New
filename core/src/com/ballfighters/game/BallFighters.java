package com.ballfighters.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.ballfighters.global.GameData;
import com.ballfighters.screens.GameOverScreen;
import com.ballfighters.screens.SplashScreen;

public class BallFighters extends Game {
	SpriteBatch batch;
	Texture img;
    Music music;
    World world;

	public static final String TITLE = "Ball Fighters Extreme v0.1";
	@Override
	public void create () {

        GdxNativesLoader.load();
        Gdx.app.log(TITLE,"create()");
        setScreen(new SplashScreen());

        //set Back button handler
//        Gdx.input.setCatchBackKey(true);

        if(Gdx.input.getInputProcessor()==null) {
            Gdx.input.setInputProcessor(new InputProcessor() {
                @Override
                public boolean keyDown(int i) {
                    if ( i == Input.Keys.BACK) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(GameData.screen));
                    }
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
            });
        }
	}

	@Override
	public void render () {
        super.render();


//        if(Gdx.input.getInputProcessor()!=null && Gdx.input.getInputProcessor().keyUp(Input.Keys.BACK)){
//            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(GameData.screen));
//        }

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
        GameData.screen.resize(width, height);
		
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
