package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by johan on 5.4.2018.
 */

public class OptionsMenu implements Screen {

    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    private Viewport viewport;
    private Skin skin;
    private Stage stage;
    private float soundVolume = 0.5f;
    private float musicVolume = 0.5f;
    private Label musicLabel;
    private Label soundLabel;
    private Slider musicSlider;
    private Slider soundSlider;

    public OptionsMenu(final  TDShooterGdxGame gam){
        game = gam;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();
        atlas = new TextureAtlas("Skin/glassy-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skin/glassy-ui.json"), atlas);

//        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
//        camera.update();

        musicLabel = new Label("Music", skin);
        musicLabel.setScale(4,4);


        stage = new Stage(viewport, game.batch);

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);


        musicSlider = new Slider(0.0f, 1.0f, 0.05f, false, skin);
        soundSlider = new Slider(0.0f, 1.0f, 0.05f, false, skin);

        musicSlider.setValue(0.5F);
        musicSlider.setWidth(400);
        musicSlider.setPosition(150, 700);

        soundSlider.setWidth(400);
        soundSlider.setPosition(150, 600);

        musicSlider.addListener( new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicVolume = musicSlider.getValue();
                musicLabel.setText("Music    " + musicVolume);
            }
        });
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundVolume = soundSlider.getValue();
            }
        });

        musicLabel.setPosition(150, 800);


        stage.addActor(musicSlider);
        stage.addActor(soundSlider);
        stage.addActor(musicLabel);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        stage.act();
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();

        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
