package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
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

public class OptionsMenu implements Screen, InputProcessor {

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
    private Preferences prefs;

    public OptionsMenu(final TDShooterGdxGame gam){
        game = gam;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();
        atlas = new TextureAtlas("Skin/glassy-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skin/glassy-ui.json"), atlas);

        prefs = Gdx.app.getPreferences("options");
        if(prefs.contains("soundvolume")) {
            soundVolume = prefs.getFloat("soundvolume");
        }
        if(prefs.contains("musicvolume")) {
            musicVolume = prefs.getFloat("musicvolume");
        }

        musicLabel = new Label("Music volume", skin);
        //musicLabel.setScale(1,1);
        musicLabel.setPosition(150, 550);

        soundLabel = new Label("Soundeffects volume", skin);
        //soundLabel.setScale(1,1);
        soundLabel.setPosition(150, 650);

        stage = new Stage(viewport, game.batch);

        soundSlider = new Slider(0.0f, 1.0f, 0.01f, false, skin);
        soundSlider.setValue(soundVolume);
        soundSlider.setWidth(400);
        soundSlider.setPosition(150, 600);

        musicSlider = new Slider(0.0f, 1.0f,0.01f, false, skin);
        musicSlider.setValue(musicVolume);
        musicSlider.setWidth(400);
        musicSlider.setPosition(150, 500);

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundVolume = soundSlider.getValue();
                prefs.putFloat("soundvolume", soundVolume);
                prefs.flush();
            }
        });
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicVolume = musicSlider.getValue();
                prefs.putFloat("musicvolume", musicVolume);
                prefs.flush();
            }
        });

        stage.addActor(soundLabel);
        stage.addActor(musicLabel);
        stage.addActor(soundSlider);
        stage.addActor(musicSlider);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {
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

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
