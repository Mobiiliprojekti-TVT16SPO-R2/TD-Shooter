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
    private Texture mapWaypoint9Texture;
    private Texture mapWaypoint10Texture;
    private Image mapWaypoint1Image;
    private Image mapWaypoint2Image;
    private Image mapWaypoint6Image;
    private Image mapWaypoint7Image;
    private Image mapWaypoint4Image;
    private Image mapWaypoint8Image;
    private Image mapWaypoint3Image;
    private Image mapWaypoint5Image;
    private Image commanderImage;
    private Image menuImage;
    private Image missionMapImage;
    private ArrayList<String> missionNameList;
    private ButtonGroup missionsButtonGroup;
    private PopupDialog dialog;
    private MenuTopBar menuTopBar;

    private MissionSelectionButton mission01;
    private MissionSelectionButton mission02;
    private MissionSelectionButton mission03;
    private MissionSelectionButton mission04;
    private MissionSelectionButton mission05;
    private MissionSelectionButton mission06;
    private MissionSelectionButton mission07;
    private MissionSelectionButton mission08;
    private TextButton mission01TextButton;
    private TextButton mission02TextButton;
    private TextButton mission03TextButton;
    private TextButton mission04TextButton;
    private TextButton mission05TextButton;
    private TextButton mission06TextButton;
    private TextButton mission07TextButton;
    private TextButton mission08TextButton;
    private TextButton launchButton;
    private Texture launchButtonTexture;
    private Image launchButtonImage;

    private int levelProgress;
    
    private int missionbuttonDiameter = 60;

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
        missionMapTexture = game.assets.get("Menu/Bridge_Screen_Map.png");
        mapWaypoint1Texture = game.assets.get("mapitems/reitti-1.png");
        mapWaypoint4Texture = game.assets.get("mapitems/reitti-4.png");
        mapWaypoint5Texture = game.assets.get("mapitems/reitti-5.png");
        mapWaypoint6Texture = game.assets.get("mapitems/reitti-6.png");
        mapWaypoint7Texture = game.assets.get("mapitems/reitti-7.png");
        mapWaypoint8Texture = game.assets.get("mapitems/reitti-8.png");
        mapWaypoint9Texture = game.assets.get("mapitems/reitti-9.png");
        mapWaypoint10Texture = game.assets.get("mapitems/reitti-10.png");
        launchButtonTexture = game.assets.get("Menu/startmenu-painike-pohjassa.png");

        menuImage = new Image(menuBackground);
        commanderImage = new Image(commanderTexture);
        missionMapImage = new Image(missionMapTexture);
        launchButtonImage = new Image(launchButtonTexture);
        mapWaypoint1Image = new Image(mapWaypoint1Texture);
        mapWaypoint2Image = new Image(mapWaypoint4Texture);
        mapWaypoint6Image = new Image(mapWaypoint5Texture);
        mapWaypoint7Image = new Image(mapWaypoint6Texture);
        mapWaypoint4Image = new Image(mapWaypoint7Texture);
        mapWaypoint8Image = new Image(mapWaypoint8Texture);
        mapWaypoint3Image = new Image(mapWaypoint9Texture);
        mapWaypoint5Image = new Image(mapWaypoint10Texture);

        mission01 = new MissionSelectionButton(viewport, game);
        mission02 = new MissionSelectionButton(viewport, game);
        mission03 = new MissionSelectionButton(viewport, game);
        mission04 = new MissionSelectionButton(viewport, game);
        mission05 = new MissionSelectionButton(viewport, game);
        mission06 = new MissionSelectionButton(viewport, game);
        mission07 = new MissionSelectionButton(viewport, game);
        mission08 = new MissionSelectionButton(viewport, game);

        // Luodaan painikkeet
        launchButton = new TextButton("Launch", textButtonStyle);
        mission01TextButton = new TextButton("", textButtonStyle);
        mission02TextButton = new TextButton("", textButtonStyle);
        mission03TextButton = new TextButton("", textButtonStyle);
        mission04TextButton = new TextButton("", textButtonStyle);
        mission05TextButton = new TextButton("", textButtonStyle);
        mission06TextButton = new TextButton("", textButtonStyle);
        mission07TextButton = new TextButton("", textButtonStyle);
        mission08TextButton = new TextButton("", textButtonStyle);

        // Asetetaan mission-painikkeiden kooot
        mission01TextButton.setWidth(missionbuttonDiameter);
        mission01TextButton.setHeight(missionbuttonDiameter);
        mission02TextButton.setWidth(missionbuttonDiameter);
        mission02TextButton.setHeight(missionbuttonDiameter);
        mission03TextButton.setWidth(missionbuttonDiameter);
        mission03TextButton.setHeight(missionbuttonDiameter);
        mission04TextButton.setWidth(missionbuttonDiameter);
        mission04TextButton.setHeight(missionbuttonDiameter);
        mission05TextButton.setWidth(missionbuttonDiameter);
        mission05TextButton.setHeight(missionbuttonDiameter);
        mission06TextButton.setWidth(missionbuttonDiameter);
        mission06TextButton.setHeight(missionbuttonDiameter);
        mission07TextButton.setWidth(missionbuttonDiameter);
        mission07TextButton.setHeight(missionbuttonDiameter);
        mission08TextButton.setWidth(missionbuttonDiameter);
        mission08TextButton.setHeight(missionbuttonDiameter);

        mission01.setSize(missionbuttonDiameter);
        mission02.setSize(missionbuttonDiameter);
        mission03.setSize(missionbuttonDiameter);
        mission04.setSize(missionbuttonDiameter);
        mission05.setSize(missionbuttonDiameter);
        mission06.setSize(missionbuttonDiameter);
        mission07.setSize(missionbuttonDiameter);
        mission08.setSize(missionbuttonDiameter);

        // Asetetaan luotujen painikkeiden paikat
        launchButton.setWidth(240);
        launchButton.setHeight(100);
        launchButton.setPosition(VIEWPORTWIDTH - launchButton.getWidth() + 40, 650);
        launchButtonImage.setWidth(240);
        launchButtonImage.setHeight(100);
        launchButtonImage.setPosition(launchButton.getX(), launchButton.getY() -5);

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
        dialog.setText("Attention Pilot! World is under attack by some pesky aliens! Our mission locations are shown in this map on the wall.");

        if (levelProgress > 8 ) {
            dialog.setText("Congratulations pilot! You managed to beat them all! You can still play some missions again whenever you will.");
        }

        // Asetetaan yhtäaikaa valittujen painikkeiden enimmäis- ja minimimäärät
        // Ja jos enimmäismäärä ylittyy poistetaan edellinen valinta
        missionsButtonGroup.setMaxCheckCount(1);
        missionsButtonGroup.setMinCheckCount(1);
        missionsButtonGroup.setUncheckLast(true);

        mission01TextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unlockByProgress();
                mission01.setSelected();
                dialog.setText("Aliens! Earth is under attack! This is your first mission. Good luck pilot!");
            }
        });

        mission02TextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelProgress > 1){
                    unlockByProgress();
                    mission02.setSelected();
                    dialog.setText("Aliens are trying to take over our food supplies! We will not tolerate this! You know what to do! ");
                }
            }
        });

        mission03TextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelProgress > 2){
                    unlockByProgress();
                    mission03.setSelected();
                    dialog.setText("We managed to push them back to the ocean islands, but we are detecting some heavy heatmap-activity. Be careful out there pilot!");
                }
            }
        });

        mission04TextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelProgress > 3){
                    unlockByProgress();
                    mission04.setSelected();
                    dialog.setText("Aliens are receiving some reinforcements form outer space, It seems they have bigger armament than we expected.");
                }
            }
        });
        mission05TextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelProgress > 4){
                    unlockByProgress();
                    mission05.setSelected();
                    dialog.setText("Aliens have some activity in lunar section, go investigate and then report back!");
                }
            }
        });
        mission06TextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelProgress > 5){
                    unlockByProgress();
                    mission06.setSelected();
                    dialog.setText("It seems aliens have constructed massive base on the dark side of the moon. We need to wipe them out!");
                }
            }
        });
        mission07TextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelProgress > 6){
                    unlockByProgress();
                    mission07.setSelected();
                    dialog.setText("We got them retreating, but they are still gathering forces near asteroidbelt behind moon.");
                }
            }
        });
        mission08TextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelProgress > 7){
                    unlockByProgress();
                    mission08.setSelected();
                    dialog.setText("We are detecting massive alien armada of battleships and cruisers! But we can survive this, if you take down their supreme leader!");
                }
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
        missionMapImage.setPosition(230, 160);

        mission01.setPosition(370,320);
        mission02.setPosition(510, 440);
        mission03.setPosition(320, 490);
        mission04.setPosition(510, 540);
        mission05.setPosition(430, 660);
        mission06.setPosition(390, 760);
        mission07.setPosition(280, 850);
        mission08.setPosition(440, 900);
        mission01TextButton.setPosition(370,320);
        mission02TextButton.setPosition(510, 440);
        mission03TextButton.setPosition(320, 490);
        mission04TextButton.setPosition(510, 540);
        mission05TextButton.setPosition(430, 660);
        mission06TextButton.setPosition(390, 760);
        mission07TextButton.setPosition(280, 850);
        mission08TextButton.setPosition(440, 900);
        missionsButtonGroup.add(mission01TextButton);
        missionsButtonGroup.add(mission02TextButton);
        missionsButtonGroup.add(mission03TextButton);
        missionsButtonGroup.add(mission04TextButton);
        missionsButtonGroup.add(mission05TextButton);
        missionsButtonGroup.add(mission06TextButton);
        missionsButtonGroup.add(mission07TextButton);
        missionsButtonGroup.add(mission08TextButton);

        mapWaypoint1Image.setPosition(390,350);
        mapWaypoint2Image.setPosition(340,470);
        mapWaypoint3Image.setPosition(340,520);
        mapWaypoint4Image.setPosition(440,560);
        mapWaypoint5Image.setPosition(420,680);
        mapWaypoint5Image.setScale(0.7f);
        mapWaypoint6Image.setPosition(315, 780);
        mapWaypoint6Image.setScale(0.8f);
        mapWaypoint7Image.setPosition(300, 875);
        mapWaypoint7Image.setScale(0.8f);

        commanderImage.setScale(0.7f);
        commanderImage.setPosition(-150, 100);

        stage.addActor(menuImage);
        stage.addActor(missionMapImage);
        stage.addActor(commanderImage);
        stage.addActor(launchButtonImage);
        stage.addActor(launchButton);

        // Lisätään mission-painikkeet MissionsButtonGroup-ryhmään
        unlockByProgress();

    }

    private void unlockByProgress() {
        switch (levelProgress){
            case 1:
                mission01.setUnlocked();
                mission02.setLocked();
                mission03.setHidden();
                mission04.setHidden();
                mission05.setHidden();
                mission06.setHidden();
                mission07.setHidden();
                mission08.setHidden();
                stage.addActor(mission01TextButton);
                break;
            case 2:
                mission01.setUnlocked();
                mission02.setUnlocked();
                mission03.setLocked();
                mission04.setHidden();
                mission05.setHidden();
                mission06.setHidden();
                mission07.setHidden();
                mission08.setHidden();
                stage.addActor(mapWaypoint1Image);
                stage.addActor(mission01TextButton);
                stage.addActor(mission02TextButton);

                break;
            case 3:
                mission01.setUnlocked();
                mission02.setUnlocked();
                mission03.setUnlocked();
                mission04.setLocked();
                mission05.setHidden();
                mission06.setHidden();
                mission07.setHidden();
                mission08.setHidden();
                stage.addActor(mapWaypoint1Image);
                stage.addActor(mapWaypoint2Image);
                stage.addActor(mission01TextButton);
                stage.addActor(mission02TextButton);
                stage.addActor(mission03TextButton);
                break;
            case 4:
                mission01.setUnlocked();
                mission02.setUnlocked();
                mission03.setUnlocked();
                mission04.setUnlocked();
                mission05.setLocked();
                mission06.setHidden();
                mission07.setHidden();
                mission08.setHidden();
                stage.addActor(mapWaypoint1Image);
                stage.addActor(mapWaypoint2Image);
                stage.addActor(mapWaypoint3Image);
                stage.addActor(mission01TextButton);
                stage.addActor(mission02TextButton);
                stage.addActor(mission03TextButton);
                stage.addActor(mission04TextButton);
                break;
            case 5:
                mission01.setUnlocked();
                mission02.setUnlocked();
                mission03.setUnlocked();
                mission04.setUnlocked();
                mission05.setUnlocked();
                mission06.setLocked();
                mission07.setHidden();
                mission08.setHidden();
                stage.addActor(mapWaypoint1Image);
                stage.addActor(mapWaypoint2Image);
                stage.addActor(mapWaypoint3Image);
                stage.addActor(mapWaypoint4Image);
                stage.addActor(mission01TextButton);
                stage.addActor(mission02TextButton);
                stage.addActor(mission03TextButton);
                stage.addActor(mission04TextButton);
                stage.addActor(mission05TextButton);
                break;
            case 6:
                mission01.setUnlocked();
                mission02.setUnlocked();
                mission03.setUnlocked();
                mission04.setUnlocked();
                mission05.setUnlocked();
                mission06.setUnlocked();
                mission07.setLocked();
                mission08.setHidden();
                stage.addActor(mapWaypoint1Image);
                stage.addActor(mapWaypoint2Image);
                stage.addActor(mapWaypoint3Image);
                stage.addActor(mapWaypoint4Image);
                stage.addActor(mapWaypoint5Image);
                stage.addActor(mission01TextButton);
                stage.addActor(mission02TextButton);
                stage.addActor(mission03TextButton);
                stage.addActor(mission04TextButton);
                stage.addActor(mission05TextButton);
                stage.addActor(mission06TextButton);
                break;
            case 7:
                mission01.setUnlocked();
                mission02.setUnlocked();
                mission03.setUnlocked();
                mission04.setUnlocked();
                mission05.setUnlocked();
                mission06.setUnlocked();
                mission07.setUnlocked();
                mission08.setLocked();
                stage.addActor(mapWaypoint1Image);
                stage.addActor(mapWaypoint2Image);
                stage.addActor(mapWaypoint3Image);
                stage.addActor(mapWaypoint4Image);
                stage.addActor(mapWaypoint5Image);
                stage.addActor(mapWaypoint6Image);
                stage.addActor(mission01TextButton);
                stage.addActor(mission02TextButton);
                stage.addActor(mission03TextButton);
                stage.addActor(mission04TextButton);
                stage.addActor(mission05TextButton);
                stage.addActor(mission06TextButton);
                stage.addActor(mission07TextButton);
                break;
            case 8:
                mission01.setUnlocked();
                mission02.setUnlocked();
                mission03.setUnlocked();
                mission04.setUnlocked();
                mission05.setUnlocked();
                mission06.setUnlocked();
                mission07.setUnlocked();
                mission08.setUnlocked();
                stage.addActor(mapWaypoint1Image);
                stage.addActor(mapWaypoint2Image);
                stage.addActor(mapWaypoint3Image);
                stage.addActor(mapWaypoint4Image);
                stage.addActor(mapWaypoint5Image);
                stage.addActor(mapWaypoint6Image);
                stage.addActor(mapWaypoint7Image);
                stage.addActor(mission01TextButton);
                stage.addActor(mission02TextButton);
                stage.addActor(mission03TextButton);
                stage.addActor(mission04TextButton);
                stage.addActor(mission05TextButton);
                stage.addActor(mission06TextButton);
                stage.addActor(mission07TextButton);
                stage.addActor(mission08TextButton);
                break;
            default:
                mission01.setUnlocked();
                mission02.setUnlocked();
                mission03.setUnlocked();
                mission04.setUnlocked();
                mission05.setUnlocked();
                mission06.setUnlocked();
                mission07.setUnlocked();
                mission08.setUnlocked();
                stage.addActor(mapWaypoint1Image);
                stage.addActor(mapWaypoint2Image);
                stage.addActor(mapWaypoint3Image);
                stage.addActor(mapWaypoint4Image);
                stage.addActor(mapWaypoint5Image);
                stage.addActor(mapWaypoint6Image);
                stage.addActor(mapWaypoint7Image);
                stage.addActor(mission01TextButton);
                stage.addActor(mission02TextButton);
                stage.addActor(mission03TextButton);
                stage.addActor(mission04TextButton);
                stage.addActor(mission05TextButton);
                stage.addActor(mission06TextButton);
                stage.addActor(mission07TextButton);
                stage.addActor(mission08TextButton);
                break;
        }
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

        mission01.update(delta);
        mission02.update(delta);
        mission03.update(delta);
        mission04.update(delta);
        mission05.update(delta);
        mission06.update(delta);
        mission07.update(delta);
        mission08.update(delta);
        mission01.draw();
        mission02.draw();
        mission03.draw();
        mission04.draw();
        mission05.draw();
        mission06.draw();
        mission07.draw();
        mission08.draw();

        dialog.update(delta);
        dialog.draw();

    }

    public void setMissionNameList(){
        missionNameList.add("Missions/mission01.txt");
        missionNameList.add("Missions/mission02.txt");
        missionNameList.add("Missions/mission03.txt");
        missionNameList.add("Missions/mission04.txt");
        missionNameList.add("Missions/mission05.txt");
        missionNameList.add("Missions/mission06.txt");
        missionNameList.add("Missions/mission07.txt");
        missionNameList.add("Missions/mission08.txt");
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
