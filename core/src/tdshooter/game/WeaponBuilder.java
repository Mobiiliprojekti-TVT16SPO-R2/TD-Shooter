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
    WEAPON01(0), WEAPON02(1), WEAPON03(2);

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

        Sound firingSound1 = assets.get("Sounds/hitSound.wav");
        Texture firingImage1 = assets.get("Bullets/bullet1_small.png");

        Weapon weapon;
        switch(weaponType)
        {
            case WEAPON01:
                weapon = new Weapon(1, 1, (long) 70000000, false, 20, firingSound1, firingImage1);
                break;

            case WEAPON02:
                weapon = new Weapon(1, 2, (long) 90000000, false, 0, firingSound1, firingImage1);
                break;

            case WEAPON03:
                weapon = new Weapon(1, 3, (long) 300000000, false, 80, firingSound1, firingImage1);
                break;

            default:
                weapon = new Weapon(1, 1, (long) 70000000, false, 20, firingSound1, firingImage1);
                break;
        }
        return weapon;
    }
}

