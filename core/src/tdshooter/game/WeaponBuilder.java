package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Samuli Lehto on 27.3.2018.
 */
enum WeaponType
{
    WEAPON01_LVL1(0), WEAPON01_LVL2(1), WEAPON01_LVL3(2),
    WEAPON02_LVL1(3), WEAPON02_LVL2(4), WEAPON02_LVL3(5),
    WEAPON03_LVL1(6), WEAPON03_LVL2(7), WEAPON03_LVL3(8);

    private int value;

    WeaponType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static WeaponType getByValue(int value) {
        for (WeaponType weaponType : WeaponType.values()) {
            if (weaponType.getValue() == value) {
                return weaponType;
            }
        }
        return null;
    }
}

public class WeaponBuilder
{
    static public Weapon create(WeaponType weaponType, AssetManager assets)
    {

        Sound firingSound1 = assets.get("Sounds/gun2.wav");
        Sound firingSound2 = assets.get("Sounds/gun1.wav");
        Sound firingSound3 = assets.get("Sounds/shotgun.wav");
        Texture firingImage1 = assets.get("Bullets/bullet1_small.png");

        Weapon weapon;
        switch(weaponType)
        {
            case WEAPON01_LVL1:
                weapon = new Weapon(2, 1, (long) 120000000, false, 20, firingSound1, firingImage1);
                break;
            case WEAPON01_LVL2:
                weapon = new Weapon(3, 1, (long) 120000000, false, 20, firingSound1, firingImage1);
                break;
            case WEAPON01_LVL3:
                weapon = new Weapon(4, 1, (long) 120000000, false, 20, firingSound1, firingImage1);
                break;

            case WEAPON02_LVL1:
                weapon = new Weapon(2, 2, (long) 150000000, false, 0, firingSound2, firingImage1);
                break;
            case WEAPON02_LVL2:
                weapon = new Weapon(2, 3, (long) 150000000, false, 0, firingSound2, firingImage1);
                break;
            case WEAPON02_LVL3:
                weapon = new Weapon(2, 4, (long) 150000000, false, 0, firingSound2, firingImage1);
                break;

            case WEAPON03_LVL1:
                weapon = new Weapon(5, 5, (long) 500000000, false, 80, firingSound3, firingImage1);
                break;
            case WEAPON03_LVL2:
                weapon = new Weapon(6, 5, (long) 400000000, false, 90, firingSound3, firingImage1);
                break;
            case WEAPON03_LVL3:
                weapon = new Weapon(7, 5, (long) 350000000, false, 100, firingSound3, firingImage1);
                break;

            default:
                weapon = new Weapon(1, 1, (long) 120000000, false, 20, firingSound1, firingImage1);
                break;
        }
        return weapon;
    }
}

