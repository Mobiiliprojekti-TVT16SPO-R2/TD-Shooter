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
    private MenuTopBar menuTopBar;
    private ShopItemSlot armorShopitem;
    private ShopItemSlot weapon1Shopitem;
    private ShopItemSlot weapon2Shopitem;
    private ShopItemSlot weapon3Shopitem;

    private int currentCurrency;
    private int weapon01Level;
    private int weapon02Level;
    private int weapon03Level;
    private final int maxWeaponLevel = 3;

    public ShopScreen(final TDShooterGdxGame game)
    {
        this.game = game;
        prefs = Gdx.app.getPreferences("savedata");
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        skin = game.skin;

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

        if(prefs.contains("weapon02")) {
            weapon02Level = prefs.getInteger("weapon02");
        } else {
            weapon02Level = 0;
        }

        if(prefs.contains("weapon03")) {
            weapon03Level = prefs.getInteger("weapon03");
        } else {
            weapon03Level = 0;
        }

        armorShopitem = new ShopItemSlot(viewport, skin, game, 1, 0);
        weapon1Shopitem = new ShopItemSlot(viewport, skin, game, 2, weapon01Level);
        weapon2Shopitem = new ShopItemSlot(viewport, skin, game, 3, weapon02Level);
        weapon3Shopitem = new ShopItemSlot(viewport, skin, game, 4, weapon03Level);

        armorShopitem.setPosition(230,850);
        weapon1Shopitem.setPosition(230,600);
        weapon2Shopitem.setPosition(230,350);
        weapon3Shopitem.setPosition(230,100);

        menuTopBar = new MenuTopBar(viewport, skin, game, 3);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(armorShopitem);
        multiplexer.addProcessor(weapon1Shopitem);
        multiplexer.addProcessor(weapon2Shopitem);
        multiplexer.addProcessor(weapon3Shopitem);
        multiplexer.addProcessor(menuTopBar);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void show() {
        quarterImage.setScale(0.7f);
        quarterImage.setPosition(0, 150);

        stage.addActor(menuImage);
        stage.addActor(quarterImage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        menuTopBar.update(delta);
        menuTopBar.draw();

        armorShopitem.update(delta);
        weapon1Shopitem.update(delta);
        weapon2Shopitem.update(delta);
        weapon3Shopitem.update(delta);
        armorShopitem.draw();
        weapon1Shopitem.draw();
        weapon2Shopitem.draw();
        weapon3Shopitem.draw();


        if (menuTopBar.isReadyForNextScreen()){
            menuTopBar.setNextScreen();
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
        menuTopBar.dispose();
        armorShopitem.dispose();
        weapon1Shopitem.dispose();
        weapon2Shopitem.dispose();
        weapon3Shopitem.dispose();
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
