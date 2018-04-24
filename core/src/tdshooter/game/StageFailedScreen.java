package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.io.StreamCorruptedException;

public class StageFailedScreen implements Screen {
    private final TDShooterGdxGame game;
    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Skin skin;
    private Stage stage;
    private Texture retryTexture;
    private Texture missionTexture;
    private Image retryImage;
    private Image missionImage;
    private Texture backgroundTexture;
    private Image backgroundImage;

    private String previousMissionName;
    private int previousMissionNumber;


    public StageFailedScreen(final TDShooterGdxGame game, String missionName, int missionNumber) {
        this.game = game;
        previousMissionName = missionName;
        previousMissionNumber = missionNumber;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        skin = game.skin;

        retryTexture = game.assets.get("Menu/vasen-normaali.png");
        missionTexture = game.assets.get("Menu/oikea-normaali.png");
        backgroundTexture = game.assets.get("Menu/MissionFailed_Screen.png");
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setWidth(VIEWPORTWIDTH);
        backgroundImage.setHeight(VIEWPORTHEIGHT);

        retryImage = new Image(retryTexture);
        missionImage = new Image(missionTexture);


        stage = new Stage(viewport, game.batch);

        stage.addActor(backgroundImage);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act();
        stage.draw();
    }

    @Override
    public void show()
    {
        float midX = VIEWPORTWIDTH / 2;
        float midY = VIEWPORTHEIGHT / 2;

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.font;
        textButtonStyle.overFontColor = Color.BLUE;
        textButtonStyle.downFontColor = Color.YELLOW;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.font.getData().setScale(2.0f);

        TextButton retryButton = new TextButton("Retry", textButtonStyle);
        TextButton missionMenuButton = new TextButton("Missions", textButtonStyle);

        retryButton.setWidth(250);
        retryButton.setHeight(150);
        missionMenuButton.setWidth(250);
        missionMenuButton.setHeight(150);

        retryButton.setPosition(midX - retryButton.getWidth(),midY - 85);
        missionMenuButton.setPosition(midX, midY- 85);


        retryButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.setScreen(new GameScreen(game, previousMissionName, previousMissionNumber));
                dispose();
                return true;
            }
        });
        missionMenuButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.setScreen(new MissionsMenu(game));
                dispose();
                return true;
            }
        });

        Label missionFailedLabel = new Label("You Died!", skin);
        missionFailedLabel.setFontScale(2);
        missionFailedLabel.setPosition(midX - missionFailedLabel.getWidth(), midY + 95);

        retryImage.setPosition(retryButton.getX(), retryButton.getY() + 20);
        missionImage.setPosition(missionMenuButton.getX(), missionMenuButton.getY() + 20);

        stage.addActor(retryImage);
        stage.addActor(missionImage);
        stage.addActor(missionFailedLabel);
        stage.addActor(retryButton);
        stage.addActor(missionMenuButton);

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

    }
}
