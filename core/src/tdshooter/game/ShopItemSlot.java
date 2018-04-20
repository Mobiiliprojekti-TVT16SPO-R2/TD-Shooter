package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class ShopItemSlot extends Stage {

    private Texture greyBoxTexture;
    private Texture itemTexture;
    private Texture itemShadowTexture;
    private Texture buttonOffTexture;
    private Texture buttonOnTexture;
    private Image greyBoxImage;
    private Image itemImage;
    private Image itemShadowImage;
    private Image buttonOffImage;
    private Image buttonOnImage;
    private Label itemNameLabel;
    private Label itemInfoLabel;
    private TextButton itemButton;
    private Sound cashSound;

    private Label.LabelStyle blackLabelstyle;
    private Label.LabelStyle whiteLabelstyle;
    private Label.LabelStyle yellowLabelstyle;
    private Label.LabelStyle redLabelstyle;

    private TDShooterGdxGame game;
    private int greyboxWidth = 480;
    private int greyboxHeight = 240;
    private int itemSlotWidth = 160;
    private int itemSlotHeight = 200;
    private int itemSlotMargin = 20;

    private int itemIndex;
    private int price = 0;
    private int currentweaponLevel;
    private int weaponMaxLevel = 3;
    private Preferences prefs;

    public ShopItemSlot(StretchViewport viewport, Skin skin, TDShooterGdxGame gam, final int itemIndex, int weaponLevel) {
        super(viewport, gam.batch);
        game = gam;

        prefs = Gdx.app.getPreferences("savedata");

        currentweaponLevel = weaponLevel;
        this.itemIndex = itemIndex;

        //STYLING
        BitmapFont font = skin.getFont("font");
        blackLabelstyle = new Label.LabelStyle(font, Color.BLACK);
        yellowLabelstyle = new Label.LabelStyle(font, Color.YELLOW);
        whiteLabelstyle = new Label.LabelStyle(font, Color.WHITE);
        redLabelstyle = new Label.LabelStyle(font, Color.MAROON);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.downFontColor = Color.YELLOW;
        textButtonStyle.fontColor = Color.WHITE;

        cashSound = game.assets.get("Sounds/cashier.wav");

        greyBoxTexture = game.assets.get("Menu/hangarvalikkoboksi.png");
        itemShadowTexture = game.assets.get("shopitems/raksi.png");
        buttonOffTexture = game.assets.get("Menu/startmenu-painike-pohjassa.png");
        buttonOnTexture = game.assets.get("Menu/startmenu-painike-normaali.png");
        buttonOffImage = new Image(buttonOffTexture);
        buttonOnImage = new Image(buttonOnTexture);
        switch (itemIndex){
            case 1:
                itemTexture = game.assets.get("shopitems/Item_Armor.png");
                itemNameLabel = new Label("Shield", blackLabelstyle);
                itemInfoLabel = new Label("Extra 50 hp", whiteLabelstyle);
                price = 100;
                break;
            case 2:
                itemTexture = game.assets.get("shopitems/Weapon_MachineGunCannon.png");
                itemNameLabel = new Label("Machinegun", blackLabelstyle);
                itemInfoLabel = new Label("current level: " + weaponLevel, whiteLabelstyle);
                price = 60 + weaponLevel* 40;
                break;
            case 3:
                itemTexture = game.assets.get("shopitems/Weapon_ScatterCannon.png");
                itemNameLabel = new Label("Twin cannons", blackLabelstyle);
                itemInfoLabel = new Label("current level: " + weaponLevel, whiteLabelstyle);
                price = 100 + weaponLevel* 50;
                break;
            case 4:
                itemTexture = game.assets.get("shopitems/Weapon_ElephantCannon.png");
                itemNameLabel = new Label("Scattergun", blackLabelstyle);
                itemInfoLabel = new Label("current level: " + weaponLevel, whiteLabelstyle);
                price = 200 + weaponLevel* 100;
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

        itemShadowImage = new Image(itemShadowTexture);
        itemShadowImage.setWidth(itemSlotWidth);
        itemShadowImage.setHeight(itemSlotHeight);

        greyBoxImage = new Image(greyBoxTexture);
        greyBoxImage.setWidth(greyboxWidth);
        greyBoxImage.setHeight(greyboxHeight);

        if (weaponLevel == 0) {
            itemButton = new TextButton("Buy: " + price, textButtonStyle);
        } else {
            itemButton = new TextButton("Upgrade: " + price, textButtonStyle);
        }
        itemButton.setWidth(greyboxWidth - itemSlotWidth - itemSlotMargin*3);

        buttonOffImage.setWidth(itemButton.getWidth());
        buttonOffImage.setHeight(itemButton.getHeight() + 40);
        buttonOnImage.setWidth(itemButton.getWidth());
        buttonOnImage.setHeight(itemButton.getHeight() + 40);

        itemButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int currentCurrency = 0;
                if(prefs.contains("currency")) {
                    currentCurrency = prefs.getInteger("currency", 0);
                }
                if(currentCurrency - price >= 0)
                {
                    if(currentweaponLevel < weaponMaxLevel)
                    {
                        currentCurrency -= price;
                        currentweaponLevel++;
                        cashSound.play();
                        prefs.putInteger("currency", currentCurrency);
                        switch (itemIndex) {
                            case 1:
                                prefs.putInteger("shield", 50);
                                disableItem();
                                break;
                            case 2:
                                prefs.putInteger("weapon01", currentweaponLevel);
                                price = 60 + currentweaponLevel* 40;
                                break;
                            case 3:
                                prefs.putInteger("weapon02", currentweaponLevel);
                                price = 100 + currentweaponLevel* 50;
                                break;
                            case 4:
                                prefs.putInteger("weapon03", currentweaponLevel);
                                price = 200 + currentweaponLevel* 100;
                                break;
                            default:
                                break;
                        }
                        checkIfMaxLevel();
                        prefs.flush();
                    }
                }
            }
        });

        this.addActor(greyBoxImage);
        this.addActor(itemImage);
        this.addActor(itemNameLabel);
        this.addActor(itemInfoLabel);
        this.addActor(buttonOnImage);
        this.addActor(buttonOffImage);
        buttonOffImage.setVisible(false);
        this.addActor(itemButton);
        checkIfMaxLevel();
    }

    private void checkIfMaxLevel(){
        if (itemIndex > 1){
            if (currentweaponLevel == weaponMaxLevel){
                disableItem();
                itemInfoLabel.setText("MAX LEVEL !!");
                itemInfoLabel.setStyle(yellowLabelstyle);
            } else {
                if (currentweaponLevel == 0) {
                    itemButton.setText("Buy: " + price);
                    itemInfoLabel.setText("Not owned yet");
                } else {
                    itemButton.setText("Upgrade: " + price);
                    itemInfoLabel.setText("current level: " + currentweaponLevel);
                }
            }
        }
    }

    public void disableItem(){
        itemInfoLabel.setText("Sold out!!");
        itemInfoLabel.setStyle(redLabelstyle);
        itemButton.setText("-disabled-");
        buttonOffImage.setVisible(true);
        this.addActor(itemShadowImage);
    }

    public void setPosition(int x, int y) {
        greyBoxImage.setPosition(x, y);
        itemImage.setPosition(x+itemSlotMargin,y+itemSlotMargin);
        itemShadowImage.setPosition(x+itemSlotMargin,y+itemSlotMargin);
        itemNameLabel.setPosition(x + itemImage.getWidth() + itemSlotMargin*2,y + itemImage.getHeight() - itemSlotMargin );
        itemInfoLabel.setPosition(x + itemImage.getWidth() + itemSlotMargin*2, y +  itemImage.getHeight() - itemNameLabel.getHeight() - itemSlotMargin );
        itemButton.setPosition(x + itemImage.getWidth() + itemSlotMargin*2, y + itemSlotMargin*2);
        buttonOffImage.setPosition(itemButton.getX(), itemButton.getY() - 20);
        buttonOnImage.setPosition(itemButton.getX(), itemButton.getY() - 20);
    }

    public void update(float delta) {
        super.act(delta);
    }
}
