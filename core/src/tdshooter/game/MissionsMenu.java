package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;


import java.util.ArrayList;

/**
 * Created by johan on 26.3.2018.
 */

public class MissionsMenu implements Screen, InputProcessor {

    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    final TDShooterGdxGame game;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Skin skin;
    private Stage stage;
    private TextureAtlas atlas;
    private Texture menuBackground;
    private Texture commanderTexture;
    private Texture topbarTexture;
    private Image commanderImage;
    private Image menuImage;
    private Image topbarImage;
    private ArrayList<String> missionNameList;
    private ButtonGroup missionsButtonGroup;
    private int levelProgress;

    public MissionsMenu(final  TDShooterGdxGame gam){
        game = gam;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        skin = new Skin();
        skin.add("font", game.fontSkin);
        skin.addRegions((TextureAtlas) game.assets.get("Skin/glassy-ui.atlas"));
        skin.load(Gdx.files.internal("Skin/glassy-ui.json"));

        Preferences prefs = Gdx.app.getPreferences("savedata");
        if(prefs.contains("levelprogress")) {
            levelProgress = prefs.getInteger("levelprogress", 1);
        }

        menuBackground = game.assets.get("Menu/Background_BaseMenu_720_1280.png");
        commanderTexture = game.assets.get("Menu/Character_Commander.png");
        topbarTexture = game.assets.get("Menu/valikko-ylapalkki.png");

        menuImage = new Image(menuBackground);
        commanderImage = new Image(commanderTexture);
        topbarImage = new Image(topbarTexture);

        missionsButtonGroup = new ButtonGroup();
        missionNameList = new ArrayList<String>();
        setMissionNameList();

        stage = new Stage(viewport, game.batch);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);

    }
    @Override
    public void show() {

        int TOPBARHEIGHT = 30;

        // Luodaan painikkeet
        TextButton launchButton = new TextButton("Launch", skin);
        TextButton bridgeButton = new TextButton("Bridge", skin);
        TextButton hangarButton = new TextButton("Hangar", skin);
        TextButton shopButton = new TextButton("Shop", skin);

        TextButton mission01 = new TextButton("", skin, "toggle");
        TextButton mission02 = new TextButton("", skin, "toggle");
        TextButton mission03 = new TextButton("", skin, "toggle");

        Label currencyLabel = new Label("Currency: 0", skin);
        currencyLabel.setPosition(VIEWPORTWIDTH - 200, VIEWPORTHEIGHT - TOPBARHEIGHT);

        Gdx.app.log("DEBUG", "Topbarimageheight: " + topbarImage.getHeight());
        topbarImage.setWidth(VIEWPORTWIDTH);
        topbarImage.setPosition(0, VIEWPORTHEIGHT - topbarImage.getHeight());  //VIEWPORTHEIGHT - topbarImage.getImageY()


        bridgeButton.setWidth(240);
        hangarButton.setWidth(240);
        shopButton.setWidth(240);

        // Asetetaan mission-painikkeiden kooot
        mission01.setWidth(60);
        mission01.setHeight(60);

        mission02.setWidth(60);
        mission02.setHeight(60);

        mission03.setWidth(60);
        mission03.setHeight(60);

        // Asetetaan luotujen painikkeiden paikat
        launchButton.setPosition(VIEWPORTWIDTH - launchButton.getWidth(), 160);
        bridgeButton.setPosition(0, VIEWPORTHEIGHT - bridgeButton.getHeight() - TOPBARHEIGHT);
        hangarButton.setPosition(bridgeButton.getWidth(), VIEWPORTHEIGHT - hangarButton.getHeight() - TOPBARHEIGHT);
        shopButton.setPosition(bridgeButton.getWidth() + hangarButton.getWidth(), VIEWPORTHEIGHT - hangarButton.getHeight() - TOPBARHEIGHT);

        mission01.setPosition((VIEWPORTWIDTH / 2) / 2 - (mission01.getWidth() / 2),750);
        mission02.setPosition(VIEWPORTWIDTH / 2 - (mission01.getWidth() / 2),750);
        mission03.setPosition(VIEWPORTWIDTH - ((VIEWPORTWIDTH / 2) / 2) - (mission01.getWidth() / 2),750);


        // Lisätään mission-painikkeet MissionsButtonGroup-ryhmään
        missionsButtonGroup.add(mission01);
        missionsButtonGroup.add(mission02);
        missionsButtonGroup.add(mission03);

        switch (levelProgress){
            case 1:
                mission02.setDisabled(true);
                mission03.setDisabled(true);
                break;
            case 2:
                mission03.setDisabled(true);
                break;
            case 3:
                break;
            default:
                break;
        }

        // Asetetaan yhtäaikaa valittujen painikkeiden enimmäis- ja minimimäärät
        // Ja jos enimmäismäärä ylittyy poistetaan edellinen valinta
        missionsButtonGroup.setMaxCheckCount(1);
        missionsButtonGroup.setMinCheckCount(1);
        missionsButtonGroup.setUncheckLast(true);

        // Painikketta painettaessa haetaan valitun mission painikkeen indexi
        // ja vaihdetaan näkymää
        launchButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int index = missionsButtonGroup.getCheckedIndex();
                game.setScreen(new GameScreen(game, missionNameList.get(index), index+1));
                dispose();

            }
        });
        bridgeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        hangarButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HangarScreen(game));
            }
        });


        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ShopScreen(game));
            }
        });

        commanderImage.setScale(0.6f);
        commanderImage.setPosition(-100, 100);

        stage.addActor(currencyLabel);
        stage.addActor(menuImage);
        stage.addActor(topbarImage);
        stage.addActor(commanderImage);
        stage.addActor(launchButton);
        stage.addActor(bridgeButton);
        stage.addActor(shopButton);
        stage.addActor(hangarButton);
        stage.addActor(mission01);
        stage.addActor(mission02);
        stage.addActor(mission03);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();


    }

    public void setMissionNameList(){
        missionNameList.add("Missions/mission01.txt");
        missionNameList.add("Missions/mission02.txt");
        missionNameList.add("Missions/mission03.txt");
    }
    @Override
    public void resize(int width, int height) {
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
//        skin.dispose();
        stage.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
            return true;
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
