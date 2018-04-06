package tdshooter.game;

/**
 * Created by leevi on 16.3.2018.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    private StretchViewport viewport;
    private Skin skin;
    private Stage stage;
    private Texture menuBackground;
    private Image menuImage;


    public MainMenuScreen(final TDShooterGdxGame gam) {
        game = gam;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(720, 1280, camera);
        viewport.apply();

//        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
//        camera.update();
//        camera.setToOrtho(false, 480, 800);

        atlas = (TextureAtlas) game.assets.get("Skin/glassy-ui.atlas");
        skin = game.assets.get("Skin/glassy-ui.json");
        menuBackground = game.assets.get("Menu/Background_BaseMenu_720_1280.png");

        menuImage = new Image(menuBackground);

        stage = new Stage(viewport, game.batch);
        Gdx.input.setCatchBackKey(false);
    }
    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();
//        mainTable.top();

        TextButton playButton = new TextButton("Play", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MissionsMenu(game));
                dispose();
            }
        });
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new OptionsMenu(game));
                dispose();
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        mainTable.add(playButton);
        mainTable.row();
        mainTable.add(optionsButton);
        mainTable.row();
        mainTable.add(exitButton);

        stage.addActor(menuImage);
        stage.addActor(mainTable);
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