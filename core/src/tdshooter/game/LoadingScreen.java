package tdshooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class LoadingScreen implements Screen {

    final TDShooterGdxGame game;
    private Texture loading;
    private Image loadingImage;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Stage stage;

    public LoadingScreen(final TDShooterGdxGame gam) {
        this.game = gam;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(game.VIEWPORTWIDTH, game.VIEWPORTHEIGHT, camera);
        viewport.apply();

        stage = new Stage(viewport, game.batch);
        loading = new Texture(Gdx.files.internal("Menu/Swixel_Software_720_1280.png"));
        loadingImage = new Image(loading);
        loadingImage.setPosition(game.VIEWPORTWIDTH / 2 - loadingImage.getWidth() / 2, game.VIEWPORTHEIGHT / 2 - loadingImage.getHeight() / 2);

        stage.addActor(loadingImage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (game.assets.update()) {
            game.setScreen(new MainMenuScreen(game));
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

    }
}
