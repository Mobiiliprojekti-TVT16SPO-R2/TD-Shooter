package tdshooter.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class ShopItemSlot extends Stage {

    private Texture greyBoxTexture;
    private Texture itemTexture;
    private Texture itemTexture2;
    private Texture itemTexture3;
    private Texture itemTexture4;
    private Image greyBoxImage;
    private Image itemImage;
    private Label itemNameLabel;
    private TextButton itemButton;

    private TDShooterGdxGame game;
    private int VIEWPORTWIDTH = 720;
    private int VIEWPORTHEIGHT = 1280;

    public ShopItemSlot(StretchViewport viewport, Skin skin, TDShooterGdxGame gam, int itemIndex, int weaponLevel) {
        super(viewport, gam.batch);
        game = gam;

        VIEWPORTWIDTH = 720; //viewport.getScreenWidth();
        VIEWPORTHEIGHT = 1280; //viewport.getScreenHeight();

        greyBoxTexture = game.assets.get("Menu/hangarvalikkoboksi.png");
        switch (itemIndex){
            case 1:
                itemTexture = game.assets.get("shopitems/Item_Armor.png");
                break;
            case 2:
                itemTexture = game.assets.get("shopitems/Weapon_MachineGunCannon.png");
                break;
            case 3:
                itemTexture = game.assets.get("shopitems/Weapon_ScatterCannon.png");
                break;
            case 4:
                itemTexture = game.assets.get("shopitems/Weapon_ElephantCannon.png");
                break;
            default:
                itemTexture = game.assets.get("shopitems/Item_Armor.png");
                break;
        }

        itemImage = new Image(itemTexture);
        greyBoxImage = new Image(greyBoxTexture);

    }

    public void setPosition(int x_position, int y_position) {

    }
}
