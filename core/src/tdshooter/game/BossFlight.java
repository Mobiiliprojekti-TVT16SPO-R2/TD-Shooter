package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class BossFlight extends FlightPattern {

    private int index;
    private boolean bossDiveComplete = false;
    private boolean bossDive = false;

    private boolean destinationX;
    public BossFlight(Collidable parent, int index) {//float amplitude, float angularFrequency) {
        super(parent);
        destinationX = true;
        this.index = index;

    }

    @Override
    public void update(float delta) {

        if (parent.hitbox.y > 1280 - parent.hitbox.height) {
            parent.hitbox.y -= parent.speed * delta;
        }
        else {
            if (index == 0) {
                if (parent.hitbox.x >= 720 - parent.hitbox.width) {
                    destinationX = false;
                } else if (parent.hitbox.x <= 0) {
                    destinationX = true;
                }
                if (destinationX) {
                    parent.hitbox.x += parent.speed * delta;
                } else {
                    parent.hitbox.x -= parent.speed * delta;
                }
            }
            else {
                bossDive = true;
                if (parent.hitbox.y > 100 && !bossDiveComplete) {

                    parent.hitbox.y -= (parent.speed * 7) * delta;

                }
                else {
                    bossDiveComplete = true;
                    parent.hitbox.y += (parent.speed * 3) * delta;
                    if (parent.hitbox.y > 1280 - parent.hitbox.height) {
                        bossDive = false;
                    }
                }
            }
        }
    }
    public boolean isBossDive(){
        return bossDive;
    }
}
