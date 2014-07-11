package com.ballfighters.game.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ballfighters.game.gamebody.Bullet;
import com.ballfighters.game.gamebody.UserDataBundle;
import com.ballfighters.game.players.Player;

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
