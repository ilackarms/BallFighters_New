package com.ballfighters.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ballfighters.game.gamebody.*;
import com.ballfighters.game.players.LittleBooAI;
import com.ballfighters.game.players.Player;
import com.ballfighters.global.GameData;

/**
 * Created by Dell_Owner on 7/9/2014.
 */
public class BallContactListener implements ContactListener {

    public BallContactListener(){
        //eh
    }

    @Override
    public void beginContact(Contact contact){

    }

    @Override
    public void endContact(Contact contact){

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold){
        WorldManifold currentManifold = contact.getWorldManifold();

        //laser thing doesnt contact
        for(int j = 0; j < manifold.getPoints().length; j++) {
            if (contact.getFixtureA().getBody().getUserData() != null &&
                    ((UserDataBundle) contact.getFixtureA().getBody().getUserData()).baseObject instanceof LaserGuyProjectileCharge) {
                contact.setEnabled(false);
            }
            if (contact.getFixtureB().getBody().getUserData() != null &&
                    ((UserDataBundle) contact.getFixtureB().getBody().getUserData()).baseObject instanceof LaserGuyProjectileCharge) {
                contact.setEnabled(false);
            }
        }

        //ghost mode
        for(int j = 0; j < currentManifold.getNumberOfContactPoints(); j++){
            if(contact.getFixtureA().getBody().getUserData() != null && ((UserDataBundle) contact.getFixtureA().getBody().getUserData()).ghostMode) {
                contact.setEnabled(false);
            }
            if(contact.getFixtureB().getUserData() != null && ((UserDataBundle) contact.getFixtureB().getBody().getUserData()).ghostMode) {
                contact.setEnabled(false);
            }
        }

        //sword contact with player
        for(int j = 0; j < currentManifold.getNumberOfContactPoints(); j++){
            if(contact.getFixtureA().getBody().getUserData() != null && ((UserDataBundle) contact.getFixtureA().getBody().getUserData()).baseObject instanceof SwordProjectile){
                UserDataBundle bundleA = (UserDataBundle) contact.getFixtureA().getBody().getUserData();
                SwordProjectile swordProjectile = (SwordProjectile) bundleA.baseObject;
                Player swordParent = (Player) swordProjectile.parent;
                if(contact.getFixtureB().getBody().getUserData() != null && ((UserDataBundle) contact.getFixtureB().getBody().getUserData()).baseObject instanceof Player){
                    UserDataBundle bundleB = (UserDataBundle) contact.getFixtureB().getBody().getUserData();
                    Player swordGuy = (Player) bundleB.baseObject;
                    //if player is parent of sword, no contact
                    if(swordParent.equals(swordGuy)){
                        contact.setEnabled(false);
                    }
                }
            }
            if(contact.getFixtureB().getBody().getUserData() != null && ((UserDataBundle) contact.getFixtureB().getBody().getUserData()).baseObject instanceof SwordProjectile){
                UserDataBundle bundleB = (UserDataBundle) contact.getFixtureB().getBody().getUserData();
                SwordProjectile swordProjectile = (SwordProjectile) bundleB.baseObject;
                Player swordParent = (Player) swordProjectile.parent;
                if(contact.getFixtureA().getBody().getUserData() != null && ((UserDataBundle) contact.getFixtureA().getBody().getUserData()).baseObject instanceof Player){
                    UserDataBundle bundleA = (UserDataBundle) contact.getFixtureA().getBody().getUserData();
                    Player swordGuy = (Player) bundleA.baseObject;
                    //if player is parent of sword, no contact
                    if(swordParent.equals(swordGuy)) {
                        contact.setEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse){

        UserDataBundle bundleA = null;
        UserDataBundle bundleB = null;

        if(contact.getFixtureA().getBody().getUserData()!=null){
            bundleA = (UserDataBundle) contact.getFixtureA().getBody().getUserData();
        }
        if(contact.getFixtureB().getBody().getUserData()!=null){
            bundleB = (UserDataBundle) contact.getFixtureB().getBody().getUserData();
        }

        if(bundleA!=null && bundleB!=null) {

            //laser on player collision
            if ((bundleA.baseObject instanceof Player) && (bundleB.baseObject instanceof LaserGuyProjectile) && !(bundleA.baseObject).equals(((LaserGuyProjectile) bundleB.baseObject).parent)) {
                bundleA.baseObject.getHit((Bullet) bundleB.baseObject);
                ((LaserGuyProjectile) bundleB.baseObject).killChain();
            }
            //laser on player collision
            if ((bundleB.baseObject instanceof Player) && (bundleA.baseObject instanceof LaserGuyProjectile) && !(bundleB.baseObject).equals(((LaserGuyProjectile) bundleA.baseObject).parent)) {
                bundleB.baseObject.getHit((Bullet) bundleA.baseObject);
                ((LaserGuyProjectile) bundleA.baseObject).killChain();
            }



            //regular bullet on player collision
            if ((bundleA.baseObject instanceof Player) && (bundleB.baseObject instanceof Bullet) && !(bundleB.baseObject instanceof LaserGuyProjectile)
                    && !(bundleA.baseObject).equals(((Bullet) bundleB.baseObject).parent)) {
                bundleA.baseObject.getHit((Bullet) bundleB.baseObject);
                bundleB.baseObject.kill();
            }
            //regular bullet on player collision
            if ((bundleB.baseObject instanceof Player) && (bundleA.baseObject instanceof Bullet) && !(bundleA.baseObject instanceof LaserGuyProjectile)
                    && !(bundleB.baseObject).equals(((Bullet) bundleA.baseObject).parent)) {
                bundleB.baseObject.getHit((Bullet) bundleA.baseObject);
                bundleA.baseObject.kill();
            }

            //if sword: knock player back
            if(bundleB.baseObject instanceof SwordProjectile){
                Body playerBody = bundleA.baseObject.body;
                Body swordBody = bundleB.baseObject.body;
                Vector2 knockbackDirection = playerBody.getPosition().add(swordBody.getPosition().scl(-1));
                playerBody.setLinearVelocity(knockbackDirection.scl(1000));
            }
            //if sword: knock other thing back
            if(bundleA.baseObject instanceof SwordProjectile){
                Body playerBody = bundleB.baseObject.body;
                Body swordBody = bundleA.baseObject.body;
                Vector2 knockbackDirection = playerBody.getPosition().add(swordBody.getPosition().scl(-1));
                playerBody.setLinearVelocity(knockbackDirection.scl(100));
            }

            //SHIELD COLLISION
            if ((bundleA.baseObject instanceof SwordGuyShield)){
                if((bundleB.baseObject instanceof Bullet) && !((SwordGuyShield) bundleA.baseObject).parent.equals(((Bullet) bundleB.baseObject).parent)){
                    Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/SwordGuySounds/Shield.wav"));
                    long soundID = fireSound.play();
                    fireSound.setVolume(soundID, GameData.VOLUME);
                    fireSound.dispose();

                    bundleB.baseObject.body.setLinearVelocity(bundleB.baseObject.body.getLinearVelocity().x*-1,bundleB.baseObject.body.getLinearVelocity().y*-1);
                    if(bundleB.baseObject instanceof SwordProjectile){
                        ((LaserGuyProjectile) bundleB.baseObject).killChain();
                    }
                }
                if((bundleB.baseObject instanceof SwordProjectile) && !((SwordGuyShield) bundleA.baseObject).parent.equals(((SwordProjectile) bundleB.baseObject).parent)){
                    ((SwordProjectile) bundleB.baseObject).direction*=-1;
                }
            }
            //SHIELD COLLISION
            if ((bundleB.baseObject instanceof SwordGuyShield)){
                if((bundleA.baseObject instanceof Bullet) && !((SwordGuyShield) bundleB.baseObject).parent.equals(((Bullet) bundleA.baseObject).parent)){
                    Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/SwordGuySounds/Shield.wav"));
                    long soundID = fireSound.play();
                    fireSound.setVolume(soundID, GameData.VOLUME);
                    fireSound.dispose();

                    bundleA.baseObject.body.setLinearVelocity(bundleA.baseObject.body.getLinearVelocity().x*-1,bundleA.baseObject.body.getLinearVelocity().y*-1);
                    if(bundleA.baseObject instanceof SwordProjectile){
                        ((LaserGuyProjectile) bundleA.baseObject).killChain();
                    }
                }
                if((bundleA.baseObject instanceof SwordProjectile) && !((SwordGuyShield) bundleB.baseObject).parent.equals(((SwordProjectile) bundleA.baseObject).parent)){
                    ((SwordProjectile) bundleA.baseObject).direction*=-1;
                }
            }

//            if ((bundleB.baseObject instanceof Bullet) && (bundleA.baseObject instanceof Bullet)) {
//                bundleA.baseObject.kill();
//                bundleB.baseObject.kill();
//            }

        }
    }
}
