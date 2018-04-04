package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by johan on 26.3.2018.
 */

public class MissionsMenu implements Screen {

    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Skin skin;
    private Stage stage;
    private TextureAtlas atlas;

    public MissionsMenu(final  TDShooterGdxGame gam){
        game = gam;

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        atlas = game.assets.get("Skin/glassy-ui.atlas");
        skin = game.assets.get("Skin/glassy-ui.json");

        stage = new Stage(viewport, game.batch);

        Gdx.input.setCatchBackKey(true);
    }
    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

        TextButton launchButton = new TextButton("Launch", skin);
        TextButton bridgeButton = new TextButton("Bridge", skin);
        TextButton hangarButton = new TextButton("Hangar", skin);
        TextButton shopButton = new TextButton("Shop", skin);
        launchButton.setPosition(VIEWPORTWIDTH / 2 - (launchButton.getWidth() / 2), VIEWPORTHEIGHT / 2 - (launchButton.getWidth() / 2));
        bridgeButton.setPosition(0, VIEWPORTHEIGHT - bridgeButton.getHeight());
        shopButton.setPosition(VIEWPORTWIDTH / 2, VIEWPORTHEIGHT - shopButton.getHeight());

        launchButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ShopScreen(game));
            }
        });

        stage.addActor(launchButton);
        stage.addActor(bridgeButton);
        stage.addActor(shopButton);
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
