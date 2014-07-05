package com.ballfighters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ballfighters.game.world.BallWorld;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 6/28/2014.
 */
public class TestBattleScreen implements Screen {

    private BallWorld ballWorld;
    private Sprite background;
    private SpriteBatch batch;
    private int track;
    public void render(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        background.draw(batch);
        ballWorld.update();
        ballWorld.render();

//        loopMusic();
    }
    public void show(){
    	GameData.screen = this;
        track = 0;
        batch = new SpriteBatch();
        background = new Sprite(new Texture(Gdx.files.internal("Backgrounds/TwinklingStars.gif")));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	        
        ballWorld = new BallWorld(batch);
    }
    public void hide(){
        dispose();
    }
    public void pause(){

    }
    public void resume(){

    }
    public void resize(int width, int height){
		ballWorld.camera.viewportHeight = height/4f;
		ballWorld.camera.viewportWidth = width/4f;
		ballWorld.camera.update();
    }
    public void dispose(){

    }

    private void loopMusic(){
        if(!GameData.music.isPlaying()){
            switch (track%5) {
                case 0:
                    GameData.playMusic("Music/NoIdea.ogg");
                case 1:
                    GameData.playMusic("Music/MainMenuMusic.ogg");
                case 2:
                    GameData.playMusic("Music/Dosk.ogg");
                case 3:
                    GameData.playMusic("Music/LastWader.ogg");
                case 4:
                    GameData.playMusic("Music/Queen.mp3");
            }
            track++;
        }
    }
}
