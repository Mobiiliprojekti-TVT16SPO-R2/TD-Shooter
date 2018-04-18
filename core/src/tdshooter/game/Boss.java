package tdshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class Boss extends Encounter {

    AssetManager assets;
    private FlightPattern flightPattern;
    private RandomXS128 random;
    private int randomNumber;
    private int rng;
    private boolean spawn;

    public Boss(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, int points, Texture image, AssetManager assets) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD, speed, points, image);
        this.assets = assets;
        rng = 120;
        spawn = false;
    }
    public void spawnWeaklings(ArrayList<Encounter> encounters) {

        if (spawn) {
            int plane_mid_x = (int) (hitbox.x + (hitbox.width / 2)); //middle of the plane
            int plane_mid_y = (int) (hitbox.y);

            Encounter encounter = EncounterBuilder.create(EncounterType.getByValue(0), assets);
            encounter.setPosition(new Vector2(plane_mid_x, plane_mid_y));
            encounters.add(encounter);
            spawn = false;
        }
    }
    @Override
    public void update(float delta) {

        if(flightPattern != null) {
            flightPattern.update(delta);
        }

        long randomSeed = TimeUtils.nanoTime();
        random = new RandomXS128(randomSeed);
        randomNumber = random.nextInt(rng) + 1;

        if (randomNumber > rng - 1) {
          spawn = true;

        }
    }
    @Override
    public void setFlightPattern(FlightPattern flightPattern) {this.flightPattern = flightPattern;}

}