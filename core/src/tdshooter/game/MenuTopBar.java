package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuTopBar extends Stage {

    private Label currencyLabel;
    private Label selectedScreenLabel;
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
    private TextButton bridgeButton;
    private TextButton hangarButton;
    private TextButton shopButton;
    private TDShooterGdxGame game;
    private boolean readyForNextScreen = false;

    private int TOPBARHEIGHT = 58;
    private int VIEWPORTWIDTH;
    private int VIEWPORTHEIGHT;
    private int currentScreen = 1;
    private int screenToSet = 1;
    private Preferences prefs;

    private int currency = 0;

    public MenuTopBar(Viewport viewport, Skin skin, TDShooterGdxGame gam, int screenindex ) {

        super(viewport, gam.batch);
        game = gam;

        currentScreen = screenindex;

        VIEWPORTWIDTH = 720; //viewport.getScreenWidth();
        VIEWPORTHEIGHT = 1280; //viewport.getScreenHeight();

        Gdx.app.log("DEBUG", "Viewportsize in MenuTopBar: " + VIEWPORTWIDTH + "; " + VIEWPORTHEIGHT);

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

        BitmapFont font = skin.getFont("font");
        font.getData().setLineHeight(font.getLineHeight() - font.getSpaceWidth() + 5);
        Label.LabelStyle myLabelstyle =
                new Label.LabelStyle(font, Color.BLUE);
        selectedScreenLabel = new Label("no name", myLabelstyle);
        selectedScreenLabel.setAlignment(Align.center);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.overFontColor = Color.BLUE;
        textButtonStyle.downFontColor = Color.YELLOW;
        textButtonStyle.fontColor = Color.BLACK;


        bridgeButton = new TextButton("Bridge", textButtonStyle);
        hangarButton = new TextButton("Hangar", textButtonStyle);
        shopButton = new TextButton("Shop", textButtonStyle);

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

        topBarImage.setWidth(VIEWPORTWIDTH);
        topBarImage.setPosition(0, VIEWPORTHEIGHT - topBarImage.getHeight());  //VIEWPORTHEIGHT - topbarImage.getImageY()

        // GET CURRENCY FROM PREFERENCES
        prefs = Gdx.app.getPreferences("savedata");
        if(prefs.contains("currency")) {
            currency = prefs.getInteger("currency", 0);
        }

        currencyLabel = new Label("Currency:  " + currency, myLabelstyle);
        currencyLabel.setFontScale(0.7f);
        currencyLabel.setPosition(VIEWPORTWIDTH - currencyLabel.getWidth() - 30, VIEWPORTHEIGHT - currencyLabel.getHeight() );

        //Adding Actors to the stage
        this.addActor(topBarImage);
        this.addActor(currencyLabel);   // ADD WHEN YOU ADD CURRENCYLABEL TO THE SCREEN!!!!

        //some depending on setup
        switch (currentScreen) {
            case 1:     // bridge screen
                this.addActor(leftButtonOnImage);
                this.addActor(middleButtonOffImage);
                this.addActor(rightButtonOffImage);
                this.addActor(hangarButton);
                this.addActor(shopButton);
                selectedScreenLabel.setText("Bridge");
                selectedScreenLabel.setWidth(bridgeButton.getWidth());
                selectedScreenLabel.setHeight(bridgeButton.getHeight());
                selectedScreenLabel.setPosition(bridgeButton.getX(), bridgeButton.getY());
                this.addActor(selectedScreenLabel);
                break;
            case 2:     // hangar screen
                this.addActor(leftButtonOffImage);
                this.addActor(middleButtonOnImage);
                this.addActor(rightButtonOffImage);
                this.addActor(bridgeButton);
                this.addActor(shopButton);
                selectedScreenLabel.setText("Hangar");
                selectedScreenLabel.setWidth(hangarButton.getWidth());
                selectedScreenLabel.setHeight(hangarButton.getHeight());
                selectedScreenLabel.setPosition(hangarButton.getX(), hangarButton.getY());
                this.addActor(selectedScreenLabel);
                break;
            case 3:     // shop screen
                this.addActor(leftButtonOffImage);
                this.addActor(middleButtonOffImage);
                this.addActor(rightButtonOnImage);
                this.addActor(bridgeButton);
                this.addActor(hangarButton);
                selectedScreenLabel.setText("Shop");
                selectedScreenLabel.setWidth(shopButton.getWidth());
                selectedScreenLabel.setHeight(shopButton.getHeight());
                selectedScreenLabel.setPosition(shopButton.getX(), shopButton.getY());
                this.addActor(selectedScreenLabel);
                break;
            default:
                break;
        }

        bridgeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenToSet = 1;
                readyForNextScreen = true;
            }
        });
        hangarButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenToSet = 2;
                readyForNextScreen = true;
            }
        });

        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenToSet = 3;
                readyForNextScreen = true;
            }
        });

    }

    public void setNextScreen() {
        switch (screenToSet) {
            case 1:
                dispose();
                game.setScreen(new MissionsMenu(game));
                break;
            case 2:
                dispose();
                game.setScreen(new HangarScreen(game));
                break;
            case 3:
                dispose();
                game.setScreen(new ShopScreen(game));
                break;
            default:
                dispose();
                game.setScreen(new MissionsMenu(game));
                break;
        }
    }

    public void update(float delta) {
        super.act(delta);
    }

    public boolean isReadyForNextScreen() {
        return readyForNextScreen;
    }


}
