package tdshooter.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by leevi on 16.3.2018.
 */
//luokkaan törmäyksen tarkistus, joka palauttaa boolean-arvon
// sisältää hitboxin(t)

public class Collidable {

    public Rectangle hitbox;

    public Collidable (int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height ){
        hitbox = new Rectangle();
        hitbox.x = hitbox_x;
        hitbox.y = hitbox_y;
        hitbox.width = hitbox_width;
        hitbox.height = hitbox_height;
    }

    public boolean collisionCheck(Rectangle other_hitbox){
        if (other_hitbox.overlaps(hitbox)){
            return true;
        } else {
            return false;
        }
    }
//
//    public float getX(){
//        return hitbox.x;
//    }
//
//    public float getY(){
//        return hitbox.y;
//    }
//    public float getWidth(){
//        return hitbox.width;
//    }
//    public float getHeight(){
//        return hitbox.height;
//    }
}
