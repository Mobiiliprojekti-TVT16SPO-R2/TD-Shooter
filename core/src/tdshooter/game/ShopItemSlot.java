package tdshooter.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Label itemInfoLabel;
    private TextButton itemButton;

    private TDShooterGdxGame game;
    private int greyboxWidth = 480;
    private int greyboxHeight = 240;
    private int itemSlotWidth = 220;
    private int itemSlotHeight = 200;
    private int itemSlotMargin = 20;

    public ShopItemSlot(StretchViewport viewport, Skin skin, TDShooterGdxGame gam, int itemIndex, int weaponLevel) {
        super(viewport, gam.batch);
        game = gam;

        //STYLING
        BitmapFont font = skin.getFont("font");
        font.getData().setLineHeight(font.getLineHeight() - font.getSpaceWidth() + 5);
        Label.LabelStyle yellowLabelstyle =
                new Label.LabelStyle(font, Color.YELLOW);
        Label.LabelStyle whiteLabelstyle =
                new Label.LabelStyle(font, Color.WHITE);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.overFontColor = Color.BLUE;
        textButtonStyle.downFontColor = Color.YELLOW;
        textButtonStyle.fontColor = Color.BLACK;

        greyBoxTexture = game.assets.get("Menu/hangarvalikkoboksi.png");
        switch (itemIndex){
            case 1:
                itemTexture = game.assets.get("shopitems/Item_Armor.png");
                itemNameLabel = new Label("Shield", yellowLabelstyle);
                itemInfoLabel = new Label("Some extra hitpoints", whiteLabelstyle);
                break;
            case 2:
                itemTexture = game.assets.get("shopitems/Weapon_MachineGunCannon.png");
                itemNameLabel = new Label("Machinegun", yellowLabelstyle);
                itemInfoLabel = new Label("current level: " + weaponLevel, whiteLabelstyle);
                break;
            case 3:
                itemTexture = game.assets.get("shopitems/Weapon_ScatterCannon.png");
                itemNameLabel = new Label("Twin cannons", yellowLabelstyle);
                itemInfoLabel = new Label("current level: " + weaponLevel, whiteLabelstyle);
                break;
            case 4:
                itemTexture = game.assets.get("shopitems/Weapon_ElephantCannon.png");
                itemNameLabel = new Label("Scattergun", yellowLabelstyle);
                itemInfoLabel = new Label("current level: " + weaponLevel, whiteLabelstyle);
                break;
            default:
                itemTexture = game.assets.get("shopitems/Item_Armor.png");
                itemNameLabel = new Label("Empty item", yellowLabelstyle);
                itemInfoLabel = new Label("not available", whiteLabelstyle);
                break;
        }

        itemImage = new Image(itemTexture);
        itemImage.setWidth(itemSlotWidth);
        itemImage.setHeight(itemSlotHeight);

        greyBoxImage = new Image(greyBoxTexture);
        greyBoxImage.setWidth(greyboxWidth);
        greyBoxImage.setHeight(greyboxHeight);

        itemButton = new TextButton("Level: " + weaponLevel, skin);  //skinin tilalta ehkä labelstyle?? ja visujen oma nappi

    }

    public void setPosition(int x, int y) {
        greyBoxImage.setPosition(x, y);
        itemImage.setPosition(x+itemSlotMargin,y+itemSlotMargin);
        itemNameLabel.setPosition(x + itemImage.getWidth() + itemSlotMargin*2,y + greyBoxImage.getHeight() - itemSlotMargin);
        itemInfoLabel.setPosition(x + itemImage.getWidth() + itemSlotMargin*2, y + itemSlotMargin + itemImage.getHeight() - itemNameLabel.getHeight() - itemSlotMargin);
        itemButton.setPosition(x + itemImage.getWidth() + itemSlotMargin*2, y + itemSlotMargin);
    }

    public void update(float delta) {
        super.act(delta);
    }
}
