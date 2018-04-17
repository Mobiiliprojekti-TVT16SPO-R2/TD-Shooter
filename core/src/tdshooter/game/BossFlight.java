package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class BossFlight extends FlightPattern {

    private float verticalSpeed;
//    private float time;
//    private float angularFrequency;
//    private float amplitude;
    private boolean destinationX;
    public BossFlight(Collidable parent) {//float amplitude, float angularFrequency) {
        super(parent);
        this.verticalSpeed = verticalSpeed;
        destinationX = true;
//        time = 0;
//        this.amplitude = amplitude;
//        this.angularFrequency = angularFrequency;
    }

    @Override
    public void update(float delta) {

        if (parent.hitbox.y > 1280 - parent.hitbox.height) {
            parent.hitbox.y -= parent.speed * delta;
        }
        else {
//          Gdx.app.log("DEBUG", "hitbox x: " + parent.hitbox.x);
            if (parent.hitbox.x >= 720 - parent.hitbox.width) {
                destinationX = false;
            }
            else if (parent.hitbox.x <= 0) {
                destinationX = true;
            }
            if (destinationX) {
                parent.hitbox.x += parent.speed * delta;
            }
            else {
                parent.hitbox.x -= parent.speed * delta;
            }
        }
    }
}
