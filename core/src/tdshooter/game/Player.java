package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
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

    private Texture playerImage;
    private Vector3 destination;
    private boolean moving;
    private int maxHitpoints = 0;
    private int baseMaxHitpoints = 0;
    private int currency = 0;
    private int points = 0;
    private int weaponChoice = 0;
    private int turretCount = 1;
    private int cooldownReduction = 0;
    private int superWeaponInt = 2;
    private long lastChangeTime;
    private ArrayList<Weapon> weapons;
    private Sound cashSound;

    public Player(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, AssetManager assets) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        playerImage = assets.get("planes/Player_FighterPlane.png");
        cashSound = assets.get("Sounds/money.wav");

        baseMaxHitpoints = hitP;
        maxHitpoints = baseMaxHitpoints;
        speed = 0;
        maxSpeed = 720;
        acceleration = maxSpeed * 5;
        lastChangeTime = 0;

        destination = new Vector3();
        weapons = new ArrayList<Weapon>();

        createWeapons(assets);

    }

    private void createWeapons(AssetManager assets) {
        Preferences prefs = Gdx.app.getPreferences("savedata");
        if(prefs.contains("weapon01")) {
            int level = prefs.getInteger("weapon01");
            switch (level) {
                case 0:
                    break;
                case 1:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON01_LVL1, assets));
                    break;
                case 2:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON01_LVL2, assets));
                    break;
                case 3:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON01_LVL3, assets));
                    break;
                default:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON01_LVL3, assets));
                    break;
            }
        }
        if(prefs.contains("weapon02")) {
            int level = prefs.getInteger("weapon02");
            switch (level) {
                case 0:
                    break;
                case 1:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON02_LVL1, assets));
                    break;
                case 2:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON02_LVL2, assets));
                    break;
                case 3:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON02_LVL3, assets));
                    break;
                default:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON02_LVL3, assets));
                    break;
            }
        }
        if(prefs.contains("weapon03")) {
            int level = prefs.getInteger("weapon03");
            switch (level) {
                case 0:
                    break;
                case 1:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON03_LVL1, assets));
                    break;
                case 2:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON03_LVL2, assets));
                    break;
                case 3:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON03_LVL3, assets));
                    break;
                default:
                    weapons.add(WeaponBuilder.create(WeaponType.WEAPON03_LVL3, assets));
                    break;
            }
        }
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

    public void shoot(ArrayList<Projectile> playerProjectiles)
    {
        int plane_mid_x = (int) (hitbox.x + (hitbox.width /2)); //middle of the plane
        int plane_mid_y = (int) (hitbox.y + hitbox.height);  // top of the plane
        if(weapons.isEmpty() == false)
        {
            weapons.get(weaponChoice).fire(plane_mid_x, plane_mid_y, playerProjectiles);
        }
    }

    public void swapWeapons() {
        if (TimeUtils.nanoTime() - lastChangeTime > 200000000) {
            weaponChoice++;
            if(weaponChoice > weapons.size() - 1)
            {
                weaponChoice = 0;
            }
            lastChangeTime = TimeUtils.nanoTime();
        }
    }

    public int getWeaponChoice(){
        return weaponChoice;
    }

    public void pickUp(Item item){

        float [] items = item.getStats();

        this.hitPoints += items[0];
        if (this.cooldownReduction < 60) {
            this.cooldownReduction += items[1];
        }
        if (this.turretCount < 7){
            this.turretCount += items[2];
        }
        int old_currency = this.currency;
        this.currency += items[3];
        if (this.currency > old_currency) {
            cashSound.play();
        }

        for(Weapon weapon : weapons) {
            weapon.upgradeTurretCount(this.turretCount);
            weapon.setCooldownReduction(this.cooldownReduction);
        }

        if (this.maxSpeed > 2000) {
            this.maxSpeed = 2000;
        }
        if (this.hitPoints > maxHitpoints) {
            this.hitPoints = maxHitpoints;
        }
    }

    public void reset()
    {
        moving = false;
        currency = 0;
        points = 0;
        turretCount = 1;
        cooldownReduction = 0;
        maxHitpoints = baseMaxHitpoints;
        speed = 0;
        maxSpeed = 720;
        acceleration = maxSpeed * 5;
        lastChangeTime = 0;
        weaponChoice = 1;
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

    public int getMaxHitpoints() {
        return maxHitpoints;
    }

    public int getBaseMaxHitpoints() {
        return baseMaxHitpoints;
    }

    public void setPosition(float x, float y) {this.hitbox.x = x; this.hitbox.y = y;}

    public int getSuperWeaponInt() {
        return superWeaponInt;
    }
    public void setSuperWeaponInt(){
        superWeaponInt --;
    }

    public void addWeapon(Weapon weapon)
    {
        weapons.add(weapon);
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }
}
