package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by johan on 22.3.2018.
 */

public class Item extends Collidable {

    Texture itemTexture;
    private String itemName;
    private float heal = 0;
    private float flightSpeed = 0;

    public Item(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int speed, String itemName, Texture itemTexture) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);

        this.itemName = itemName;
        this.itemTexture = itemTexture;
        this.speed = speed;


    }
    public float getStats() {
        if (itemName == "healtpack") {
            heal = 50;
            return heal;
        }
        else if (itemName == "flightspeed") {
            flightSpeed = 2;
            return flightSpeed;
        }
        else {
            return 0;
        }
    }
    public String getName(){
        return itemName;
    }
    public void update(){
        this.hitbox.y -= this.speed * Gdx.graphics.getDeltaTime();
    }
}
