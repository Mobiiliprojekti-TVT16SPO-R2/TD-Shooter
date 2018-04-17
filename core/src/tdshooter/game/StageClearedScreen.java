package tdshooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class StageClearedScreen implements Screen {

    private final TDShooterGdxGame game;
    private OrthographicCamera camera;

    private int healthLeft;
    private int earnedCurrency;
    private int totalCurrency;
    private int highscore;
    private float runningTime;
    private float waitTime;
    private boolean newHighscore;

    public StageClearedScreen(final TDShooterGdxGame game, int earnedCurrency, int healthLeft, String missionName, boolean newHighscore)
    {
        this.game = game;
        this.earnedCurrency = earnedCurrency;
        this.healthLeft = healthLeft;
        this.totalCurrency = 0;
        this.newHighscore = newHighscore;
        runningTime = 0;
        waitTime = 2.0f;

        Preferences prefs = Gdx.app.getPreferences("savedata");
        String currencyKey = "currency";
        if(prefs.contains(currencyKey))
        {
            totalCurrency = prefs.getInteger(currencyKey);
        }
        String highscoreKey = "highscore" + missionName;

        if (prefs.contains(highscoreKey))
        {
                highscore = prefs.getInteger(highscoreKey);
        }

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
        game.font.draw(game.batch, "Currency Earned: " + earnedCurrency, 100, 580);
        game.font.draw(game.batch, "Total Currency: " + totalCurrency, 100, 560);
        game.font.draw(game.batch, "Player Health Left: " + healthLeft, 100, 540);
        if (newHighscore){
            game.font.draw(game.batch, "New Highscore!!! " + highscore, 100, 620);
        }
        else {
            game.font.draw(game.batch, "Highscore: " + highscore, 100, 620);
        }
        game.batch.end();

        if (Gdx.input.isTouched()) {
            if(runningTime > waitTime)
            {
                game.setScreen(new MissionsMenu(game));
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

