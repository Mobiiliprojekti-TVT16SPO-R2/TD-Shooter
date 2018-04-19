package tdshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Samuli Lehto on 27.3.2018.
 */
enum EncounterType
{
    BASIC_ENEMY_LVL1(0), SHOOTING_ENEMY_LVL1(1), SHOOTING_ENEMY_LVL2(2), SHOOTING_ENEMY_LVL3(3), BASIC_ENEMY_LVL2(4), BASIC_ENEMY_LVL3(5), FIGHTERBRUISER_ENEMY(6), ORGANIC_MINE(7), EXPLOSIVE_MINE(7);

    private int value;

    EncounterType(int value)
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
            case BASIC_ENEMY_LVL1:
                encounter = new Encounter(1000, 2000, 64, 96,
                        20, 20,400 , 25,
                        (Texture)assets.get("Encounters/AlienBeast_LVL_1_Test.png"));
                break;

            case SHOOTING_ENEMY_LVL1:
                encounter = new ShootingEnemy(1000, 2000, 64, 112,
                        20, 20,300 , 1, 0, 500000000, 40,
                        50 , (Texture)assets.get("Encounters/AlienFighter_LVL_1_Test.png"), assets);
                break;

            case SHOOTING_ENEMY_LVL2:
                encounter = new ShootingEnemy(1000, 2000, 96, 128,
                        50, 40,180 , 2, 200, 2100000000, 300,
                        100 , (Texture)assets.get("Encounters/AlienFighter_LVL_2_VERSION2.png"), assets);
                break;

            case SHOOTING_ENEMY_LVL3:
                encounter = new ShootingEnemy(1000, 2000, 128, 128,
                        100, 60,160 , 3, 200, 2100000000, 300,
                        200 , (Texture)assets.get("Encounters/AlienFighter_LVL_3_VERSION2.png"), assets);
                break;

            case BASIC_ENEMY_LVL2:
                encounter = new Encounter(1000, 2000, 96, 96,
                        40, 30,350 , 50,
                        (Texture)assets.get("Encounters/AlienBeast_LVL_2.png"));
                break;

            case BASIC_ENEMY_LVL3:
                encounter = new Encounter(1000, 2000, 128, 128,
                        60, 50,300 , 150,
                        (Texture)assets.get("Encounters/AlienBeast_LVL_3.png"));
                break;

            case FIGHTERBRUISER_ENEMY:
                encounter = new ShootingEnemy(1000, 2000, 140, 140,
                        200, 60,160 , 5, 300, 1500000000, 280,
                        400 , (Texture)assets.get("Encounters/AlienFighter_Bruiser.png"), assets);
                break;
            default:
                encounter = new Encounter(1000, 2000, 64, 64,20, 20,100 , 25, (Texture)assets.get("Encounters/AlienBeast_LVL_1_Test.png"));
                break;
        }
        return encounter;
    }
}

