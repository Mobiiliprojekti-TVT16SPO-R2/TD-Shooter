package tdshooter.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameHUD extends Stage
{
    private Player player;
    private Label scoreLabel;
    private Label missionLabel;
    private Label currencyLabel;
    private Label healthLabel;
    private TextButton superWeaponButton;
    private TextButton changeWeaponButton;
    private Texture topbarTexture;
    private Texture healthtbarTexture;
    private Texture healthtbarTexture2;
    private Texture changeWeaponTexture;
    private Texture nukeTexture;
    private Image changeWeaponImage;
    private Image topbarImage;
    private Image healthbarImage;
    private Image healthbarImage2;
    private Image nukeImage;

    private int buttonWidth = 260;
    private int buttonHeight = 110;
    private boolean superWeaponClicked = false;

    public GameHUD(Viewport viewport, Batch batch, Skin skin, final Player player, int missionNumber, final TDShooterGdxGame game)
    {
        super(viewport, batch);
        this.player = player;

        BitmapFont font = skin.getFont("font");

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.overFontColor = Color.YELLOW;
        textButtonStyle.downFontColor = Color.YELLOW;
        textButtonStyle.fontColor = Color.WHITE;


        topbarTexture = game.assets.get("Menu/pelitila-ylapalkki_v2.png");
        healthtbarTexture = game.assets.get("Menu/pelitila-healthbar_v2.png");
        healthtbarTexture2 = game.assets.get("Menu/pelitila-healthbar_jaljella_v3.png");
        changeWeaponTexture = game.assets.get("Menu/pelitila-equipmentjuttu_v2.png");
        nukeTexture = game.assets.get("Menu/pelitila-equipmentjuttu_mirrored.png");
        topbarImage = new Image(topbarTexture);
        changeWeaponImage = new Image(changeWeaponTexture);
        nukeImage = new Image(nukeTexture);
        healthbarImage = new Image(healthtbarTexture2);
        healthbarImage2 = new Image(healthtbarTexture);


        superWeaponButton = new TextButton("Nuke: " + player.getSuperWeaponInt(), textButtonStyle);
        superWeaponButton.getLabel().setAlignment(Align.bottom);
        superWeaponButton.pad(10);
        superWeaponButton.setWidth(250);
        superWeaponButton.setHeight(131);
        superWeaponButton.setPosition(0, 0);
        superWeaponButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int i = player.getSuperWeaponInt();
                if (i > 0) {
                    superWeaponClicked = true;
                    player.setSuperWeaponInt();
                }
            }
        });
        changeWeaponButton = new TextButton("Gun: " + player.getWeaponChoice(), textButtonStyle);
        changeWeaponButton.getLabel().setAlignment(Align.bottom);
        changeWeaponButton.pad(10);
        changeWeaponButton.setWidth(250);
        changeWeaponButton.setHeight(131);
        changeWeaponButton.setPosition(game.VIEWPORTWIDTH - changeWeaponButton.getWidth(), 0);
        changeWeaponButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    player.swapWeapons();
            }
        });

        missionLabel = new Label("Mission " + missionNumber, skin);
        missionLabel.setFontScale(0.9f);
        scoreLabel = new Label("Score " + player.getPoints(), skin);
        scoreLabel.setFontScale(0.9f);
        currencyLabel = new Label("Currency " + player.getCurrency(), skin);
        currencyLabel.setFontScale(0.9f);

        healthLabel = new Label(player.getHitPoints() + "/" + player.getBaseMaxHitpoints(), skin);
        healthLabel.setFontScale(0.9f);

        healthbarImage.setScale(1.5f);
        healthbarImage2.setScale(1.5f);

        changeWeaponImage.setWidth(buttonWidth);
        changeWeaponImage.setPosition(changeWeaponButton.getX(), changeWeaponButton.getY());

        nukeImage.setWidth(buttonWidth);
        nukeImage.setPosition(superWeaponButton.getX(), superWeaponButton.getY());

        topbarImage.setPosition(0,1280 - topbarImage.getHeight());
        healthbarImage.setPosition(0, nukeImage.getHeight() + 100);
        healthbarImage2.setPosition(healthbarImage.getX(), healthbarImage.getY());
        healthLabel.setPosition(healthbarImage.getX(), healthbarImage.getY());

        scoreLabel.setPosition(game.VIEWPORTWIDTH / 2 - scoreLabel.getWidth() / 2, game.VIEWPORTHEIGHT - topbarImage.getHeight() / 2 - scoreLabel.getHeight() / 2);
        missionLabel.setPosition(60, game.VIEWPORTHEIGHT - topbarImage.getHeight() / 2 - missionLabel.getHeight() / 2);
        currencyLabel.setPosition(game.VIEWPORTWIDTH - 60 - currencyLabel.getWidth(), game.VIEWPORTHEIGHT - topbarImage.getHeight() / 2 - currencyLabel.getHeight() / 2);

        this.addActor(topbarImage);
        this.addActor(scoreLabel);
        this.addActor(missionLabel);
        this.addActor(changeWeaponImage);
        this.addActor(changeWeaponButton);
        this.addActor(nukeImage);
        this.addActor(superWeaponButton);
        this.addActor(healthbarImage);
        this.addActor(healthbarImage2);
        this.addActor(healthLabel);
    }

    public void update(float delta)
    {
        super.act(delta);
        float healthLeft = player.getHitPoints() * 100 / player.getBaseMaxHitpoints();
        healthLeft = healthLeft * 1.5f;
        healthbarImage2.setWidth(healthLeft);
        changeWeaponButton.setText("Gun: " + player.getWeaponChoice());
        superWeaponButton.setText("Nuke: " + player.getSuperWeaponInt());
        scoreLabel.setText("Score " + player.getPoints());
        healthLabel.setText(player.getHitPoints() + "/" + player.getBaseMaxHitpoints());
    }

    public boolean isSuperWeaponClicked() {
        return superWeaponClicked;
    }

    public void setSuperWeaponClicked() {
        superWeaponClicked = false;
    }
}
