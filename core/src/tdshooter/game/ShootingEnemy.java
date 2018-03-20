package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by johan on 20.3.2018.
 */

public class ShootingEnemy extends Encounter {

    Projectile bullet;
    Texture bulletImage;
    long lastShootTime;

    public ShootingEnemy(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD, speed, image);

        bulletImage = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));

    }
    public Projectile spawnBullet() {

        bullet = new Projectile((int)this.hitbox.x + 32,(int)this.hitbox.y - 46,
                24, 36, 5, -400, bulletImage);

        return bullet;
    }
    public void shoot(ArrayList<Projectile> projectileList) {
        if (TimeUtils.nanoTime() - lastShootTime > 600000000) {

            Projectile bullet = spawnBullet();
            projectileList.add(bullet);

            lastShootTime = TimeUtils.nanoTime();
        }

    }
}
