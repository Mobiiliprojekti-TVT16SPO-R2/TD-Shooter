package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by leevi on 16.3.2018.
 */

public class Player extends Destroyable{

    Texture playerImage;

    public Player(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        playerImage = new Texture(Gdx.files.internal("bucket.png"));
    }
}
