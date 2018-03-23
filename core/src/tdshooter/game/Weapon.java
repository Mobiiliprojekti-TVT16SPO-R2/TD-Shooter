package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by leevi on 23.3.2018.
 */

class Weapon {
    int turretCount = 1;
    int projectileType = 1;
    Long cooldownTime = (long) 500000000; // half a second
    Long lastBulletTime = (long )0;
    boolean autoTargetting = false;
    Sound firingSound;
    Texture firingImage;
    Projectile bulletModel,bullet1,bullet2,bullet3,bullet4,bullet5;
    
    Texture bulletImage1;
    Texture bulletImage2;
    Texture bulletImage3;

    public Weapon(int turretCount, int projectileType, Long cooldownTime, boolean autoTargetting, Sound firingSound, Texture firingImage) {
        this.turretCount = turretCount;
        this.projectileType = projectileType;
        this.cooldownTime = cooldownTime;
        this.autoTargetting = autoTargetting;
        this.firingSound = firingSound;
        this.firingImage = firingImage;
        
        //load all bullet textures
        bulletImage1 = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));
        bulletImage2 = new Texture(Gdx.files.internal("Bullets/bullet1.png"));

        //first create bulletmodel by type
        switch (projectileType) {
            case 1:
                bulletModel = new Projectile(0,0, 24, 32, 5, 1000, bulletImage1);
                break;
            case 2:
                bulletModel = new Projectile(0,0, 24, 64, 13, 1400, bulletImage2);
                break;
            case 3:
                bulletModel = new Projectile(0,0, 32, 48, 10, 900, bulletImage2);
                break;
            default: //same as basic
                bulletModel = new Projectile(0,0, 32, 32, 5, 700, bulletImage1);
                break;
        }
    }

    public void fire(int x, int y, ArrayList<Projectile> playerProjectiles) {

        bullet1 = new Projectile(bulletModel);
        bullet2 = new Projectile(bulletModel);
        bullet3 = new Projectile(bulletModel);
        bullet4 = new Projectile(bulletModel);
        bullet5 = new Projectile(bulletModel);
        int fixed_x_pos = x - (int)(bulletModel.hitbox.width / 2);

        if (TimeUtils.nanoTime() - lastBulletTime > cooldownTime){
            switch (turretCount){
                case 1:
                    bullet1.setPosition(fixed_x_pos,y);
                    playerProjectiles.add(bullet1);
                    break;
                case 2:
                    bullet1.setPosition(fixed_x_pos - 12,y);
                    bullet2.setPosition(fixed_x_pos + 12,y);
                    playerProjectiles.add(bullet1);
                    playerProjectiles.add(bullet2);
                    break;
                case 3:
                    bullet1.setPosition(x,y);
                    bullet2.setPosition(fixed_x_pos - 12,y);
                    bullet3.setPosition(fixed_x_pos + 12,y);
                    playerProjectiles.add(bullet1);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    break;
                case 4:
                    bullet1.setPosition(fixed_x_pos - 8,y);
                    bullet2.setPosition(fixed_x_pos + 8,y);
                    bullet3.setPosition(fixed_x_pos - 16,y);
                    bullet4.setPosition(fixed_x_pos + 16,y);
                    playerProjectiles.add(bullet1);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    playerProjectiles.add(bullet4);
                    break;
                case 5:
                    bullet1.setPosition(fixed_x_pos,y);
                    bullet2.setPosition(fixed_x_pos - 12,y);
                    bullet3.setPosition(fixed_x_pos + 12,y);
                    bullet4.setPosition(fixed_x_pos - 24,y);
                    bullet5.setPosition(fixed_x_pos + 24,y);
                    playerProjectiles.add(bullet1);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    playerProjectiles.add(bullet4);
                    playerProjectiles.add(bullet5);
                    break;
                default:
                    break;
            }
            lastBulletTime = TimeUtils.nanoTime();
        }
    }
}