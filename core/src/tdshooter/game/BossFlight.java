package tdshooter.game;

import com.badlogic.gdx.Gdx;

public class BossFlight extends FlightPattern {

    private int index;
    private int flightHeight;
    private boolean bossDiveComplete = false;
    private boolean bossDive = false;

    private boolean destinationX;
    public BossFlight(Collidable parent, int index, int flightHeight) {
        super(parent);
        destinationX = true;
        this.index = index;
        this.flightHeight = flightHeight;

    }

    @Override
    public void update(float delta) {

        if (parent.hitbox.y >= flightHeight) { //1280 - parent.hitbox.height - 80
            parent.hitbox.y -= parent.speed * delta;
        }
        else {
            if (index == 0) {
                verticalFlight(delta);
            }
            else if (index == 1) {
                bossDiveFlight(delta);
            }
            else if (index == 2) {
                bossDiveVerticalFlight(delta);
            }
            else if (index == 3) {
                basicDive(delta);
            }
        }
    }

    public boolean isBossDive(){
        return bossDive;
    }

    public void verticalFlight(float delta){
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
    public void bossDiveFlight(float delta) {
        bossDive = true;
        if (parent.hitbox.y >= 100 && !bossDiveComplete) {
            parent.hitbox.y -= (parent.speed * 8) * delta;
        }
        else {
            bossDiveComplete = true;
            parent.hitbox.y += (parent.speed * 3) * delta;
            if (parent.hitbox.y >= flightHeight) {
                bossDive = false;
            }
        }
    }
    private void bossDiveVerticalFlight(float delta) {
        bossDive = true;
        if (parent.hitbox.y > 100 && !bossDiveComplete) {

            parent.hitbox.y -= (parent.speed * 8) * delta;

            if (parent.hitbox.x >= 720 - parent.hitbox.width) {
                destinationX = false;
            }
            else if (parent.hitbox.x <= 0) {
                destinationX = true;
            }
            if (destinationX) {
                parent.hitbox.x += (parent.speed * 2) * delta;
            }
            else {
                parent.hitbox.x -= (parent.speed * 2) * delta;
            }
        }
        else {

            bossDiveComplete = true;
            parent.hitbox.y += (parent.speed * 3) * delta;
            if (parent.hitbox.x >= 720 - parent.hitbox.width) {
                destinationX = false;
            }
            else if (parent.hitbox.x <= 0) {
                destinationX = true;
            }
            if (destinationX) {
                parent.hitbox.x += (parent.speed * 2) * delta;
            }
            else {
                parent.hitbox.x -= (parent.speed * 2) * delta;
            }
            if (parent.hitbox.y > flightHeight) {
                bossDive = false;
            }
        }
    }
    public void basicDive(float delta) {
        parent.hitbox.y -= (parent.speed * 8) * delta;
    }
    @Override
    public void setVariables() {
        bossDiveComplete = false;
        bossDive = false;
    }
}


