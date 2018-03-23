package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by leevi on 16.3.2018.
 */

class Projectile extends Collidable{

    Texture bulletImage;
    int damage = 0;
    int verticalSpeed = 0;

    public Projectile(float hitbox_x, float hitbox_y, float hitbox_width, float hitbox_height, int damage, int speed, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);
        this.damage = damage;
        this.speed = speed;
        this.bulletImage = image;
    }

    public Projectile(float hitbox_x, float hitbox_y, float hitbox_width, float hitbox_height, int damage, int speed, int verticalSpeed, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);
        this.damage = damage;
        this.speed = speed;
        this.bulletImage = image;
        this.verticalSpeed = verticalSpeed;
    }

    public Projectile(Projectile bulletModel) {
        super(bulletModel.hitbox.x, bulletModel.hitbox.y, bulletModel.hitbox.width, bulletModel.hitbox.height);
        this.damage = bulletModel.damage;
        this.speed = bulletModel.speed;
        this.bulletImage = bulletModel.bulletImage;
        this.verticalSpeed = bulletModel.verticalSpeed;
    }

    public void update() {
        this.hitbox.y += this.speed * Gdx.graphics.getDeltaTime();
        this.hitbox.x += this.verticalSpeed * Gdx.graphics.getDeltaTime();
    }

    public void setPosition(int x, int y){
        this.hitbox.x = x;
        this.hitbox.y = y;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(bulletImage, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }
}
