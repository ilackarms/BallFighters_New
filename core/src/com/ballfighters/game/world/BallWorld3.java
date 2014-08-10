package com.ballfighters.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ballfighters.game.gamebody.Animator;
import com.ballfighters.game.gamebody.GamePolygon;
import com.ballfighters.game.gamebody.UserDataBundle;
import com.ballfighters.game.players.*;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;

/**
 * Created by Dell_Owner on 6/29/2014.
 */
public class BallWorld3 {
	private final float TIME_STEP = 1/30f;
	private final int VELOCITY_ITERATIONS = 8, POSITION_ITERATIONS = 3;
	public Player player1, player2;
    public Camera camera;
    public Box2DDebugRenderer debugRenderer;
    public Array<Body> worldBodies;
    public Array<Joint> worldJoints;
    public World world;
    public SpriteBatch batch;
    GamePolygon leftWall, rightWall, floor, ceiling;

    public BallWorld3(SpriteBatch batch){
        world = new World(new Vector2(0,0), true);//TODO: make sure this doesn't make a new world every time
        GameData.WORLD = world;

        switch (GameData.PLAYER_CHOICE){
            case GameData.LITTLE_BOO:
                player1 = new LittleBoo(new Vector2(-40,10));
                break;
            case GameData.SWORD_GUY:
                player1 = new SwordGuy(new Vector2(-40,10));
                break;
            case GameData.LASER_GUY:
                player1 = new LaserGuy(new Vector2(-40,10));
                break;
            case GameData.DEATH_GUY:
                player1 = new DeathGuy(new Vector2(-40,10));
                break;
            case GameData.BOMB_GUY:
                player1 = new BombGuy(new Vector2(-40,10));
                break;
            case GameData.PLASMA_GUY:
                player1 = new PlasmaGuy(new Vector2(-40,10));
                break;
            case GameData.LIGHTNING_GUY:
                player1 = new LightningGuy(new Vector2(-40,10));
                break;
            case GameData.FIRE_GUY:
                player1 = new FireGuy(new Vector2(-40,10));
                break;
        }

        player2 = new LaserGuyAI(new Vector2(40,-10));
        GameData.PLAYER_2 = player2;

        camera = new OrthographicCamera(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        GameData.camera = camera;
        GameData.PLAYER_1 = player1;
        GameData.ANIMATEDBG =new Animator("Backgrounds/8bitplanet.png",4,2,.125f);
        debugRenderer = new Box2DDebugRenderer();
        worldBodies = new Array<Body>();
        worldJoints = new Array<Joint>();
        this.batch = batch;
        world = GameData.WORLD;

        leftWall =  new GamePolygon(MyMathStuff.convertTo2D(GameData.camera.unproject(new Vector3(Gdx.graphics.getWidth()*0.25f,0f,0f))), new Vector2[]{
                new Vector2(-1f, -1000f),
                new Vector2(-1f, 1000f),
                new Vector2(1f, -1000f),
                new Vector2(1f, 1000f)});
        rightWall=  new GamePolygon(MyMathStuff.convertTo2D(GameData.camera.unproject(new Vector3(0f,Gdx.graphics.getHeight()*0.25f,0f))), new Vector2[] {
                new Vector2(-2000f,-1f),
                new Vector2(-2000f,1f),
                new Vector2(2000f,-1f),
                new Vector2(2000f,1f)});
        floor=  new GamePolygon(MyMathStuff.convertTo2D(GameData.camera.unproject(new Vector3(0f,Gdx.graphics.getHeight()*0.75f,0f))), new Vector2[] {
                new Vector2(-2000f,-1f),
                new Vector2(-2000f,1f),
                new Vector2(2000f,-1f),
                new Vector2(2000f,1f)});
        ceiling=  new GamePolygon(MyMathStuff.convertTo2D(GameData.camera.unproject(new Vector3(Gdx.graphics.getWidth()*0.75f,0f,0f))), new Vector2[] {
                new Vector2(-1f,-1000f),
                new Vector2(-1f,1000f),
                new Vector2(1f,-1000f),
                new Vector2(1f,1000f)});


        //Contact Listener
        world.setContactListener(new BallContactListener());
    }


    public void update(){

    	player1.update();
    	player2.update();

        GameData.WORLD.getBodies(worldBodies);
        GameData.WORLD.getJoints(worldJoints);

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

        //for deletion: remember, the update method has to be called to update the setUserData() for the body!
        for(Joint joint : worldJoints){
            if(joint.getUserData()!=null && joint.getUserData() instanceof UserDataBundle){
                UserDataBundle bundle = (UserDataBundle) joint.getUserData();
                if (bundle.flaggedForDeletion){
                    GameData.WORLD.destroyJoint(joint);
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
                if(bundle.draw) {
                    Sprite sprite = bundle.sprite;
                    sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                    if (bundle.rotatable) {
                        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                    }
                    sprite.draw(batch);
                }
            }
        }

        GameData.renderStaticAnimations();

        batch.setProjectionMatrix(camera.combined);

        batch.end();

        //debug
//        debugRenderer.render(world, camera.combined);
    }

    public void dispose(){
        player1.body.destroyFixture(player1.fixture);
        player2.body.destroyFixture(player2.fixture);
        player1.kill();
        player2.kill();
    }

}
