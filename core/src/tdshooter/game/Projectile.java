package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by leevi on 16.3.2018.
 */

class Projectile extends Collidable{

    Texture bulletImage;
    int damage = 0;
    int speed = 0;

    public Projectile(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int damage, int speed, String imageName) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);
        this.damage = damage;
        this.speed = speed;
        bulletImage = new Texture(Gdx.files.internal(imageName));
    }



}
