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
    int spread = 0;
    Sound firingSound;
    Texture firingImage;
    Projectile bulletModel,bullet1,bullet2,bullet3,bullet4,bullet5,bullet6,bullet7;
    
    Texture bulletImage1;
    Texture bulletImage2;
    Texture bulletImage3;

    public Weapon(int turretCount, int projectileType, Long cooldownTime, boolean autoTargetting, int spread, Sound firingSound, Texture firingImage) {
        this.turretCount = turretCount;
        this.projectileType = projectileType;
        this.cooldownTime = cooldownTime;
        this.autoTargetting = autoTargetting;
        this.spread = spread;
        this.firingSound = firingSound;
        this.firingImage = firingImage;
        
        //load all bullet textures
        bulletImage1 = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));
        bulletImage2 = new Texture(Gdx.files.internal("Bullets/bullet1.png"));

        //first create bulletmodel by type
        switch (projectileType) {
            case 1:
                bulletModel = new Projectile(-500,0, 24, 32, 5, 1000, bulletImage1);
                break;
            case 2:
                bulletModel = new Projectile(-500,0, 18, 64, 10, 2000, bulletImage1);
                break;
            case 3:
                bulletModel = new Projectile(-500,0, 32, 48, 13, 1400, bulletImage2);
                break;
            default: //same as basic
                bulletModel = new Projectile(-500,0, 32, 32, 5, 700, bulletImage1);
                break;
        }
    }

    public void fire(int x, int y, ArrayList<Projectile> playerProjectiles) {

        bullet1 = new Projectile(bulletModel);
        bullet2 = new Projectile(bulletModel);
        bullet3 = new Projectile(bulletModel);
        bullet4 = new Projectile(bulletModel);
        bullet5 = new Projectile(bulletModel);
        bullet6 = new Projectile(bulletModel);
        bullet7 = new Projectile(bulletModel);

        if (spread > 0) {
            bullet2.setVerticalSpeed(-(int)(spread*0.8));
            bullet3.setVerticalSpeed((int)(spread*0.8));
            bullet4.setVerticalSpeed(-(int)(spread*1.4));
            bullet5.setVerticalSpeed((int)(spread*1.4));
            bullet6.setVerticalSpeed(-(spread*2));
            bullet7.setVerticalSpeed((spread*2));
        }

        int fixed_x_pos = x - (int)(bulletModel.hitbox.width / 2);

        if (TimeUtils.nanoTime() - lastBulletTime > cooldownTime){
            switch (turretCount){
                case 1:
                    bullet1.setPosition(fixed_x_pos,y);
                    playerProjectiles.add(bullet1);
                    break;
                case 2:
                    bullet2.setPosition(fixed_x_pos - 12,y);
                    bullet3.setPosition(fixed_x_pos + 12,y);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    break;
                case 3:
                    bullet1.setPosition(fixed_x_pos,y + 3);
                    bullet2.setPosition(fixed_x_pos - 12,y);
                    bullet3.setPosition(fixed_x_pos + 12,y);
                    playerProjectiles.add(bullet1);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    break;
                case 4:
                    bullet2.setPosition(fixed_x_pos - 8,y + 3);
                    bullet3.setPosition(fixed_x_pos + 8,y + 3);
                    bullet4.setPosition(fixed_x_pos - 16,y);
                    bullet5.setPosition(fixed_x_pos + 16,y);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    playerProjectiles.add(bullet4);
                    playerProjectiles.add(bullet5);
                    break;
                case 5:
                    bullet1.setPosition(fixed_x_pos,y + 6);
                    bullet2.setPosition(fixed_x_pos - 12,y + 3);
                    bullet3.setPosition(fixed_x_pos + 12,y + 3);
                    bullet4.setPosition(fixed_x_pos - 24,y);
                    bullet5.setPosition(fixed_x_pos + 24,y);
                    playerProjectiles.add(bullet1);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    playerProjectiles.add(bullet4);
                    playerProjectiles.add(bullet5);
                    break;
                case 6:
                    bullet2.setPosition(fixed_x_pos - 8,y + 6);
                    bullet3.setPosition(fixed_x_pos + 8,y + 6);
                    bullet4.setPosition(fixed_x_pos - 16,y + 3);
                    bullet5.setPosition(fixed_x_pos + 16,y + 3);
                    bullet6.setPosition(fixed_x_pos - 24,y);
                    bullet7.setPosition(fixed_x_pos + 24,y);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    playerProjectiles.add(bullet4);
                    playerProjectiles.add(bullet5);
                    playerProjectiles.add(bullet6);
                    playerProjectiles.add(bullet7);
                    break;
                case 7:
                    bullet1.setPosition(fixed_x_pos,y + 9);
                    bullet2.setPosition(fixed_x_pos - 12,y + 6);
                    bullet3.setPosition(fixed_x_pos + 12,y + 6);
                    bullet4.setPosition(fixed_x_pos - 24,y + 3);
                    bullet5.setPosition(fixed_x_pos + 24,y + 3);
                    bullet6.setPosition(fixed_x_pos - 36,y);
                    bullet7.setPosition(fixed_x_pos + 36,y);
                    playerProjectiles.add(bullet1);
                    playerProjectiles.add(bullet2);
                    playerProjectiles.add(bullet3);
                    playerProjectiles.add(bullet4);
                    playerProjectiles.add(bullet5);
                    playerProjectiles.add(bullet6);
                    playerProjectiles.add(bullet7);
                    break;
                default:
                    break;
            }
            lastBulletTime = TimeUtils.nanoTime();
        }
    }

    public void setTurretCount(int turretCount) {
        this.turretCount = turretCount;
    }
}

//
//    private void spawnBullet() {
//        Projectile bullet = new Projectile((int)player.hitbox.x + 96 - 6,(int)player.hitbox.y + 42,
//                24, 36, 5, 800, 35, bulletImage);
//
//        playerProjectiles.add(bullet);
//
//        Projectile bullet2 = new Projectile((int)player.hitbox.x - 6,(int)player.hitbox.y + 42,
//                24, 36, 5, 800, -35, bulletImage);
//        playerProjectiles.add(bullet2);
//
//        Projectile bullet3 = new Projectile((int)player.hitbox.x + 72 - 6,(int)player.hitbox.y + 50,
//                24, 36, 5, 800, 25, bulletImage);
//        playerProjectiles.add(bullet3);
//
//        Projectile bullet4 = new Projectile((int)player.hitbox.x + 24 - 6,(int)player.hitbox.y + 50,
//                24, 36, 5, 800,  -25,  bulletImage);
//        playerProjectiles.add(bullet4);
//
//        Projectile bullet5 = new Projectile((int)player.hitbox.x + 48 - 6,(int)player.hitbox.y + 58,
//                24, 36, 5, 800, bulletImage);
//        playerProjectiles.add(bullet5);
//
//    }

