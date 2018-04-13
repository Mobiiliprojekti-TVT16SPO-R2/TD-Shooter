package tdshooter.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ScrollingBackground extends Sprite
{
    public ScrollingBackground(Texture texture)
    {
        super(texture);
        loopingSprite = new Sprite(this);
        loopingSprite.translateY(this.getHeight());

        backgroundLooping = true;
        scrollSpeed = 100.0f;
        //scrollAcceleration = 0.0f;
    }

    public void update(float delta)
    {
        if(backgroundLooping)
        {
            super.translateY(-scrollSpeed * delta);
            loopingSprite.translateY(-scrollSpeed * delta);
            checkForReset(this);
            checkForReset(loopingSprite);
        }
    }

    @Override
    public void draw(Batch batch)
    {
        super.draw(batch);
        loopingSprite.draw(batch);
    }

    private void checkForReset(Sprite sprite)
    {
        if (sprite.getY() + sprite.getHeight() < 0 )
        {
            sprite.translateY(sprite.getHeight() * 2);
        }

    }

    public void setScrollSpeed(float scrollSpeed) {this.scrollSpeed = scrollSpeed;}
    public void setLooping(boolean looping) {this.backgroundLooping = looping;}

    private float scrollSpeed;
    //private float scrollAcceleration;
    //private float maxSpeed;
    private Sprite loopingSprite;
    private boolean backgroundLooping;
}
