package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class Boss extends ShootingEnemy {

    AssetManager assets;
    private FlightPattern flightPattern;
    private FlightPattern verticalFlight;
    private FlightPattern diveFlight;
//    private FlightPattern diveVerticalFlight;
//    private RandomXS128 random;
//    private int randomNumber;
    private int rng;
    private int index;
    private boolean spawn;
    private long lastSpawnTime;
    private long lastDiveTime;
    private long diveCooldown;
    private long spawnCooldown;
    private boolean flightSet = true;

    public Boss(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, int turretCount, int spread, long cooldownTime, int shootRNG, int points, long spawnCooldown, int index, Texture image, AssetManager assets) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD, speed, turretCount, spread, cooldownTime, shootRNG, points, image, assets);
        this.assets = assets;
        this.index = index;
        rng = 3;
        spawn = false;
        lastSpawnTime = TimeUtils.nanoTime();
        lastDiveTime = TimeUtils.nanoTime();
        diveCooldown = 9000000000L;
        this.spawnCooldown = spawnCooldown; // 2000000000L
        verticalFlight = FlightPatternBuilder.create(FlightType.getByValue(6), this);

        if (this.index == 1){
            diveFlight = FlightPatternBuilder.create(FlightType.getByValue(7), this);
        }
        else if (this.index == 2){
            diveFlight = FlightPatternBuilder.create(FlightType.getByValue(8), this);
        }
    }
    public void spawnWeaklings(ArrayList<Encounter> enemyList) {

        if (spawn) {
            if (index == 1) {
                int plane_mid_x = (int) (hitbox.x + (hitbox.width / 2)); //middle of the plane
                int plane_mid_y = (int) (hitbox.y);

                Encounter encounter = EncounterBuilder.create(EncounterType.getByValue(0), assets);
                FlightPattern flight = FlightPatternBuilder.create(FlightType.getByValue(3), encounter);
                encounter.setFlightPattern(flight);
                encounter.setPosition(new Vector2(plane_mid_x, plane_mid_y));
                enemyList.add(encounter);
                spawn = false;
            }
            else if (index == 2) {
                int plane_mid_x = (int) (hitbox.x + (hitbox.width / 2)); //middle of the plane
                int plane_mid_y = (int) (hitbox.y);

                Encounter encounter = EncounterBuilder.create(EncounterType.getByValue(1), assets);
                FlightPattern flight = FlightPatternBuilder.create(FlightType.getByValue(3), encounter);
                encounter.setFlightPattern(flight);
                encounter.setPosition(new Vector2(plane_mid_x, plane_mid_y));
                enemyList.add(encounter);
                spawn = false;
            }
            else if (index == 3) {

            }
        }
    }
    @Override
    public void update(float delta) {
        if(flightPattern != null) {
            flightPattern.update(delta);
        }
        if (!this.flightPattern.isBossDive()){
            bossAttack();
        }
        else {
            flightSet = false;
        }
    }

    public void bossAttack(){
        if (!flightSet) {
            this.setFlightPattern(verticalFlight);
            flightSet = true;
        }
        if (TimeUtils.nanoTime() - lastSpawnTime > spawnCooldown) {
            random = new RandomXS128(lastSpawnTime);
            randomNumber = random.nextInt(rng) + 1;
            if (randomNumber > rng - 1  && TimeUtils.nanoTime() - lastDiveTime > diveCooldown) {
                diveFlight.setVariables();
                this.setFlightPattern(diveFlight);
                lastDiveTime = TimeUtils.nanoTime();
            }
            else {
                spawn = true;
            }
            lastSpawnTime = TimeUtils.nanoTime();
        }
    }
    @Override
    public void shoot(ArrayList<Projectile> projectileList){
        long randomSeed = TimeUtils.nanoTime();
        random = new RandomXS128(randomSeed);
        randomNumber = random.nextInt(this.shootRNG) + 1;

        if (randomNumber > shootRNG - 1 && !this.flightPattern.isBossDive()) {

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
    @Override
    public void setFlightPattern(FlightPattern flightPattern) {this.flightPattern = flightPattern;}
}