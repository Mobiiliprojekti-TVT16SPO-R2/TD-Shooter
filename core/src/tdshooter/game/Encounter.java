package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by leevi on 16.3.2018.
 */

public class Encounter extends Destroyable{

    Texture encounterImage;
    protected int points = 0;
    protected Item itemDrop = null;


    public Encounter(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, int points, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        encounterImage = image;
        this.speed = speed;
        this.points = points;
    }

    public void update(float delta){
        this.hitbox.y -= this.speed * Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(encounterImage, hitbox.x-3, hitbox.y-3, hitbox.width+6, hitbox.height+6);
    }
    public int getPoints() {
        return points;
    }

    public void setPosition(Vector2 position) {this.hitbox.setPosition(position);}

    public void dropItem(ArrayList<Item> items) {
        if (itemDrop != null){
            itemDrop.setPosition(this.hitbox.x + (hitbox.width / 2), this.hitbox.y + hitbox.height / 2);
            items.add(itemDrop);
        }
    }

    public void setItemDrop(Item item){
        this.itemDrop = item;
    }

    //Flight patterns

    //
}
