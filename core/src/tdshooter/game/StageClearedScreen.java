package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StageClearedScreen implements Screen, InputProcessor {

    private final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    private Skin skin;

    private final int VIEWPORTWIDTH = 720;
    private final int VIEWPORTHEIGHT = 1280;

    private int earnedCurrency;
    private int yourScore;
    private int totalCurrency;
    private int highscore;
    private boolean isNewHighscore;

    public StageClearedScreen(final TDShooterGdxGame game, int earnedCurrency, int yourScore, String missionName, boolean isNewHighscore)
    {
        this.game = game;
        this.earnedCurrency = earnedCurrency;
        this.totalCurrency = 0;
        this.isNewHighscore = isNewHighscore;
        this.yourScore = yourScore;

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
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        skin = new Skin();
        skin.add("font", game.fontSkin);
        skin.addRegions((TextureAtlas) game.assets.get("Skin/glassy-ui.atlas"));
        skin.load(Gdx.files.internal("Skin/glassy-ui.json"));

        stage = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act();
        stage.draw();
    }

    @Override
    public void show()
    {
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.top();

        Label stageClearedLabel = new Label("Stage Cleared!", skin);
        stageClearedLabel.setFontScale(2.0f);
        Label currencyEarnedLabel = new Label("Currency Earned: " + earnedCurrency, skin);
        Label highscoreLabel = new Label("", skin);
        Label yourScoreLabel = new Label("Your score: " + yourScore, skin);
        if(isNewHighscore)
        {
            highscoreLabel.setText("New Highscore!  " + highscore);
        }
        else {
            highscoreLabel.setText("Highscore:  " + highscore);
        }
        Label touchToContinueLabel = new Label("Touch to Continue.", skin);
        touchToContinueLabel.setFontScale(1.5f);

        table.add(stageClearedLabel).left().spaceBottom(10.0f).padTop(300.0f);
        table.row();
        table.add(currencyEarnedLabel).left();
        table.row();


        table.add(highscoreLabel).left();
        table.row();
        table.add(yourScoreLabel).left();
        table.row();

        table.add(touchToContinueLabel).center().spaceTop(300.0f);

        stage.addActor(table);
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
    }
    @Override
    public boolean keyDown(int keycode) { return false; }

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
        game.setScreen(new MissionsMenu(game));
        dispose();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

