package tdshooter.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Samuli Lehto on 20.3.2018.
 */

public class Mission {

    private class SpawnInfo {
        public SpawnInfo(Encounter spawnObject, float spawnTime) {
            this.spawnObject = spawnObject;
            this.spawnTime = spawnTime;
        }

        public Encounter spawnObject;
        public float spawnTime;
    }

    public Mission(String fileName, AssetContainer assets, ArrayList<Encounter> enemyList) {
        runningTime = 0;
        missionOver = false;
        this.enemyList = enemyList;

        endingTime = 20;
        scrollSpeed = 100;
        backgroundLooping = true;

        background = assets.textures.get("background01");
        backgroundMusic = assets.musics.get("music01");

        spawnInfoList = new ArrayList<SpawnInfo>();
        for (int i = 0; i < 4; i++) {
            Encounter encounter = new Encounter(240, 864, 64, 64,
                    20, 20, assets.textures.get("enemy01"));
            spawnInfoList.add(new SpawnInfo(encounter, i * 4));
        }
    }

    public void update(float delta)
    {
        runningTime += delta;
        if(runningTime > endingTime)
        {
            missionOver = true;
        }

        if(!missionOver)
        {
            checkSpawns();
        }
    }

    private void checkSpawns()
    {
        Iterator<SpawnInfo> iter = spawnInfoList.iterator();
        while(iter.hasNext())
        {
            SpawnInfo spawnInfo = iter.next();
            if(runningTime > spawnInfo.spawnTime)
            {
                enemyList.add(spawnInfo.spawnObject);
                iter.remove();
            }

        }
    }

    public Texture getBackground() {return background;}
    public Music getBackgroundMusic() {return backgroundMusic;}
    public boolean isBackgroundLooping() {return backgroundLooping;}
    public boolean isMissionOver() {return missionOver;}
    public float getScrollSpeed() {return scrollSpeed;}

    private Texture background;
    private Music backgroundMusic;
    private boolean backgroundLooping;
    private boolean missionOver;
    private float scrollSpeed;
    private float runningTime;
    private float endingTime;
    private List<SpawnInfo> spawnInfoList;
    private ArrayList<Encounter> enemyList;
}
