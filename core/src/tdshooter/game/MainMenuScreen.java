package tdshooter.game;

/**
 * Created by leevi on 16.3.2018.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;


/**
 * Created by leevi on 16.3.2018.
 */

public class MainMenuScreen implements Screen {

    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Skin skin;
    private Stage stage;
    private Texture menuBackground;
    private Texture menuButtonWhite;
    private Texture menuButtonYellow;
    private Texture gameLogoTexture;
    private Image menuImage;
    private Image startButtonImage;
    private Image optionsButtonImage;
    private Image exitButtonImage;
    private Image menuButtonOnImage;
    private Image gameLogoImage;

    private Sound menuSelectSound;

    private TextButton startGameButton;
    private TextButton optionsButton;
    private TextButton exitButton;

    private int buttonLeftMargin = 50;
    private int buttonSpacing = 10;
    private int buttonRightOffset = 20;
    private int buttonBottomMargin = 70;
    private int buttonWidth = 260;
    private int buttonHeight = 110;

    public MainMenuScreen(final TDShooterGdxGame gam) {
        game = gam;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        skin = gam.skin;

        BitmapFont font = skin.getFont("font");

        menuBackground = game.assets.get("Menu/Background_StartMenu.png");
        menuButtonWhite = game.assets.get("Menu/startmenu-painike-normaali.png");
        menuButtonYellow = game.assets.get("Menu/startmenu-painike-pohjassa.png");
        gameLogoTexture = game.assets.get("Menu/logo-lapinakyva.png");
        menuSelectSound = game.assets.get("Sounds/menublip2.wav");

        menuImage = new Image(menuBackground);
        menuImage.setHeight(VIEWPORTHEIGHT);
        menuImage.setWidth(VIEWPORTWIDTH);

        gameLogoImage = new Image(gameLogoTexture);

        startButtonImage = new Image(menuButtonYellow);
        optionsButtonImage = new Image(menuButtonWhite);
        exitButtonImage = new Image(menuButtonWhite);
        menuButtonOnImage = new Image(menuButtonWhite);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.overFontColor = Color.YELLOW;
        textButtonStyle.downFontColor = Color.YELLOW;
        textButtonStyle.fontColor = Color.WHITE;

        startGameButton = new TextButton("Start Game", textButtonStyle);
        optionsButton = new TextButton("Options", textButtonStyle);
        exitButton = new TextButton("Exit", textButtonStyle);

        //Setting sizes
        startGameButton.setWidth(buttonWidth);
        optionsButton.setWidth(buttonWidth);
        exitButton.setWidth(buttonWidth);
        startGameButton.setHeight(buttonHeight);
        optionsButton.setHeight(buttonHeight);
        exitButton.setHeight(buttonHeight);

        startButtonImage.setWidth(buttonWidth);
        optionsButtonImage.setWidth(buttonWidth);
        exitButtonImage.setWidth(buttonWidth);
        menuButtonOnImage.setWidth(buttonWidth);
        startButtonImage.setHeight(buttonHeight);
        optionsButtonImage.setHeight(buttonHeight);
        exitButtonImage.setHeight(buttonHeight);
        menuButtonOnImage.setHeight(buttonHeight);

        gameLogoImage.setWidth(500);
        gameLogoImage.setHeight(400);

        //Setting menubutton locations:
        exitButton.setPosition(buttonLeftMargin, buttonBottomMargin);
        optionsButton.setPosition(buttonLeftMargin + buttonRightOffset, exitButton.getY() + buttonHeight + buttonSpacing);
        startGameButton.setPosition(buttonLeftMargin + buttonRightOffset*2, optionsButton.getY() + buttonHeight + buttonSpacing);

        startButtonImage.setPosition(startGameButton.getX(),startGameButton.getY());
        optionsButtonImage.setPosition(optionsButton.getX(),optionsButton.getY());
        exitButtonImage.setPosition(exitButton.getX(),exitButton.getY());
        menuButtonOnImage.setPosition(-400, -400); // off-screen on purpose

        gameLogoImage.setPosition(110, 620);

        stage = new Stage(viewport, game.batch);
        Gdx.input.setCatchBackKey(false);

    }
    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuButtonOnImage.setPosition(startGameButton.getX(),startGameButton.getY());
                game.setScreen(new MissionsMenu(game));
                menuSelectSound.play();
                dispose();
            }
        });
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuButtonOnImage.setPosition(optionsButton.getX(),optionsButton.getY());
                game.setScreen(new OptionsMenu(game));
                menuSelectSound.play();
                dispose();
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuButtonOnImage.setPosition(exitButton.getX(),exitButton.getY());
                menuSelectSound.play();
                Gdx.app.exit();
            }
        });

        stage.addActor(menuImage);
        stage.addActor(gameLogoImage);

        stage.addActor(startButtonImage);
        stage.addActor(optionsButtonImage);
        stage.addActor(exitButtonImage);
        stage.addActor(menuButtonOnImage);

        stage.addActor(startGameButton);
        stage.addActor(optionsButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
//        skin.dispose();
//        atlas.dispose();
        stage.dispose();
    }
}