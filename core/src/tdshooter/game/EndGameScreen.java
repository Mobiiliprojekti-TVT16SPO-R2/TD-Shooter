package tdshooter.game;

/**
 * Created by leevi on 16.3.2018.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class EndGameScreen implements Screen, InputProcessor {
    final TDShooterGdxGame game;
    OrthographicCamera camera;

    private int player_score = 0;
    private int high_score = 0;
    private boolean new_highscore = false;

    public EndGameScreen(final TDShooterGdxGame gam, int score) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        high_score = game.prefs.getInteger("highscore", 0);
        player_score = score;

        if (player_score > high_score){
            new_highscore = true;
            game.prefs.putInteger("highscore", player_score);
            game.prefs.flush();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "GAME OVER", 50, 700);
        if (new_highscore) {
            game.font.draw(game.batch, "You got a new highscore!", 70, 500);
            game.font.draw(game.batch, "Old highscore: " + high_score, 50, 400);
        } else {
            game.font.draw(game.batch, "Highscore: " + high_score, 50, 400);
        }
        game.font.draw(game.batch, "Your Score: " + player_score, 50, 300);
        game.font.draw(game.batch, "(Tap anywhere to go to menu)", 50, 100);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
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
            game.setScreen(new MainMenuScreen(game));
            dispose();
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