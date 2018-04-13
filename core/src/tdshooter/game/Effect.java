package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by johan on 9.4.2018.
 */

public class Effect {
    private Animation<TextureRegion> deathAnimation;
    private TextureAtlas animationAtlas;
    private float stateTime;
    private float speed;
    private float x;
    private float y;

    public Effect(float x, float y, float speed){
        this.x = x;
        this.y = y;
        this.speed = speed;
        stateTime = 0f;
        animationAtlas = new TextureAtlas("effects/Explosion/explosion_alienplane_center.txt");
        deathAnimation = new Animation<TextureRegion>(0.015f, animationAtlas.findRegions("AlienFighter_Death_Explosion")); // 0.015f
    }

    public TextureRegion getCurrentFrame(float delta){

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = deathAnimation.getKeyFrame(stateTime, false);
//        Gdx.app.log("DEBUG", "state time: " + stateTime + " Key Frame index: " + deathAnimation.getKeyFrameIndex(stateTime));

        return currentFrame;
    }
    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public boolean isFinished() {
        if (deathAnimation.isAnimationFinished(stateTime)){
                    Gdx.app.log("DEBUG", "state time: " + stateTime + " Key Frame index: " + deathAnimation.getKeyFrameIndex(stateTime));

            return true;
        }
        else {
            return false;
        }
    }
    public void update(){
        this.y -= this.speed * Gdx.graphics.getDeltaTime();
    }

    public void setAtributes(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        stateTime = 0f;
    }
}
