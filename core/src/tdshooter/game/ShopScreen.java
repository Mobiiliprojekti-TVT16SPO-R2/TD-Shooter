package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

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
    private Preferences prefs;

    private int currentCurrency;
    private int weapon01Level;
    private final int maxWeapon01Level = 3;

    public ShopScreen(final TDShooterGdxGame game)
    {
        this.game = game;
        prefs = Gdx.app.getPreferences("savedata");
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        atlas = (TextureAtlas)game.assets.get("Skin/glassy-ui.atlas");
        skin = (Skin)game.assets.get("Skin/glassy-ui.json");

        stage = new Stage(viewport, game.batch);

        if(prefs.contains("currency")) {
            currentCurrency = prefs.getInteger("currency");
        } else {
            currentCurrency = 0;
        }

        if(prefs.contains("weapon01")) {
            weapon01Level = prefs.getInteger("weapon01");
        } else {
            weapon01Level = 0;
        }

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

        final TextButton buyButton = new TextButton("Level: " + weapon01Level, skin);
        final Label currencyLabel = new Label("Currency: " + currentCurrency, new Label.LabelStyle(game.font, Color.WHITE));

        buyButton.setPosition((VIEWPORTWIDTH - buyButton.getWidth()) / 2, (VIEWPORTHEIGHT - buyButton.getHeight()) / 2);
        currencyLabel.setPosition(VIEWPORTWIDTH - currencyLabel.getWidth(), VIEWPORTHEIGHT - currencyLabel.getHeight());

        buyButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int cost = 20;
                if(currentCurrency - cost >= 0)
                {
                    if(weapon01Level < maxWeapon01Level)
                    {
                        currentCurrency -= cost;
                        weapon01Level++;
                        prefs.putInteger("currency", currentCurrency);
                        prefs.putInteger("weapon01", weapon01Level);
                        prefs.flush();

                        currencyLabel.setText("Currency: " + currentCurrency);
                        buyButton.setText("Level: " + weapon01Level);
                    }
                }
            }
        });

        stage.addActor(buyButton);
        stage.addActor(currencyLabel);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            dispose();
            game.setScreen(new MissionsMenu(game));
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
