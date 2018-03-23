package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by johan on 22.3.2018.
 */

public class Item extends Collidable {

    Texture itemTexture;
    private float[] items;
    private int itemIndex;
    private float heal = 0;
    private float flightSpeed = 0;
    private float projectile = 0;
    private float currency = 0;

    public Item(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int speed, int itemIndex, Texture itemTexture) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);

        this.itemIndex = itemIndex;
        this.itemTexture = itemTexture;
        this.speed = speed;
        items = new float[4];


    }
    public float[] getStats() {
        if (itemIndex == 1) {
            heal = 50;
        }
        else if (itemIndex == 2) {
            flightSpeed = 100f;
        }
        else if (itemIndex == 3){
            projectile = 1;
        }
        else if (itemIndex == 4){
            currency = 10;
        }
        items[0] = heal;
        items[1] = flightSpeed;
        items[2] = projectile;
        items[3] = currency;
            return items;

    }
    public void update(){
        this.hitbox.y -= this.speed * Gdx.graphics.getDeltaTime();
    }
}
