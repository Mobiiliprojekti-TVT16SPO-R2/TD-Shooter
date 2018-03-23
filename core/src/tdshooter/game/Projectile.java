package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by leevi on 16.3.2018.
 */

class Projectile extends Collidable{

    Texture bulletImage;
    int damage = 0;
    int verticalSpeed = 0;

    public Projectile(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int damage, int speed, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);
        this.damage = damage;
        this.speed = speed;
        bulletImage = image;
    }
    public Projectile(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int damage, int speed, int verticalSpeed, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);
        this.damage = damage;
        this.speed = speed;
        bulletImage = image;
        this.verticalSpeed = verticalSpeed;
    }
    public void update() {
        this.hitbox.y += this.speed * Gdx.graphics.getDeltaTime();
        this.hitbox.x += this.verticalSpeed * Gdx.graphics.getDeltaTime();
    }
}
