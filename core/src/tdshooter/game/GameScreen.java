package tdshooter.game;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by leevi on 16.3.2018.
 */

public class GameScreen implements Screen, InputProcessor {

    final TDShooterGdxGame game;
    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    private final int PLAYERSIZE_X = 128;
    private final int PLAYERSIZE_Y = 128;
    private final int FLIGHTZONE_X_MIN = (PLAYERSIZE_X / 2);
    private final int FLIGHTZONE_X_MAX = (VIEWPORTWIDTH - (PLAYERSIZE_X / 2));
    private final int FLIGHTZONE_Y_MIN = (PLAYERSIZE_X / 2);
    private final int FLIGHTZONE_Y_MAX = (VIEWPORTHEIGHT - 200);
    private Preferences options;

    private boolean gamePaused = false;
    private Player player;
    private ScrollingBackground background;
    private Music backgroundMusic;
    private OrthographicCamera camera;
    private ArrayList<Encounter> encounters;
    private ArrayList<Projectile> playerProjectiles;
    private Mission mission;
    private ArrayList<Projectile> enemyProjectiles;
    private ArrayList<Item> items;

    private int encountersDestroyed;
    private ArrayList<Long> oldSoundIds;

    private int fps;
    private Skin skin;
    private TextureAtlas atlas;
    private StretchViewport viewport;
    private Stage stage;
    private boolean inputBoolean = true;
    private Texture menuTexture;
    private Image menuImage;
    private Table menuTable;
    private float soundVolume = 0.5f;
    private float musicVolume = 0.5f;
    private boolean soundMuted = false;
    private boolean musicMuted = false;
    private Slider soundSlider;
    private Slider musicSlider;

    private GameHUD hud;

    public GameScreen(final TDShooterGdxGame game, String missionName) {
        this.game = game;
        camera = new OrthographicCamera();
        encounters = new ArrayList<Encounter>();
        playerProjectiles = new ArrayList<Projectile>();
        enemyProjectiles = new ArrayList<Projectile>();

        options = Gdx.app.getPreferences("options");
        if(options.contains("soundvolume")) {
            soundVolume = options.getFloat("soundvolume");
        }
        if(options.contains("musicvolume")) {
            musicVolume = options.getFloat("musicvolume");
        }
        if(options.contains("soundmuted")) {
            soundMuted = options.getBoolean("soundmuted");
            if (soundMuted) {
                soundVolume = 0.0f;
            }
        }
        if(options.contains("musicmuted")) {
            musicMuted = options.getBoolean("musicmuted");
            if (musicMuted) {
                musicVolume = 0.0f;
            }
        }

        oldSoundIds = new ArrayList<Long>();

        viewport = new StretchViewport(VIEWPORTWIDTH, VIEWPORTHEIGHT, camera);
        viewport.apply();
        atlas = game.assets.get("Skin/glassy-ui.atlas");
        skin = game.assets.get("Skin/glassy-ui.json");
        stage = new Stage(viewport, game.batch);
        menuTexture = game.assets.get("menu_test.png");
        menuImage = new Image(menuTexture);

        mission = new Mission(missionName, game.assets, encounters);
        player = new Player(VIEWPORTWIDTH / 2 - 64 / 2,20, PLAYERSIZE_X , PLAYERSIZE_Y, 100,50, game.assets);
        background = new ScrollingBackground(mission.getBackground());
        background.setLooping(mission.isBackgroundLooping());
        background.setScrollSpeed(mission.getScrollSpeed());
        backgroundMusic = mission.getBackgroundMusic();
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(musicVolume);
        backgroundMusic.play();
        items = new ArrayList<Item>();
        hud = new GameHUD(viewport, game.batch, (Skin) game.assets.get("Skin/glassy-ui.json"), player);


        //Play sound Effects once, to initialize prev_sound_id
        oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play(0.0f));
        oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play(0.0f));

//        camera.setToOrtho(false, VIEWPORTWIDTH, VIEWPORTHEIGHT);
        Gdx.input.setInputProcessor(this);
//        Gdx.input.setCatchBackKey(true);

        setPauseMenu();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        fps = Gdx.graphics.getFramesPerSecond();

        if (gamePaused){
            inputBoolean = true;
            Gdx.input.setInputProcessor(stage);
        } else {
            if (inputBoolean){
                inputBoolean = false;
                Gdx.input.setInputProcessor(this);
            }
            processUserInput();

            if (mission.isMissionOver()) {
                endGame();
            }

            moveAllObjects(delta);
            checkCollisions();
            mission.update(delta);
        }
        drawAllObjects();
    }



    private void processUserInput() {
        if(Gdx.input.isTouched() || Gdx.input.isTouched(1) || Gdx.input.isTouched(2)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Limit player movement to flightzone
            if (touchPos.x < FLIGHTZONE_X_MIN){
                touchPos.x = FLIGHTZONE_X_MIN;
            } else if (touchPos.x > FLIGHTZONE_X_MAX) {
                touchPos.x = FLIGHTZONE_X_MAX;
            }
            if (touchPos.y < FLIGHTZONE_Y_MIN) {
                touchPos.y = FLIGHTZONE_Y_MIN;
            } else if ( touchPos.y > FLIGHTZONE_Y_MAX) {
                touchPos.y = FLIGHTZONE_Y_MAX;
            }
            player.setDestination(touchPos);
            player.setMoving(true);
            player.shoot(playerProjectiles);
        }
        else
        {
            player.setMoving(false);
        }
        if (Gdx.input.isTouched(1)) {
            player.swapWeapons();
        }
    }

    private void checkCollisions() {
        // move the encounters, remove any that are beneath the bottom edge of
        // the screen or that hit the player.
        for (int i = 0; i < encounters.size(); i++) {
            Encounter encounter = encounters.get(i);
            boolean loot_not_given = true;
            if (encounter.hitbox.y + 64 < 0) {
                encounter.getsDamage(1000);
            } else if (encounter.overlaps(player)){
                encounter.collidesWith(player);
                player.collidesWith(encounter);
                ((Sound)game.assets.get("hitSound.wav")).stop(oldSoundIds.get(0)); //stop oldest
                oldSoundIds.remove(0); // remove oldest
                oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play(soundVolume)); // play and add new
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if (bullet.overlaps(encounter)){
                    ((Sound)game.assets.get("hitSound.wav")).stop(oldSoundIds.get(0)); //stop oldest
                    oldSoundIds.remove(0); // remove oldest
                    oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play(soundVolume)); // play and add new
                    encounter.getsDamage(bullet.damage);
                    playerProjectiles.remove(j);
                    if (encounter.isDestroyed() && loot_not_given){
                        player.setPoints(encounter.getPoints());
                        encounter.dropItem(items);
                        loot_not_given = false;  // give loot only once
                    }
                }
            }
            if (encounter.isDestroyed()){
                encounters.remove(i);
            }
        }
        for (int j = 0; j < playerProjectiles.size(); j++) {
            Projectile bullet = playerProjectiles.get(j);
            if ((bullet.hitbox.y > VIEWPORTHEIGHT + 64)
                    || (bullet.hitbox.x < -32)
                    || (bullet.hitbox.x > VIEWPORTWIDTH + 32)) {
                playerProjectiles.remove(j);
            }
        }
        for (int i = 0; i < enemyProjectiles.size() ; i++) {
            Projectile bullet = enemyProjectiles.get(i);
            if (bullet.hitbox.y < 0) {
                enemyProjectiles.remove(i);
            }
            else if (bullet.overlaps(player)){
                ((Sound)game.assets.get("hitSound.wav")).stop(oldSoundIds.get(0)); //stop oldest
                oldSoundIds.remove(0); // remove oldest
                oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play(soundVolume));
                player.getsDamage(bullet.damage);
                enemyProjectiles.remove(i);
            }
        }
        if (player.isDestroyed()){
            endGame();
        }

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.hitbox.y < 0) {
                items.remove(i);
            }
            else if (item.overlaps(player)) {
                Gdx.app.log("DEBUG", "item overlaps player");
                player.pickUp(item);
                items.remove(i);
            }
        }
    }

    private void endGame() {
        saveCurrency();
        game.setScreen(new StageClearedScreen(game, player.getCurrency(), player.getHitPoints()));
        dispose();
    }

    private void drawAllObjects()
    {
        game.batch.begin();
        background.draw(game.batch);
        player.draw(game.batch);
        for (Item item : items) {
            item.draw(game.batch);
        }
        for (Encounter encounter : encounters) {
            encounter.draw(game.batch);
        }
        for (Projectile bullet : playerProjectiles) {
            bullet.draw(game.batch);
        }
        for (Projectile bullet : enemyProjectiles) {
            bullet.draw(game.batch);
        }
        game.font.draw(game.batch, "FPS: " + fps, 0, VIEWPORTHEIGHT - 30);
        game.font.draw(game.batch, "Player points: " + player.getPoints(), 0, VIEWPORTHEIGHT);
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , VIEWPORTHEIGHT - 60);
        game.font.draw(game.batch, "Projectiles: " + playerProjectiles.size(), 0 , VIEWPORTHEIGHT - 90);
        game.font.draw(game.batch, "Encounters: " + encounters.size(), 0 , VIEWPORTHEIGHT - 120);
        game.font.draw(game.batch, "Currency: " + player.getCurrency(), 0 , VIEWPORTHEIGHT - 150);
        game.font.draw(game.batch, "WEAPONCHOICE: " + player.getWeaponChoice(), 0 , VIEWPORTHEIGHT - 180);
        game.batch.end();

        hud.draw();

        if (gamePaused) {
            stage.act();
            stage.draw();
        }
    }

    private void moveAllObjects(float delta)
    {
        background.update(delta);
        player.move(delta);
        for (Projectile bullet : playerProjectiles){
            bullet.update();
        }
        for (Projectile bullet : enemyProjectiles){
            bullet.update();
        }
        for (Encounter encounter : encounters){
            encounter.update();
            if (encounter instanceof ShootingEnemy) {
                ((ShootingEnemy) encounter).shoot(enemyProjectiles);
            }
        }
        for (Item item : items){
            item.update();
        }
        hud.update(delta);
    }

    private void saveCurrency()
    {
        Preferences prefs = Gdx.app.getPreferences("savedata");
        String currencyKey = "currency";
        int savedCurrency;
        int earnedCurrency = player.getCurrency();
        int totalCurrency;
        final int maxCurrency = 99999;

        if(prefs.contains(currencyKey))
        {
            savedCurrency = prefs.getInteger(currencyKey);
            totalCurrency = earnedCurrency + savedCurrency;
            if(totalCurrency > maxCurrency)
            {
                totalCurrency = 99999;
            }
        }
        else
        {
            totalCurrency = earnedCurrency;
        }

        prefs.putInteger(currencyKey, totalCurrency);
        prefs.flush();
    }

    private void setPauseMenu(){

        int pauseMenuWidth = 300;
        int pauseMenuHeight = 600;

        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        TextButton musicButton = new TextButton("", skin);
        TextButton soundButton = new TextButton("", skin);

        soundSlider = new Slider(0.0f, 1.0f, 0.01f, false, skin);
        soundSlider.setValue(soundVolume);

        musicSlider = new Slider(0.0f, 1.0f, 0.01f, false, skin);
        musicSlider.setValue(musicVolume);

        resumeButton.setWidth(pauseMenuWidth);
        exitButton.setWidth(pauseMenuWidth);
        musicButton.setWidth(pauseMenuWidth / 3);
        soundButton.setWidth(pauseMenuWidth / 3);
        soundSlider.setWidth(pauseMenuWidth / 3 * 2);
        musicSlider.setWidth(pauseMenuWidth / 3 * 2);

        resumeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gamePaused = false;
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MissionsMenu(game));
                dispose();
            }
        });
        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (musicMuted) {
                    if(options.contains("musicvolume")) {
                        musicVolume = options.getFloat("musicvolume");
                    }
                    musicMuted = false;
                    backgroundMusic.setVolume(musicVolume);
                    options.putBoolean("musicmuted", musicMuted);
                    options.flush();
                }
                else {
                    musicVolume = 0.0f;
                    musicMuted = true;
                    backgroundMusic.setVolume(musicVolume);
                    options.putBoolean("musicmuted", musicMuted);
                    options.flush();
                }
            }
        });
        soundButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (soundMuted) {
                    if(options.contains("soundvolume")) {
                        soundVolume = options.getFloat("soundvolume");
                    }
                    soundMuted = false;
                    options.putBoolean("soundmuted", soundMuted);
                    options.flush();
                }
                else {
                    soundVolume = 0.0f;
                    soundMuted = true;
                    options.putBoolean("soundmuted", soundMuted);
                    options.flush();
                }
            }
        });

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundVolume = soundSlider.getValue();
                options.putFloat("soundvolume", soundVolume);
                options.flush();
                if (soundVolume == 0.0f) {
                    soundMuted = true;
                } else {
                    soundMuted = false;
                }
                options.putBoolean("soundmuted", soundMuted);
                options.flush();
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicVolume = musicSlider.getValue();
                backgroundMusic.setVolume(musicVolume);
                options.putFloat("musicvolume", musicVolume);
                options.flush();
                if (musicVolume == 0.0f) {
                    musicMuted = true;
                } else {
                    musicMuted = false;
                }
                options.putBoolean("musicmuted", musicMuted);
                options.flush();
            }
        });

        float corner_x = VIEWPORTWIDTH / 2 - pauseMenuWidth / 2;
        float corner_y = VIEWPORTHEIGHT / 2 + pauseMenuHeight / 2 - 50;
        float padding = 10;
        float big_spacing = 100;
        float spacing = 50;

        resumeButton.setPosition(corner_x - padding, corner_y - padding - big_spacing);
        soundSlider.setPosition(corner_x - padding + (pauseMenuWidth / 3 + padding), corner_y - padding - big_spacing - spacing);
        soundButton.setPosition(corner_x - padding, corner_y - padding - big_spacing * 2);
        musicSlider.setPosition(corner_x - padding + (pauseMenuWidth / 3 + padding), corner_y - padding - big_spacing * 2 - spacing);
        musicButton.setPosition(corner_x - padding, corner_y - padding - big_spacing * 3);
        exitButton.setPosition(corner_x - padding, corner_y - padding - big_spacing * 4);

        menuImage.setPosition(VIEWPORTWIDTH / 2 - (menuImage.getWidth() / 2), VIEWPORTHEIGHT / 2 - (menuImage.getHeight() / 2));

        stage.addActor(menuImage);
        stage.addActor(resumeButton);
        stage.addActor(soundSlider);
        stage.addActor(soundButton);
        stage.addActor(musicSlider);
        stage.addActor(musicButton);
        stage.addActor(exitButton);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // backgroundMusic.play();
    }

    @Override
    public void hide() {
        gamePaused = true;
    }

    @Override
    public void pause() {
        gamePaused = true;
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
//        skin.dispose();
//        atlas.dispose();
        stage.dispose();
        backgroundMusic.stop();
        hud.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK) {
            gamePaused = true;
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
