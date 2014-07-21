package com.ballfighters.game.players;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Timer;
import com.ballfighters.game.gamebody.*;
import com.ballfighters.global.AnimationPackage;
import com.ballfighters.global.GameData;
import com.ballfighters.math.MyMathStuff;
import com.ballfighters.screens.GameOverScreen;
import com.ballfighters.screens.TestBattleScreen;
import com.ballfighters.tween.BallTween;
import com.ballfighters.tween.SpriteAccessor;

import java.util.ArrayList;

/**
 * Created by Dell_Owner on 7/19/2014.
 */
public class LaserGuyAI extends Player {


    protected InputHandler inputHandler;
    protected float radius = 5.5f;
    protected float density = 5f;
    protected float restitution = 0.5f;
    public static final int MAX_HEALTH = 150;

    protected AIHandlerSwordGuy aiInputHandler;

    protected BallTween tween;

    public LaserGuyAI(Vector2 position) {

        this.position = position;
        animator = new Animator("Sprites/SwordGuy.png", 4, 4);
        inputDirection = new Vector2(0,0);
        body = createBody();
        health = MAX_HEALTH;
        spriteHeight = 13f;
        spriteWidth = 13f;
        dataBundle = createUserDataBundle();
        clickPosition = new Vector3(0,0,0);
        ACCELERATION = 60000f;
        tweenList = new ArrayList<BallTween>();

        aiInputHandler = new AIHandlerSwordGuy(this);

        tween = null;
        lastPosition = body.getPosition();

    }

    protected UserDataBundle createUserDataBundle(){
        UserDataBundle returnDataBundle = new UserDataBundle();
        returnDataBundle.baseObject = this;
        returnDataBundle.flaggedForDeletion = false;
        returnDataBundle.health = health;
        returnDataBundle.rotatable = false;
        returnDataBundle.isPlayer = true;
        return returnDataBundle;
    }

    protected Body createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        body = GameData.WORLD.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);
        shape.dispose();
        body.setUserData(dataBundle);
        body.setTransform(position,0);
        return body;
    }

    @Override
    public void update(){
        super.update();
        aiInputHandler.updateDirections();
        animate();
        if(tween!=null){
            tween.update(sprite);
        }
        inputDirection = aiInputHandler.inputDirection;
        clickPosition.x = aiInputHandler.targetDirection.x;
        clickPosition.y = aiInputHandler.targetDirection.y;
        move(inputDirection);
        fireShots();
    }

    @Override
    public void move(Vector2 inputDirection){
        if(inputDirection.len()!=0){
            inputDirection.x =  inputDirection.x * ACCELERATION;
            inputDirection.y = inputDirection.y * ACCELERATION;
            body.applyForceToCenter(inputDirection, true);
        }
    }

    Boolean fireShotOnCoolDown = false;
    @Override
    public void fireShots(){
        if(!fireShotOnCoolDown && aiInputHandler.state!=AIInputHandlerLittleBoo.HESITATING) {

            int rand = MathUtils.random(1,3);
            Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/SwordGuySounds/SwordProjectile" + rand + ".wav"));
            long soundID = fireSound.play();
            fireSound.setVolume(soundID, GameData.VOLUME);
            fireSound.dispose();

            Vector2 swordDisplacement = MyMathStuff.toUnit(new Vector2(this.body.getPosition().x+radius*2*MyMathStuff.toUnit(aiInputHandler.targetDirection).x,
                    this.body.getPosition().x+radius*2*MyMathStuff.toUnit(aiInputHandler.targetDirection).y));
            swordDisplacement.x *= 10;
            swordDisplacement.y *= 10;
            int direction;
            if (swordDisplacement.x >= 0) {
                direction = 1;
            } else {
                direction = -1;
            }
            swordDisplacement.rotate(60 * direction);
            float angle = ((float) Math.atan2(-1 * swordDisplacement.x, swordDisplacement.y));
            new SwordProjectile(this, swordDisplacement, angle);

            tween = new BallTween(animator, BallTween.COLOR, BallTween.Colors.YELLOW, 1.2f).yoyo(1);

            fireShotOnCoolDown=true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    fireShotOnCoolDown = false;
                }
            }, 0.20f);
        }
    }

    @Override
    public void shield(){
        Vector2 shieldDisplacement = MyMathStuff.toUnit(new Vector2(clickPosition.x,clickPosition.y));
        shieldDisplacement.x*=10;
        shieldDisplacement.y*=10;
        float angle = ((float) Math.atan2(shieldDisplacement.y,shieldDisplacement.x));
        new SwordGuyShield(this, shieldDisplacement, angle);

        Gdx.input.vibrate(50);
        Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/WavySound.wav"));
        long soundID = fireSound.play();
        fireSound.setVolume(soundID, GameData.VOLUME);
        fireSound.dispose();

        tween = new BallTween(animator,BallTween.COLOR,BallTween.Colors.BLUE,1.2f).yoyo(1);
    }

    @Override
    public void getHit(Bullet bullet){
        Gdx.input.vibrate(100);
        tween = new BallTween(animator,BallTween.COLOR,BallTween.Colors.RED,2.2f).yoyo(1);
        Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooHit.wav"));
        long soundID = hitSound .play();
        hitSound .setVolume(soundID, GameData.VOLUME);
        hitSound.dispose();

        health-=bullet.damage;
        float ratio = (float) health/ (float) MAX_HEALTH;
        System.out.println(health+"/"+MAX_HEALTH+"="+ratio);
        GameData.PLAYER_2_HEALTH_BAR.setSize(Gdx.graphics.getWidth() / 6 * ratio, GameData.PLAYER_1_HEALTH_BAR.getHeight());
        if(health<0){
            kill();
        }
    }

    @Override
    public void kill(){
        super.kill();
        GameData.playMusic("Music/GameOver.mp3");
        Gdx.input.vibrate(1000);
        Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/LittleBooSounds/LittleBooDeath.wav"));
        long soundID = deathSound.play();
        deathSound .setVolume(soundID, GameData.VOLUME);
        deathSound.dispose();
        lastPosition.x = MyMathStuff.convertTo3D(body.getPosition()).x;
        lastPosition.y = MyMathStuff.convertTo3D(body.getPosition()).y;

        Animator gameOverAnimation = new Animator("Sprites/GameOverAnimation.png",2,5);
        final AnimationPackage staticGameOverAnimation = new AnimationPackage(gameOverAnimation,64*2,32*2);
        staticGameOverAnimation.play();
        staticGameOverAnimation.position=new Vector2(lastPosition);
        GameData.staticAnimations.add(staticGameOverAnimation);

        //NEW GAME ON DEATH!
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                final TestBattleScreen battleScreen = (TestBattleScreen) GameData.screen;
                Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 3).target(1).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        Tween.to(GameData.BLACKSCREEN, SpriteAccessor.ALPHA, 2).target(0).delay(4).start(battleScreen.tweenManager);
                        GameData.screen.dispose();
                        GameData.screen.hide();
                        GameData.staticAnimations.remove(staticGameOverAnimation);
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(GameData.screen));
                    }
                }).start(battleScreen.tweenManager);
            }
        }, 8);

    }

}
