package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;



/**
 * Created by johan on 6.4.2018.
 */

public class HangarScreen implements Screen, InputProcessor {
    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;

    final TDShooterGdxGame game;

    private OrthographicCamera camera;
    private TextureAtlas atlas;
    private StretchViewport viewport;
    private Skin skin;
    private Stage stage;
    private Image menuImage;
    private Image scientistImage;
    private Texture menuBackground;
    private Texture scientistTexture;


    public HangarScreen(final  TDShooterGdxGame gam){
        game = gam;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        atlas = (TextureAtlas) game.assets.get("Skin/glassy-ui.atlas");
        skin = game.assets.get("Skin/glassy-ui.json");
        menuBackground = game.assets.get("Menu/Background_BaseMenu_720_1280.png");
        scientistTexture = game.assets.get("Menu/Character_Scientist.png");

        menuImage = new Image(menuBackground);
        scientistImage = new Image(scientistTexture);


        stage = new Stage(viewport, game.batch);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void show() {

        // Luodaan painikkeet
        TextButton bridgeButton = new TextButton("Bridge", skin);
        TextButton hangarButton = new TextButton("Hangar", skin);
        TextButton shopButton = new TextButton("Shop", skin);

        bridgeButton.setWidth(240);
        hangarButton.setWidth(240);
        shopButton.setWidth(240);

        bridgeButton.setPosition(0, VIEWPORTHEIGHT - bridgeButton.getHeight());
        shopButton.setPosition(VIEWPORTWIDTH / 2, VIEWPORTHEIGHT - shopButton.getHeight());
        hangarButton.setPosition(bridgeButton.getWidth(), VIEWPORTHEIGHT - hangarButton.getHeight());
        shopButton.setPosition(bridgeButton.getWidth() + hangarButton.getWidth(), VIEWPORTHEIGHT - hangarButton.getHeight());

        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ShopScreen(game));
            }
        });
        bridgeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MissionsMenu(game));
            }
        });

        scientistImage.setScale(0.7f);
        scientistImage.setPosition(-50, 100);

        stage.addActor(menuImage);
        stage.addActor(scientistImage);
        stage.addActor(bridgeButton);
        stage.addActor(shopButton);
        stage.addActor(hangarButton);
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

    }

    @Override
    public boolean keyDown(int keycode) {
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
