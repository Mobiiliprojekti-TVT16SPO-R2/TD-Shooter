package tdshooter.game;

import com.badlogic.gdx.Gdx;

public abstract class FlightPattern
{
    protected Collidable parent;

    public FlightPattern(Collidable parent)
    {
        this.parent = parent;
    }

    public abstract void update(float delta);
}
