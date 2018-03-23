package tdshooter.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by leevi on 16.3.2018.
 */

public class Destroyable extends Collidable {

    private int hitPoints = 0;
    private int hitDamage = 0;

    public Destroyable(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD){
        super(hitbox_x,hitbox_y,hitbox_width,hitbox_height);
        hitPoints = hitP;
        hitDamage = hitD;
    }

    public void getsDamage(int damageAmount){
        hitPoints -= damageAmount;
    }

    public void collidesWith(Destroyable collider){
        this.getsDamage(collider.hitDamage);
    }

    public boolean isDestroyed(){
        if (hitPoints < 1){
            return true;
        } else{
            return false;
        }
    }

    public int getHitPoints() {
        return hitPoints;
    }
}
