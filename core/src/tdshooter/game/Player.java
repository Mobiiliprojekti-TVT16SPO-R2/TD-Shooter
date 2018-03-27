package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by leevi on 16.3.2018.
 */

public class Player extends Destroyable{

    Texture playerImage;
    private Vector3 destination;
    private boolean moving;
    private boolean shooting;
    private int maxHitpoints = 0;
    private int currency = 0;
    private int points = 0;
    private float[] items;
    private Weapon weapon1;
    private Weapon weapon2;
    private Weapon weapon3;
    private Weapon weapon4;
    private Weapon weapon5;
    private Weapon weapon6;
    private Weapon weapon7;
    private int weaponChoice = 1;
    private int turretCount = 1;
    private int cooldownReduction = 0;
    private long lastChangeTime;

    private Sound firingSound1;
    private Sound firingSound2;
    private Sound firingSound3;
    private Texture firingImage1;
    private Texture firingImage2;

    public Player(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        playerImage = new Texture(Gdx.files.internal("planes/Player_FighterPlane.png"));

        maxHitpoints = hitP;
        speed = 0;
        maxSpeed = 720;
        acceleration = maxSpeed * 5;

        destination = new Vector3();

        // LOAD ALL WEAPON SOUNDS AND FIRINGIMAGES/animations
        firingSound1 = Gdx.audio.newSound(Gdx.files.internal("hitSound.wav"));
        firingSound2 = firingSound1;
        firingImage1 = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));
        firingImage2 = firingImage1;

        weapon1 = new Weapon(1, 1, (long) 70000000, false, 20, firingSound1, firingImage1);
        weapon2 = new Weapon(1, 2, (long) 90000000, false, 0, firingSound1, firingImage1);
        weapon3 = new Weapon(1, 3, (long) 300000000, false, 80, firingSound1, firingImage1);
//        weapon4 = new Weapon(2, 2, (long) 90000000, false, 0, firingSound2, firingImage2);
//        weapon5 = new Weapon(4, 2, (long) 90000000, false, 0, firingSound2, firingImage2);
//        weapon6 = new Weapon(4, 3, (long) 300000000, false, 80, firingSound2, firingImage2);
//        weapon7 = new Weapon(6, 3, (long) 300000000, false, 80, firingSound2, firingImage2);

        items = new float[4];
    }

    public void move(float delta)
    {

        if(this.moving) {
            speed = Math.min(speed + acceleration * delta, maxSpeed);
        }
        else {
            speed = Math.max(speed - acceleration * delta, 0);
        }

        Vector3 playerPos = new Vector3(), moveDirection = new Vector3();
        float distance;

        playerPos.set(this.hitbox.getX(), this.hitbox.getY(), 0);
        moveDirection.set(destination.x - playerPos.x, destination.y - playerPos.y, 0);
        moveDirection.nor();
        distance = playerPos.dst(destination);
        playerPos.add(moveDirection.scl(Math.min(delta * speed, distance)));
        this.hitbox.setX(playerPos.x);
        this.hitbox.setY(playerPos.y);
    }

    public void setDestination(Vector3 destination)
    {
        destination.x = (destination.x - (hitbox.width / 2));
        this.destination = destination;
    }

    public void setMoving(boolean moving)
    {
        this.moving = moving;
    }

    public void draw(SpriteBatch batch) {
        //player image will be drawn a little bit bigger than its hitbox
        batch.draw(playerImage, hitbox.x-3, hitbox.y-3, hitbox.width+6, hitbox.height+6);
    }

    public void shoot(ArrayList<Projectile> playerProjectiles) {
        int plane_mid_x = (int) (hitbox.x + (hitbox.width /2)); //middle of the plane
        int plane_mid_y = (int) (hitbox.y + hitbox.height);  // top of the plane
        switch (weaponChoice) {
            case 0:
                break;
            case 1:
                weapon1.fire(plane_mid_x, plane_mid_y, playerProjectiles);
                break;
            case 2:
                weapon2.fire(plane_mid_x, plane_mid_y, playerProjectiles);
                break;
            case 3:
                weapon3.fire(plane_mid_x, plane_mid_y, playerProjectiles);
                break;
//            case 4:
//                weapon4.fire(plane_mid_x, plane_mid_y, playerProjectiles);
//                break;
//            case 5:
//                weapon5.fire(plane_mid_x, plane_mid_y, playerProjectiles);
//                break;
//            case 6:
//                weapon6.fire(plane_mid_x, plane_mid_y, playerProjectiles);
//                break;
//            case 7:
//                weapon7.fire(plane_mid_x, plane_mid_y, playerProjectiles);
//                break;
            default:
                break;
        }
    }

    public void swapWeapons() {
        if (TimeUtils.nanoTime() - lastChangeTime > 200000000) {
            if (weaponChoice == 3) {
                weaponChoice = 0;
            } else {
                weaponChoice++;
            }
            lastChangeTime = TimeUtils.nanoTime();
        }
    }

    public int getWeaponChoice(){
        return weaponChoice;
    }

    public void pickUp(Item item){

        items = item.getStats();

        this.hitPoints += items[0];
        if (this.cooldownReduction < 60) {
            this.cooldownReduction += items[1];
        }
        if (this.turretCount < 7){
            this.turretCount += items[2];
        }
        this.currency += items[3];

        weapon1.setTurretCount(this.turretCount);
        weapon2.setTurretCount(this.turretCount);
        weapon3.setTurretCount(this.turretCount);
        weapon1.setCooldownReduction(this.cooldownReduction);
        weapon2.setCooldownReduction(this.cooldownReduction);
        weapon3.setCooldownReduction(this.cooldownReduction);

        Gdx.app.log("DEBUG", "weapon CDR from player: " + cooldownReduction);

        if (this.maxSpeed > 2000) {
            this.maxSpeed = 2000;
        }
        if (this.hitPoints > maxHitpoints) {
            this.hitPoints = maxHitpoints;
        }

//        if (item.getName() == "healtpack") {
//            if (this.hitPoints < maxHitpoints) {
//                this.hitPoints += item.getStats();
//                if (this.hitPoints > maxHitpoints) {
//                    this.hitPoints = maxHitpoints;
//                }
//            }
//        }
//        if (item.getName() == "flightspeed") {
//            this.maxSpeed = this.maxSpeed * item.getStats();
//
//        }
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points += points;
    }

    public int getCurrency() {
        return currency;
    }
}
