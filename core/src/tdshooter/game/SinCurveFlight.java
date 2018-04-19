package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class SinCurveFlight extends FlightPattern
{
    private float time;
    private float angularFrequency;
    private float amplitude;

    public SinCurveFlight(Collidable parent, float amplitude, float angularFrequency)
    {
        super(parent);
        time = 0;
        this.amplitude = amplitude;
        this.angularFrequency = angularFrequency;
    }

    @Override
    public void update(float delta)
    {
        parent.hitbox.y -= parent.speed * delta;

        time += delta;
        if(time > MathUtils.PI2 / angularFrequency) {
            time = 0;
        }

        // sinin aallon derivaatta (muutosnopeus) kerrottuna delta-ajalla
        parent.hitbox.x += amplitude * MathUtils.cos(angularFrequency * time) * angularFrequency * delta;
    }

    @Override
    public boolean isBossDive() {
        return false;
    }
}
