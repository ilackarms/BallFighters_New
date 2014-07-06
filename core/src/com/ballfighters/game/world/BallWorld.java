package com.ballfighters.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ballfighters.game.gamebody.GamePolygon;
import com.ballfighters.game.gamebody.LittleBooProjectile;
import com.ballfighters.game.gamebody.UserDataBundle;
import com.ballfighters.game.players.LittleBoo;
import com.ballfighters.game.players.Player;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class BallWorld {
	private final float TIME_STEP = 1/30f;
	private final int VELOCITY_ITERATIONS = 8, POSITION_ITERATIONS = 3;
	public Player player1, player2, player3;
    public Camera camera;
    public Box2DDebugRenderer debugRenderer;
    public Array<Body> worldBodies;
    public World world;
    public SpriteBatch batch;
    GamePolygon leftWall, rightWall, floor, ceiling;

    public BallWorld(SpriteBatch batch){
        player1 = new LittleBoo(new Vector2(10,80));
//        player2 = new LittleBoo(new Vector2(12,50));
//        player3= new LittleBoo(new Vector2(12,52));
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        GameData.camera = camera;
        debugRenderer = new Box2DDebugRenderer();
        worldBodies = new Array<Body>();
        this.batch = batch;
        world = GameData.WORLD;

        leftWall =  new GamePolygon(new Vector2(-180,0), new Vector2[] {
                new Vector2(-1f,-100f),
                new Vector2(-1f,100f),
                new Vector2(1f,-100f),
                new Vector2(1f,100f)});
        rightWall=  new GamePolygon(new Vector2(0,85), new Vector2[] {
                new Vector2(-200f,-1f),
                new Vector2(-200f,1f),
                new Vector2(200f,-1f),
                new Vector2(200f,1f)});
        floor=  new GamePolygon(new Vector2(0,-85), new Vector2[] {
                new Vector2(-200f,-1f),
                new Vector2(-200f,1f),
                new Vector2(200f,-1f),
                new Vector2(200f,1f)});
        ceiling=  new GamePolygon(new Vector2(180,0), new Vector2[] {
                new Vector2(-1f,-100f),
                new Vector2(-1f,100f),
                new Vector2(1f,-100f),
                new Vector2(1f,100f)});
    }


    public void update(){

    	player1.update();
//        player2.update();
//        player1.dataBundle.baseObject.kill();
//        littleBooProjectile.update();

//        player3.update();

        GameData.WORLD.getBodies(worldBodies);

        //for deletion: remember, the update method has to be called to update the setUserData() for the body!
        for(Body body : worldBodies){
            if(body.getUserData()!=null && body.getUserData() instanceof UserDataBundle){
                UserDataBundle bundle = (UserDataBundle) body.getUserData();
                bundle.baseObject.update();
                if (bundle.flaggedForDeletion){
                    bundle.baseObject.body.setActive(false);
                    GameData.WORLD.destroyBody(body);
                }
            }
        }

		//step time
		world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        
    }




    public void render(){

        batch.begin();

        for(Body body : worldBodies){
            if(body.getUserData()!=null && body.getUserData() instanceof UserDataBundle){
                UserDataBundle bundle = (UserDataBundle) body.getUserData();
                Sprite sprite = bundle.sprite;
                sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                if (bundle.rotatable){
                    sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                }
                sprite.draw(batch);
            }
        }

        batch.setProjectionMatrix(camera.combined);

        batch.end();

        //debug
//        debugRenderer.render(world, camera.combined);
    }
}
