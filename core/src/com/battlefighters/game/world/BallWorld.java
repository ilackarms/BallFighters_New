package com.battlefighters.game.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.battlefighters.game.gamebody.GamePolygon;
import com.battlefighters.game.gamebody.UserDataBundle;
import com.battlefighters.game.players.LittleBoo;
import com.battlefighters.game.players.Player;
import com.battlefighters.global.GameData;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class BallWorld {
    public Player player1, player2;
    public Camera camera;
    public Box2DDebugRenderer debugRenderer;
    public Array<Body> worldBodies;

    public BallWorld(){
        player1 = new LittleBoo(new Vector2(0,0));
        player2 = new LittleBoo(new Vector2(5,0));
        camera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer();
        worldBodies = new Array<Body>();

        GamePolygon leftWall =  new GamePolygon(new Vector2(-100,0), new Vector2[] {
                new Vector2(-1f,-100f),
                new Vector2(-1f,100f),
                new Vector2(1f,-100f),
                new Vector2(1f,100f)}),
                rightWall=  new GamePolygon(new Vector2(-100,0), new Vector2[] {
                        new Vector2(-1f,-100f),
                        new Vector2(-1f,100f),
                        new Vector2(1f,-100f),
                        new Vector2(1f,100f)}),
                floor=  new GamePolygon(new Vector2(-100,0), new Vector2[] {
                        new Vector2(-1f,-100f),
                        new Vector2(-1f,100f),
                        new Vector2(1f,-100f),
                        new Vector2(1f,100f)}),
                ceiling=  new GamePolygon(new Vector2(-100,0), new Vector2[] {
                        new Vector2(-1f,-100f),
                        new Vector2(-1f,100f),
                        new Vector2(1f,-100f),
                        new Vector2(1f,100f)});
    }

    public void update(){
        player1.update();
        player2.update();
        GameData.WORLD.getBodies(worldBodies);
        for(Body body : worldBodies){
            if(body.getUserData()!=null && body.getUserData() instanceof UserDataBundle){
                UserDataBundle bundle = (UserDataBundle) body.getUserData();
                if (bundle.flaggedForDeletion){
                    bundle.baseObject.body.setActive(false);
                    GameData.WORLD.destroyBody(bundle.baseObject.body);
                }
            }
        }
    }
}
