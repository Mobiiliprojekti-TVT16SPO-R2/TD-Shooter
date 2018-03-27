package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

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

    public Mission(String fileName, AssetManager assets, ArrayList<Encounter> enemyList) {
        spawnInfoList = new ArrayList<SpawnInfo>();
        runningTime = 0;
        missionOver = false;
        this.enemyList = enemyList;

        loadFromFile(fileName, assets);
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

    private void loadFromFile(String filename, AssetManager assets)
    {
        String str;
        FileHandle file = Gdx.files.internal(filename);
        if(!file.exists())
        {
            Gdx.app.error("MissionReadError", "File '" + filename + "' not found.");
            System.exit(1);
        }

        str = file.readString();
        String [] strArray = str.split("[\\n\\r]+");

        if(strArray.length >= numberOfHeaderLines)
        {
            if((background = assets.get(strArray[0])) == null)
            {
                Gdx.app.error("MissionReadError", filename + ": Texture '" + strArray[0] + "' is not found.");
                System.exit(1);
            }

            if((backgroundMusic = assets.get(strArray[1])) == null)
            {
                Gdx.app.error("MissionReadError", filename + ": Music '" + strArray[1] + "' is not found.");
                System.exit(1);
            }

            try
            {
                endingTime = Integer.parseInt(strArray[2]);
                scrollSpeed = Integer.parseInt(strArray[3]);
                backgroundLooping = Integer.parseInt(strArray[4]) != 0;
            }
            catch(NumberFormatException e)
            {
                Gdx.app.error("MissionReadError", filename + ": EndingTime, bgLooping, or scrollSpeed parameters' format is faulty.");
                System.exit(1);
            }

            for (int i = numberOfHeaderLines; i < strArray.length; i++)
            {
                String [] spawnInfoStr = strArray[i].split(",\\s+");
                int id;
                float positionX, positionY, spawnTime;
                SpawnInfo spawnInfo;
                try
                {
                    id = Integer.parseInt(spawnInfoStr[0]);
                    positionX = Float.parseFloat(spawnInfoStr[1]);
                    positionY = Float.parseFloat(spawnInfoStr[2]);
                    spawnTime = Float.parseFloat(spawnInfoStr[3]);

                    Encounter encounter = EncounterBuilder.create(EncounterType.getByValue(id), assets);
                    encounter.setPosition(new Vector2(positionX, positionY));
                    spawnInfo = new SpawnInfo(encounter, spawnTime);
                    spawnInfoList.add(spawnInfo);
                }
                catch (NumberFormatException e)
                {
                    Gdx.app.error("MissionReadError", filename + ": Spawn info on line " + i + " faulty.");
                    System.exit(1);
                }

            }
        }
        else
        {
            Gdx.app.error("MissionReadError", "File '" + filename + "' is faulty.");
            System.exit(1);
        }

    }

    public Texture getBackground() {return background;}
    public Music getBackgroundMusic() {return backgroundMusic;}
    public boolean isBackgroundLooping() {return backgroundLooping;}
    public boolean isMissionOver() {return missionOver;}
    public float getScrollSpeed() {return scrollSpeed;}

    private final int numberOfHeaderLines = 5;
    private Texture background;
    private Music backgroundMusic;
    private boolean backgroundLooping;
    private boolean missionOver;
    private float scrollSpeed;
    private float runningTime;
    private float endingTime;
    private ArrayList<SpawnInfo> spawnInfoList;
    private ArrayList<Encounter> enemyList;
}
