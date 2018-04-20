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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Texture menuBackground;
    private Texture commanderTexture;
    private Texture missionMapTexture;
    private Texture mapWaypoint1Texture;
    private Texture mapWaypoint4Texture;
    private Texture mapWaypoint5Texture;
    private Texture mapWaypoint6Texture;
    private Texture mapWaypoint7Texture;
    private Texture mapWaypoint8Texture;
    private Image mapWaypoint1Image;
    private Image mapWaypoint4Image;
    private Image mapWaypoint5Image;
    private Image mapWaypoint6Image;
    private Image mapWaypoint7Image;
    private Image mapWaypoint8Image;
    private Image commanderImage;
    private Image menuImage;
    private Image missionMapImage;
    private ArrayList<String> missionNameList;
    private ButtonGroup missionsButtonGroup;
    private PopupDialog dialog;
    private MenuTopBar menuTopBar;

    private MissionSelectionButton mission01;
    private TextButton mission01TextButton;
    private TextButton mission02TextButton;
    private TextButton mission03TextButton;
    private TextButton mission04TextButton;
    private TextButton mission05TextButton;
    private TextButton mission06TextButton;
    private TextButton mission07TextButton;
    private TextButton mission08TextButton;

    private int levelProgress;
    
    private int missionbuttonDiameter = 80;

    public MissionsMenu(final  TDShooterGdxGame gam){
        game = gam;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();

        skin = gam.skin;
        BitmapFont font = skin.getFont("font");

        Preferences prefs = Gdx.app.getPreferences("savedata");
        if(prefs.contains("levelprogress")) {
            levelProgress = prefs.getInteger("levelprogress", 1);
        }

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.overFontColor = Color.YELLOW;
        textButtonStyle.downFontColor = Color.YELLOW;
        textButtonStyle.fontColor = Color.WHITE;
        
        menuBackground = game.assets.get("Menu/Background_BaseMenu_720_1280.png");
        commanderTexture = game.assets.get("Menu/Character_Commander.png");
        missionMapTexture = game.assets.get("Menu/Bridge_Screen_Map_with_lines.png");
        mapWaypoint1Texture = game.assets.get("mapitems/reitti-1.png");
        mapWaypoint4Texture = game.assets.get("mapitems/reitti-4.png");
        mapWaypoint5Texture = game.assets.get("mapitems/reitti-5.png");
        mapWaypoint6Texture = game.assets.get("mapitems/reitti-6.png");
        mapWaypoint7Texture = game.assets.get("mapitems/reitti-7.png");
        mapWaypoint8Texture = game.assets.get("mapitems/reitti-8.png");

        menuImage = new Image(menuBackground);
        commanderImage = new Image(commanderTexture);
        missionMapImage = new Image(missionMapTexture);
        mapWaypoint1Image = new Image(mapWaypoint1Texture);
        mapWaypoint4Image = new Image(mapWaypoint4Texture);
        mapWaypoint5Image = new Image(mapWaypoint5Texture);
        mapWaypoint6Image = new Image(mapWaypoint6Texture);
        mapWaypoint7Image = new Image(mapWaypoint7Texture);
        mapWaypoint8Image = new Image(mapWaypoint8Texture);

        mission01 = new MissionSelectionButton(viewport, game);

        // Luodaan painikkeet
        TextButton launchButton = new TextButton("Launch", textButtonStyle);
        TextButton mission01 = new TextButton("", textButtonStyle);
        TextButton mission02 = new TextButton("", textButtonStyle);
        TextButton mission03 = new TextButton("", textButtonStyle);
        TextButton mission04 = new TextButton("", textButtonStyle);
        TextButton mission05 = new TextButton("", textButtonStyle);
        TextButton mission06 = new TextButton("", textButtonStyle);
        TextButton mission07 = new TextButton("", textButtonStyle);
        TextButton mission08 = new TextButton("", textButtonStyle);

        // Asetetaan mission-painikkeiden kooot
        mission01.setWidth(missionbuttonDiameter);
        mission01.setHeight(missionbuttonDiameter);
        mission02.setWidth(missionbuttonDiameter);
        mission02.setHeight(missionbuttonDiameter);
        mission03.setWidth(missionbuttonDiameter);
        mission03.setHeight(missionbuttonDiameter);
        mission04.setWidth(missionbuttonDiameter);
        mission04.setHeight(missionbuttonDiameter);
        mission05.setWidth(missionbuttonDiameter);
        mission05.setHeight(missionbuttonDiameter);
        mission06.setWidth(missionbuttonDiameter);
        mission06.setHeight(missionbuttonDiameter);
        mission07.setWidth(missionbuttonDiameter);
        mission07.setHeight(missionbuttonDiameter);
        mission08.setWidth(missionbuttonDiameter);
        mission08.setHeight(missionbuttonDiameter);

        // Asetetaan luotujen painikkeiden paikat
        launchButton.setPosition(VIEWPORTWIDTH - launchButton.getWidth() + 40, 600);

        missionsButtonGroup = new ButtonGroup();
        missionNameList = new ArrayList<String>();
        setMissionNameList();

        for (int i = 0; i < missionNameList.size(); i++) {
            String highscoreKey = "highscore" + missionNameList.get(i);
            if (!prefs.contains(highscoreKey)) {
                prefs.putInteger(highscoreKey, 0);
            }
        }
        prefs.flush();

        stage = new Stage(viewport, game.batch);

        menuTopBar = new MenuTopBar(viewport, skin, game, 1);
        dialog = new PopupDialog(viewport, game.batch, skin, game.assets);
        InputMultiplexer multiplexer = new InputMultiplexer();
        //multiplexer.addProcessor(dialog);  // for enabling popup message tap-checkouts
        multiplexer.addProcessor(menuTopBar);  // enabling menuTopBar buttons
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);

    }
    @Override
    public void show() {

        dialog.setSize(VIEWPORTWIDTH, 360);
        
        // Lisätään mission-painikkeet MissionsButtonGroup-ryhmään
        missionsButtonGroup.add(mission01);
        missionsButtonGroup.add(mission02);
        missionsButtonGroup.add(mission03);

        switch (levelProgress){
            case 1:
                
                break;
            case 2:
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

        mission01.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.setText("Aliens! I knew this would happen some day. Go get them private!");
            }
        });

        mission02.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.setText("Aliens are trying to take over our farmlands! We will not tolerate this! You know what to do. ");
            }
        });

        mission03.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.setText("We managed to push them back to the ocean islands, but we are detecting some heavy organisms. Be careful out there pilot!");
            }
        });

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

        missionMapImage.setHeight(920);
        missionMapImage.setWidth(410);
        missionMapImage.setPosition(200, 160);

        mission01.setPosition(350,320);
        mission02.setPosition(355, 400);
        mission03.setPosition(460, 390);

        commanderImage.setScale(0.7f);
        commanderImage.setPosition(-100, 100);

        stage.addActor(menuImage);
        stage.addActor(missionMapImage);
        stage.addActor(commanderImage);
        stage.addActor(launchButton);
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

        menuTopBar.update(delta);
        menuTopBar.draw();

        if (menuTopBar.isReadyForNextScreen()){
            menuTopBar.setNextScreen();
            dispose();
        }

        dialog.update(delta);
        dialog.draw();

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
        stage.dispose();
        dialog.dispose();
        menuTopBar.dispose();
        Gdx.app.log("DEBUG", "missionsmenu dispose()-executed");
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
