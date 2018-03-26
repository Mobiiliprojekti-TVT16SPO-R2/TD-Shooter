package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by johan on 20.3.2018.
 */

public class ShootingEnemy extends Encounter {

    Projectile bullet;
    Texture bulletImage;
    Random random;
    private long lastShootTime;
    private int randomNumber = 0;

    public ShootingEnemy(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD, speed, image);
        bulletImage = new Texture(Gdx.files.internal("Bullets/alien_bullet_test.png"));
    }
    public ShootingEnemy(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, int points, Texture image) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD, speed, points, image);

        bulletImage = new Texture(Gdx.files.internal("Bullets/alien_bullet_test.png"));

    }
    public Projectile spawnBullet() {

        bullet = new Projectile((int)this.hitbox.x + 32,(int)this.hitbox.y - 32,
                12, 12, 5, -200, bulletImage);

        return bullet;
    }
    public Projectile spawnBulletType2() {

        bullet = new Projectile((int)this.hitbox.x ,(int)this.hitbox.y - 32,
                12, 12, 5, -200, -50, bulletImage);

        return bullet;
    }
    public Projectile spawnBulletType3() {

        bullet = new Projectile((int)this.hitbox.x + 64,(int)this.hitbox.y - 32,
                12, 12, 5, -200, 50, bulletImage);

        return bullet;
    }
    public void shoot(ArrayList<Projectile> projectileList) {
        if (TimeUtils.nanoTime() - lastShootTime > 2100000000) {
            long randomSeed = TimeUtils.nanoTime();
            random = new Random(randomSeed);
            randomNumber = random.nextInt(300) + 1;
            if (randomNumber >= 299) {

                Projectile bullet = spawnBullet();
                Projectile bullet2 = spawnBulletType2();
                Projectile bullet3 = spawnBulletType3();
                projectileList.add(bullet);
                projectileList.add(bullet2);
                projectileList.add(bullet3);

                lastShootTime = TimeUtils.nanoTime();
            }
        }

    }
}
