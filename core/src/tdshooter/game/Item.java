package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by johan on 22.3.2018.
 */

public class Item extends Collidable {

    Texture itemTexture;
    private Texture healthpackTexture;
    private Texture CDReductionTexture;
    private Texture currencyTexture;
    private Texture weaponUpgradeTexture;
    private float[] items;
    private int itemIndex;
    private float heal = 0;
    private float cooldownReduction = 0;
    private float projectile = 0;
    private float currency = 0;

    public Item(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int speed, int itemIndex) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height);

        this.itemIndex = itemIndex;
        this.speed = speed;
        items = new float[4];

        Gdx.app.log("LOADING", "start loading item assets..");
        healthpackTexture = new Texture(Gdx.files.internal("Items/healthpack_test.png"));
        Gdx.app.log("LOADING", "itemAsset 1 loaded..");
        CDReductionTexture = new Texture(Gdx.files.internal("Items/flightspeed_test.png"));
        Gdx.app.log("LOADING", "itemAsset 2 loaded..");
        currencyTexture = new Texture(Gdx.files.internal("Items/currency_test.png"));
        Gdx.app.log("LOADING", "itemAsset 3 loaded..");
        weaponUpgradeTexture = new Texture(Gdx.files.internal("Items/armor_test.png"));
        Gdx.app.log("LOADING", "itemAsset 4 loaded..");

        switch (this.itemIndex) {
            case 1:
                this.itemTexture = healthpackTexture;
                break;
            case 2:
                this.itemTexture = CDReductionTexture;
                break;
            case 3:
                this.itemTexture = weaponUpgradeTexture;
                break;
            case 4:
                this.itemTexture = currencyTexture;
                break;
            default:
                this.itemTexture = currencyTexture;
                break;
        }
    }
    public float[] getStats() {
        if (itemIndex == 1) {
            heal = 10;
        }
        else if (itemIndex == 2) {
            cooldownReduction = 10;
        }
        else if (itemIndex == 3){
            projectile = 1;
        }
        else if (itemIndex == 4){
            currency = 10;
        }
        items[0] = heal;
        items[1] = cooldownReduction;
        items[2] = projectile;
        items[3] = currency;
            return items;

    }
    public void update(){
        this.hitbox.y -= this.speed * Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(itemTexture, hitbox.x, hitbox.y, hitbox.getWidth(), hitbox.getHeight());
    }
}
