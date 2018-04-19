package tdshooter.game;

import com.badlogic.gdx.math.MathUtils;

public class StraightFlight extends FlightPattern
{
    private float angle;

    public StraightFlight(Collidable parent, float angle)
    {
        super(parent);
        this.angle = angle;
    }

    public void update(float delta)
    {
        parent.hitbox.y -= MathUtils.cosDeg(angle) * parent.speed * delta;
        parent.hitbox.x += MathUtils.sinDeg(angle) * parent.speed * delta;
    }

    @Override
    public boolean isBossDive() {
        return false;
    }
}
