package tdshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.Hashtable;

/**
 * Created by Samuli Lehto on 27.3.2018.
 */


enum EncounterType
{
    BASIC_ENEMY(0), SHOOTING_ENEMY(1);

    private int value;

    private EncounterType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static EncounterType getByValue(int value) {
        for (EncounterType encounterType : EncounterType.values()) {
            if (encounterType.getValue() == value) {
                return encounterType;
            }
        }
        return null;
    }
}

public class EncounterBuilder
{

    static public Encounter create(EncounterType enemyType, AssetManager assets)
    {
        Encounter encounter;
        switch(enemyType)
        {
            case BASIC_ENEMY:
                encounter = new Encounter(240, 864, 64, 64,20, 20,100 , (Texture)assets.get("Encounters/AlienBeast_Test_1_small.png"));
                break;

            default:
                encounter = new Encounter(240, 864, 64, 64,20, 20,100 , (Texture)assets.get("Encounters/AlienBeast_Test_1_small.png"));
                break;
        }
        return encounter;
    }
}

