package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class SinCurveFlight extends FlightPattern
{
    private float angle;
    private float angularFrequency;
    private float amplitude;

    public SinCurveFlight(Collidable parent, float amplitude, float angularFrequency)
    {
        super(parent);
        angle = 0;
        this.amplitude = amplitude;
        this.angularFrequency = angularFrequency;
    }

    @Override
    public void update(float delta)
    {
        parent.hitbox.y -= parent.speed * delta;

        angle += angularFrequency * delta;
        if(angle > MathUtils.PI2) {
            angle = 0;
        }
        parent.hitbox.x += amplitude * MathUtils.cos(angle); // sinin derivaatta
    }
}
