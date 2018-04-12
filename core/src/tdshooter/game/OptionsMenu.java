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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by johan on 5.4.2018.
 */

public class OptionsMenu implements Screen, InputProcessor {

    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Skin skin;
    private Stage stage;
    private float soundVolume = 0.5f;
    private float musicVolume = 0.5f;
    private boolean soundMuted = false;
    private boolean musicMuted = false;
    private Label musicLabel;
    private Label soundLabel;
    private Slider musicSlider;
    private Slider soundSlider;
    private TextButton soundButton;
    private TextButton musicButton;
    private Preferences options;

    public OptionsMenu(final TDShooterGdxGame gam){
        game = gam;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        skin = new Skin();
        skin.add("font", game.fontSkin);
        skin.addRegions((TextureAtlas) game.assets.get("Skin/glassy-ui.atlas"));
        skin.load(Gdx.files.internal("Skin/glassy-ui.json"));

        options = Gdx.app.getPreferences("options");

        if(options.contains("soundvolume")) {
            soundVolume = options.getFloat("soundvolume");
        }
        if(options.contains("musicvolume")) {
            musicVolume = options.getFloat("musicvolume");
        }
        if(options.contains("soundmuted")) {
            soundMuted = options.getBoolean("soundmuted");
            if (soundMuted) {
                soundVolume = 0.0f;
            }
        }
        if(options.contains("musicmuted")) {
            musicMuted = options.getBoolean("musicmuted");
            if (musicMuted) {
                musicVolume = 0.0f;
            }
        }
        musicLabel = new Label("Music volume", skin);
        //musicLabel.setScale(1,1);
        musicLabel.setPosition(150, 550);

        soundLabel = new Label("Soundeffects volume", skin);
        //soundLabel.setScale(1,1);
        soundLabel.setPosition(150, 650);

        stage = new Stage(viewport, game.batch);

        soundButton = new TextButton("", skin, "small");
        soundButton.setWidth(50);
        soundButton.setPosition(150,580);

        musicButton = new TextButton("", skin, "small");
        musicButton.setWidth(50);
        musicButton.setPosition(150,380);

        soundSlider = new Slider(0.0f, 1.0f, 0.01f, false, skin);
        soundSlider.setValue(soundVolume);
        soundSlider.setWidth(400);
        soundSlider.setPosition(220, 600);

        musicSlider = new Slider(0.0f, 1.0f,0.01f, false, skin);
        musicSlider.setValue(musicVolume);
        musicSlider.setWidth(400);
        musicSlider.setPosition(220, 400);

        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (musicMuted) {
                    if(options.contains("musicvolume")) {
                        musicVolume = options.getFloat("musicvolume");
                    }
                    musicMuted = false;
                    options.putBoolean("musicmuted", musicMuted);
                    options.flush();
                }
                else {
                    musicVolume = 0.0f;
                    musicMuted = true;
                    options.putBoolean("musicmuted", musicMuted);
                    options.flush();
                }
            }
        });
        soundButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (soundMuted) {
                    if(options.contains("soundvolume")) {
                        soundVolume = options.getFloat("soundvolume");
                    }
                    soundMuted = false;
                    options.putBoolean("soundmuted", soundMuted);
                    options.flush();
                }
                else {
                    soundVolume = 0.0f;
                    soundMuted = true;
                    options.putBoolean("soundmuted", soundMuted);
                    options.flush();
                }
            }
        });

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundVolume = soundSlider.getValue();
                options.putFloat("soundvolume", soundVolume);
                options.flush();
                if (soundVolume == 0.0f) {
                    soundMuted = true;
                } else {
                    soundMuted = false;
                }
                options.putBoolean("soundmuted", soundMuted);
                options.flush();
            }
        });
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicVolume = musicSlider.getValue();
                options.putFloat("musicvolume", musicVolume);
                options.flush();
                if (musicVolume == 0.0f) {
                    musicMuted = true;
                } else {
                    musicMuted = false;
                }
                options.putBoolean("musicmuted", musicMuted);
                options.flush();
            }
        });

        stage.addActor(soundLabel);
        stage.addActor(musicLabel);
        stage.addActor(soundButton);
        stage.addActor(musicButton);
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

//        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
//            game.setScreen(new MainMenuScreen(game));
//            dispose();
//        }
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
