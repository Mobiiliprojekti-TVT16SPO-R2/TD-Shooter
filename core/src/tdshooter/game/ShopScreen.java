package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

public class ShopScreen implements Screen, InputProcessor
{
    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    private StretchViewport viewport;
    private Skin skin;
    private Stage stage;
    private Preferences prefs;
    private Image menuImage;
    private Image quarterImage;
    private Texture menuBackground;
    private Texture quarterTexture;

    private int currentCurrency;
    private int weapon01Level;
    private final int maxWeapon01Level = 3;

    public ShopScreen(final TDShooterGdxGame game)
    {
        this.game = game;
        prefs = Gdx.app.getPreferences("savedata");
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

//        atlas = (TextureAtlas)game.assets.get("Skin/glassy-ui.atlas");
//        skin = (Skin)game.assets.get("Skin/glassy-ui.json");

        skin = new Skin();
        skin.add("font", game.fontSkin);
        skin.addRegions((TextureAtlas) game.assets.get("Skin/glassy-ui.atlas"));
        skin.load(Gdx.files.internal("Skin/glassy-ui.json"));


        menuBackground = game.assets.get("Menu/Background_BaseMenu_720_1280.png");
        quarterTexture = game.assets.get("Menu/Character_QuarterMaster.png");
        menuImage = new Image(menuBackground);
        quarterImage = new Image(quarterTexture);

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

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void show() {

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

        bridgeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MissionsMenu(game));
            }
        });
        hangarButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HangarScreen(game));
            }
        });

        quarterImage.setScale(0.7f);
        quarterImage.setPosition(0, 100);

        stage.addActor(menuImage);
        stage.addActor(quarterImage);
        stage.addActor(bridgeButton);
        stage.addActor(shopButton);
        stage.addActor(hangarButton);
        stage.addActor(buyButton);
        stage.addActor(currencyLabel);
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
