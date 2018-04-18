package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    private String previousMissionName;
    private int previousMissionNumber;


    public StageFailedScreen(final TDShooterGdxGame game, String missionName, int missionNumber) {
        this.game = game;
        previousMissionName = missionName;
        previousMissionNumber = missionNumber;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        skin = new Skin();
        skin.add("font", game.fontSkin);
        skin.addRegions((TextureAtlas) game.assets.get("Skin/glassy-ui.atlas"));
        skin.load(Gdx.files.internal("Skin/glassy-ui.json"));

        stage = new Stage(viewport, game.batch);

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
        Table table = new Table();
        table.setFillParent(true);

        TextButton retryButton = new TextButton("Retry", skin);
        TextButton missionMenuButton = new TextButton("Missions", skin);
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

        Label missionFailedLabel = new Label("Dead!", skin);
        missionFailedLabel.setFontScale(2);

        table.add(missionFailedLabel).center().colspan(2);
        table.row();
        table.add(retryButton).center().right().space(20);
        table.add(missionMenuButton).center().left().space(20);

        stage.addActor(table);
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
