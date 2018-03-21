package tdshooter.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

/**
 * Created by Samuli Lehto on 20.3.2018.
 */

public class Mission {

    private class SpawnInfo
    {
        public Collidable spawnObject;
        public Vector3 spawnPosition;
        public long spawnTime;
    }

    public Mission(String fileName)
    {
        runningTime = 0;
        endingTime = 60;
        scrollSpeed = 100;
        backgroundLooping = true;
    }

    private Sprite background;
    private Music backgroundMusic;
    private boolean backgroundLooping;
    private float scrollSpeed;
    private float runningTime;
    private float endingTime;
    private List<SpawnInfo> spawnInfoList;

    void setBackground(Texture bg) {background.setTexture(bg);}
    void setBackgroundMusic(Music bgm) {backgroundMusic = bgm;}
}
