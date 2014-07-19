package com.ballfighters.game.world;

import com.badlogic.gdx.physics.box2d.*;
import com.ballfighters.game.gamebody.Bullet;
import com.ballfighters.game.gamebody.SwordProjectile;
import com.ballfighters.game.gamebody.UserDataBundle;
import com.ballfighters.game.players.Player;
import com.ballfighters.game.players.SwordGuy;

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
        for(int j = 0; j < currentManifold.getNumberOfContactPoints(); j++){
            if(contact.getFixtureA().getBody().getUserData() != null && ((UserDataBundle) contact.getFixtureA().getBody().getUserData()).ghostMode)
                contact.setEnabled(false);
            if(contact.getFixtureB().getUserData() != null && ((UserDataBundle) contact.getFixtureB().getBody().getUserData()).ghostMode)
                contact.setEnabled(false);
        }
        for(int j = 0; j < currentManifold.getNumberOfContactPoints(); j++){
            if(contact.getFixtureA().getBody().getUserData() != null && ((UserDataBundle) contact.getFixtureA().getBody().getUserData()).baseObject instanceof SwordProjectile){
                UserDataBundle bundleA = (UserDataBundle) contact.getFixtureA().getBody().getUserData();
                SwordProjectile swordProjectile = (SwordProjectile) bundleA.baseObject;
                Player swordParent = (Player) swordProjectile.parent;
                if(contact.getFixtureB().getBody().getUserData() != null && ((UserDataBundle) contact.getFixtureB().getBody().getUserData()).baseObject instanceof Player){
                    UserDataBundle bundleB = (UserDataBundle) contact.getFixtureB().getBody().getUserData();
                    Player swordGuy = (Player) bundleB.baseObject;
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
            if ((bundleA.baseObject instanceof Player) && (bundleB.baseObject instanceof Bullet) && !(bundleA.baseObject).equals(((Bullet) bundleB.baseObject).parent)) {
                bundleA.baseObject.getHit((Bullet) bundleB.baseObject);
                bundleB.baseObject.kill();
            }

            if ((bundleB.baseObject instanceof Player) && (bundleA.baseObject instanceof Bullet) && !(bundleB.baseObject).equals(((Bullet) bundleA.baseObject).parent)) {
                bundleB.baseObject.getHit((Bullet) bundleA.baseObject);
                bundleA.baseObject.kill();
            }

//            if ((bundleB.baseObject instanceof Bullet) && (bundleA.baseObject instanceof Bullet)) {
//                bundleA.baseObject.kill();
//                bundleB.baseObject.kill();
//            }

        }
    }
}
