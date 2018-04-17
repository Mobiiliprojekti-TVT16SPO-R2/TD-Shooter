package tdshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Boss extends Encounter {

    AssetManager assets;

    public Boss(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD, float speed, int points, Texture image, AssetManager assets) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD, speed, points, image);
        this.assets = assets;
    }
    public void spawnWeaklings() {

        Encounter encounter = EncounterBuilder.create(EncounterType.getByValue(0), assets);
    }
}
