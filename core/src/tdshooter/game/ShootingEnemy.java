package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by johan on 20.3.2018.
 */

public class ShootingEnemy extends Encounter {

    Texture bulletImage;
    private RandomXS128 random;
    private int shootRNG;
    private int randomNumber = 0;
    private Weapon weapon1;
    private Sound firingSound1;
    private Texture firingImage1;
    private Long cooldownTime =(long) 0;
    private int weaponChoice = 1;
    private int turretCount = 0;
    private int spread = 0;

    public ShootingEnemy(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD,
                         float speed, int turretCount, int spread, long cooldownTime, int shootRNG, int points, Texture image, AssetManager assets) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD, speed, points, image);

        this.turretCount = turretCount;
        this.spread = spread;
        this.cooldownTime = cooldownTime;
        this.shootRNG = shootRNG;

        bulletImage = (Texture) assets.get("Bullets/alien_bullet_test.png");
        firingSound1 = (Sound) assets.get("hitSound.wav");
        firingImage1 = (Texture) assets.get("Bullets/bullet1_small.png");

        weapon1 = new Weapon(this.turretCount, 4, this.cooldownTime, false, this.spread, firingSound1, firingImage1);
    }

    public void shoot(ArrayList<Projectile> projectileList) {

            long randomSeed = TimeUtils.nanoTime();
            random = new RandomXS128(randomSeed);
            randomNumber = random.nextInt(this.shootRNG) + 1;

            if (randomNumber > shootRNG - 1) {

                int plane_mid_x = (int) (hitbox.x + (hitbox.width /2)); //middle of the plane
                int plane_mid_y = (int) (hitbox.y);  // top of the plane
                switch (weaponChoice) {
                    case 0:
                        break;
                    case 1:
                        weapon1.fire(plane_mid_x, plane_mid_y, projectileList);
                        break;
                    default:
                        break;
                }
            }


    }
}
