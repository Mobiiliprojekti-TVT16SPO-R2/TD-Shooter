package tdshooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private TDShooterGdxGame game;

    private int TOPBARHEIGHT = 60;
    private int VIEWPORTWIDTH;
    private int VIEWPORTHEIGHT;
    private int currentScreen;

    private int currency;

    public MenuTopBar(Viewport viewport, Skin skin, TDShooterGdxGame gam, int screenindex) {

        super(viewport, gam.batch);
        game = gam;

        currentScreen = screenindex;

        VIEWPORTWIDTH = viewport.getScreenWidth();
        VIEWPORTHEIGHT = viewport.getScreenHeight();

        leftButtonOffTexture = game.assets.get("Menu/vasen-normaali.png");
        leftButtonOnTexture = game.assets.get("Menu/vasen-valittu.png");
        middleButtonOffTexture = game.assets.get("Menu/keskimmainen-normaali.png");
        middleButtonOnTexture = game.assets.get("Menu/keskimmainen-valittu.png");
        rightButtonOffTexture = game.assets.get("Menu/oikea-normaali.png");
        rightButtonOnTexture = game.assets.get("Menu/oikea-valittu.png");
        topBarTexture = game.assets.get("Menu/ylapalkki2.png");

        leftButtonOffImage = new Image(leftButtonOffTexture);
        leftButtonOnImage = new Image(leftButtonOnTexture);
        middleButtonOffImage = new Image(middleButtonOffTexture);
        middleButtonOnImage = new Image(middleButtonOnTexture);
        rightButtonOffImage = new Image(rightButtonOffTexture);
        rightButtonOnImage = new Image(rightButtonOnTexture);
        topBarImage = new Image(topBarTexture);

        //Setting sizes
        bridgeButton.setWidth(190);
        bridgeButton.setHeight(110);
        hangarButton.setWidth(195);
        hangarButton.setHeight(110);
        shopButton.setWidth(190);
        shopButton.setHeight(110);

        leftButtonOnImage.setWidth(190);
        leftButtonOnImage.setHeight(110);
        leftButtonOffImage.setWidth(190);
        leftButtonOffImage.setHeight(110);
        middleButtonOffImage.setWidth(195);
        middleButtonOffImage.setHeight(110);
        middleButtonOnImage.setWidth(195);
        middleButtonOffImage.setHeight(110);
        rightButtonOffImage.setWidth(190);
        rightButtonOffImage.setHeight(110);
        rightButtonOnImage.setWidth(190);
        rightButtonOnImage.setHeight(110);

        //Setting positions
        bridgeButton.setPosition(70, VIEWPORTHEIGHT - bridgeButton.getHeight() - TOPBARHEIGHT);
        hangarButton.setPosition(70 + bridgeButton.getWidth(), VIEWPORTHEIGHT - hangarButton.getHeight() - TOPBARHEIGHT);
        shopButton.setPosition(70 + bridgeButton.getWidth() + hangarButton.getWidth(), VIEWPORTHEIGHT - hangarButton.getHeight() - TOPBARHEIGHT);

        leftButtonOffImage.setPosition(bridgeButton.getX(),bridgeButton.getY());
        leftButtonOnImage.setPosition(bridgeButton.getX(),bridgeButton.getY());
        middleButtonOffImage.setPosition(hangarButton.getX(),hangarButton.getY());
        middleButtonOnImage.setPosition(hangarButton.getX(),hangarButton.getY());
        rightButtonOffImage.setPosition(shopButton.getX(),shopButton.getY());
        rightButtonOnImage.setPosition(shopButton.getX(),shopButton.getY());

        topBarImage.setWidth(viewport.getScreenWidth());
        topBarImage.setPosition(0, VIEWPORTHEIGHT - topBarImage.getHeight());  //VIEWPORTHEIGHT - topbarImage.getImageY()

        bridgeButton = new TextButton("Bridge", skin);
        hangarButton = new TextButton("Hangar", skin);
        shopButton = new TextButton("Shop", skin);

        this.addActor(dialogImage);
        this.addActor(textLabel);
        setPosition(0,0);

        bridgeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(1);
            }
        });
        hangarButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(2);
            }
        });

        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(3);
            }
        });

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

    private void setScreen(int selection) {
        switch (selection) {
            case 1:
                game.setScreen(new MissionsMenu(game));
                break;
            case 2:
                game.setScreen(new HangarScreen(game));
                break;
            case 3:
                game.setScreen(new ShopScreen(game));
                break;
            default:
                break;
        }
    }

    public void update(float delta) {
        super.act(delta);
    }
}
