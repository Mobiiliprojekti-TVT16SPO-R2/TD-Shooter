package tdshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuTopBar extends Stage {

    private Label currencyLabel;
    private Texture topBarTexture;
    private Texture leftButtonOnTexture;
    private Texture leftButtonOffTexture;
    private Texture rightButtonOnTexture;
    private Texture rightButtonOffTexture;
    private Texture middleButtonOnTexture;
    private Texture middleButtonOffTexture;
    private Image topBarImage;
    private Image leftButtonOnImage;
    private Image leftButtonOffImage;
    private Image rightButtonOnImage;
    private Image rightButtonOffImage;
    private Image middleButtonOnImage;
    private Image middleButtonOffImage;
    private TextButton launchButton;
    private TextButton bridgeButton;
    private TextButton hangarButton;
    private TextButton shopButton;

    private int TOPBARHEIGHT = 60;
    private int VIEWPORTWIDTH;
    private int VIEWPORTHEIGHT;

    private int currency;

    public MenuTopBar(Viewport viewport, Batch batch, Skin skin, AssetManager assets) {

        super(viewport, batch);

        VIEWPORTWIDTH = viewport.getScreenWidth();
        VIEWPORTHEIGHT = viewport.getScreenHeight();

        leftButtonOffTexture = assets.get("Menu/vasen-normaali.png");
        leftButtonOnTexture = assets.get("Menu/vasen-valittu.png");
        middleButtonOffTexture = assets.get("Menu/keskimmainen-normaali.png");
        middleButtonOnTexture = assets.get("Menu/keskimmainen-valittu.png");
        rightButtonOffTexture = assets.get("Menu/oikea-normaali.png");
        rightButtonOnTexture = assets.get("Menu/oikea-valittu.png");
        topBarTexture = assets.get("Menu/ylapalkki2.png");

        leftButtonOffImage = new Image(leftButtonOffTexture);
        leftButtonOnImage = new Image(leftButtonOnTexture);
        middleButtonOffImage = new Image(middleButtonOffTexture);
        middleButtonOnImage = new Image(middleButtonOnTexture);
        rightButtonOffImage = new Image(rightButtonOffTexture);
        rightButtonOnImage = new Image(rightButtonOnTexture);
        topBarImage = new Image(topBarTexture);

        topBarImage.setWidth(viewport.getScreenWidth());
        topBarImage.setPosition(0, VIEWPORTHEIGHT - topBarImage.getHeight());  //VIEWPORTHEIGHT - topbarImage.getImageY()

        bridgeButton = new TextButton("Bridge", skin);
        hangarButton = new TextButton("Hangar", skin);
        shopButton = new TextButton("Shop", skin);

//        if(prefs.contains("currency") == false) {
//            prefs.putInteger("currency", 0);
//            prefs.flush();
//        }
//
//
//        currencyLabel = new Label("Currency " + currency, skin);
//        currencyLabel.setFontScale(0.5f);
//
//        table.add(currencyLabel).left().top();
//        table.add(healthBar).width(300.0f).right().top().expand();
//        table.row();
//        table.add(superWeaponButton).width(200).bottom();
//        this.addActor(table);
    }
}
