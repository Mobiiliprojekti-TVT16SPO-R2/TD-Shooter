package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by leevi on 16.3.2018.
 */

public class Player extends Destroyable{

    Texture playerImage;
    private Vector3 destination;
    private boolean moving;
    private boolean shooting;

    public Player(int hitbox_x, int hitbox_y, int hitbox_width, int hitbox_height, int hitP, int hitD) {
        super(hitbox_x, hitbox_y, hitbox_width, hitbox_height, hitP, hitD);
        playerImage = new Texture(Gdx.files.internal("planes/FighterPlane_Test_1_256.png"));

        speed = 0;
        maxSpeed = 720;
        acceleration = maxSpeed * 5;

        destination = new Vector3();
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

    public void setShooting(boolean shooting)
    {
        this.shooting = shooting;
    }

    public boolean isShooting()
    {
        return this.shooting;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(playerImage, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }
}
