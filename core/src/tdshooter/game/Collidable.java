package tdshooter.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by leevi on 16.3.2018.
 */
//luokkaan törmäyksen tarkistus, joka palauttaa boolean-arvon
// sisältää hitboxin(t)

public class Collidable {

    protected float speed;
    protected float acceleration;
    protected float maxSpeed;

    public Rectangle hitbox;

    public Collidable (float hitbox_x, float hitbox_y, float hitbox_width, float hitbox_height ){
        hitbox = new Rectangle();
        hitbox.x = hitbox_x;
        hitbox.y = hitbox_y;
        hitbox.width = hitbox_width;
        hitbox.height = hitbox_height;
    }

    public boolean overlaps(Collidable collidable)
    {
        return collidable.hitbox.overlaps(hitbox);
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
