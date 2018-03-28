package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by johan on 22.3.2018.
 */

public class Item extends Collidable {

    Texture itemTexture;
    private float[] items;

    public Item(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int speed, int itemIndex, Texture itemTexture) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);
        this.itemTexture = itemTexture;
        this.speed = speed;
        items = new float[4];

        if (itemIndex == 1) {
            items[0] = 10;  // heal
        }
        else if (itemIndex == 2) {
            items[1] = 10; // Cooldown Reduction
        }
        else if (itemIndex == 3){
            items[2] = 1;  // Projectilecount / turretcount
        }
        else if (itemIndex == 4){
            items[3] = 10; // Currency
        }
    }
    public float[] getStats() {
        return items;
    }
    public void update(){
        this.hitbox.y -= this.speed * Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(itemTexture, hitbox.x, hitbox.y, hitbox.getWidth(), hitbox.getHeight());
    }

    public void setPosition(float x, float y) {
        this.hitbox.x = (x - (this.hitbox.width/2) );
        this.hitbox.y = (y - (this.hitbox.height/2) );
    }
}
