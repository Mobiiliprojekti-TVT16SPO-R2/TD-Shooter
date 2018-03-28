package tdshooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class StageClearedScreen implements Screen {

    private final TDShooterGdxGame game;
    private OrthographicCamera camera;

    private int healthLeft;
    private int score;
    private float runningTime;
    private float waitTime;

    public StageClearedScreen(final TDShooterGdxGame game, int score, int healthLeft)
    {
        this.game = game;
        this.score = score;
        this.healthLeft = healthLeft;
        runningTime = 0;
        waitTime = 2.0f;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
    }

    @Override
    public void render(float delta)
    {
        runningTime += delta;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Stage Cleared!", 100, 600);
        game.font.draw(game.batch, "Enemies Destroyed: " + score, 100, 580);
        game.font.draw(game.batch, "Player Health Left: " + healthLeft, 100, 560);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            if(runningTime > waitTime)
            {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
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

