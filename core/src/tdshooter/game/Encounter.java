package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by leevi on 16.3.2018.
 */

//this class is for all encounters which

public class Encounter extends Destroyable{

    Texture encounterImage;

    public Encounter(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        encounterImage = image;
        this.speed = speed;
    }
    public void update(){
        this.hitbox.y -= this.speed * Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(encounterImage, hitbox.x-3, hitbox.y-3, hitbox.width+6, hitbox.height+6);
    }

    //Flight patterns

    //
}
