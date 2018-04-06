package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private Image commanderImage;
    private Image menuImage;
    private ArrayList<String> missionNameList;
    private ButtonGroup missionsButtonGroup;

    public MissionsMenu(final  TDShooterGdxGame gam){
        game = gam;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        atlas = (TextureAtlas) game.assets.get("Skin/glassy-ui.atlas");
        skin = game.assets.get("Skin/glassy-ui.json");
        menuBackground = game.assets.get("Menu/Background_BaseMenu_720_1280.png");
        commanderTexture = game.assets.get("Menu/Character_Commander.png");

        menuImage = new Image(menuBackground);
        commanderImage = new Image(commanderTexture);

//        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
//        camera.update();


        missionsButtonGroup = new ButtonGroup();
        missionNameList = new ArrayList<String>();
        setMissionNameList();

        stage = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

    }
    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

        // Luodaan painikkeet
        TextButton launchButton = new TextButton("Launch", skin);
        TextButton bridgeButton = new TextButton("Bridge", skin);
        TextButton hangarButton = new TextButton("Hangar", skin);
        TextButton shopButton = new TextButton("Shop", skin);

        TextButton mission01 = new TextButton("", skin, "toggle");
        TextButton mission02 = new TextButton("", skin, "toggle");
        TextButton mission03 = new TextButton("", skin, "toggle");


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
        launchButton.setPosition(VIEWPORTWIDTH / 2 - (launchButton.getWidth() / 2), VIEWPORTHEIGHT / 2 - (launchButton.getWidth() / 2));
        bridgeButton.setPosition(0, VIEWPORTHEIGHT - bridgeButton.getHeight());
        shopButton.setPosition(VIEWPORTWIDTH / 2, VIEWPORTHEIGHT - shopButton.getHeight());
        hangarButton.setPosition(bridgeButton.getWidth(), VIEWPORTHEIGHT - hangarButton.getHeight());
        shopButton.setPosition(bridgeButton.getWidth() + hangarButton.getWidth(), VIEWPORTHEIGHT - hangarButton.getHeight());

        mission01.setPosition((VIEWPORTWIDTH / 2) / 2 - (mission01.getWidth() / 2),750);
        mission02.setPosition(VIEWPORTWIDTH / 2 - (mission01.getWidth() / 2),750);
        mission03.setPosition(VIEWPORTWIDTH - ((VIEWPORTWIDTH / 2) / 2) - (mission01.getWidth() / 2),750);


        // Lisätään mission-painikkeet MissionsButtonGroup-ryhmään
        missionsButtonGroup.add(mission01);
        missionsButtonGroup.add(mission02);
        missionsButtonGroup.add(mission03);

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
                game.setScreen(new GameScreen(game, missionNameList.get(index)));
                dispose();

            }
        });
        bridgeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });


        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ShopScreen(game));
            }
        });

        commanderImage.setScale(0.6f);
        commanderImage.setPosition(-30, 150);

        stage.addActor(menuImage);
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

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act();
        stage.draw();


    }

    public void setMissionNameList(){
        missionNameList.add("Missions/mission01.txt");
        missionNameList.add("Missions/mission02.txt");
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
        stage.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            game.setScreen(new MainMenuScreen(game));
            dispose();

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
