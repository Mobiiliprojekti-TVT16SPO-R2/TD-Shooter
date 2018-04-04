package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ShopScreen implements Screen
{
    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    private Viewport viewport;
    private Skin skin;
    private Stage stage;

    public ShopScreen(final TDShooterGdxGame game)
    {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        atlas = (TextureAtlas)game.assets.get("Skin/glassy-ui.atlas");
        skin = (Skin)game.assets.get("Skin/glassy-ui.json");

        stage = new Stage(viewport, game.batch);

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

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.setScreen(new MissionsMenu(game));
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
