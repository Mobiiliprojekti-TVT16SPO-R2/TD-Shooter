package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by leevi on 16.3.2018.
 */

public class Encounter extends Destroyable{

    Texture encounterImage;
    protected int points = 0;
    protected int itemDrop = 0;

    public Encounter(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        encounterImage = image;
        this.speed = speed;
        points = 25;
    }
    public Encounter(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, int points, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        encounterImage = image;
        this.speed = speed;
        this.points = points;
    }
    public Encounter(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, int points, int itemDrop, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        encounterImage = image;
        this.speed = speed;
        this.points = points;
        this.itemDrop = itemDrop;
    }
    public void update(){
        this.hitbox.y -= this.speed * Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(encounterImage, hitbox.x-3, hitbox.y-3, hitbox.width+6, hitbox.height+6);
    }
    public int getPoints() {
        return points;
    }

    public void setPosition(Vector2 position) {this.hitbox.setPosition(position);}

    public void dropItem(ArrayList<Item> items, int scrollSpeed) {
        if (itemDrop > 0){
            Item item = new Item((int) this.hitbox.x, (int) this.hitbox.y, 48, 48, scrollSpeed, itemDrop);
            items.add(item);
        }
    }

    //Flight patterns

    //
}
