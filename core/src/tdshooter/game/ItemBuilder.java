package tdshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

enum ItemType
{
    NONE(0),HEALTH(1), COOLDOWN_REDUCTION(2), WEAPON_UPGRADE(3), CURRENCY(4);

    private int value;

    private ItemType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static ItemType getByValue(int value) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.getValue() == value) {
                return itemType;
            }
        }
        return null;
    }
}

public class ItemBuilder
{
    static public Item create(ItemType itemType, AssetManager assets)
    {
        Item item;
        switch(itemType)
        {
            case HEALTH:
                item = new Item(-500, 0, 64, 64, 100, 1, (Texture)assets.get("Items/healthpack_test.png"));
                break;
            case COOLDOWN_REDUCTION:
                item = new Item(-500, 0, 64, 64, 100, 2, (Texture)assets.get("Items/flightspeed_test.png"));
                break;
            case WEAPON_UPGRADE:
                item = new Item(-500, 0, 64, 64, 100, 3, (Texture)assets.get("Items/armor_test.png"));
                break;
            case CURRENCY:
                item = new Item(-500, 0, 64, 64, 100, 4, (Texture)assets.get("Items/currency_test.png"));
                break;
            case NONE:
                item = null;
                break;
            default:
                item = null;
                break;
        }
        return item;
    }
}

