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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by leevi on 16.3.2018.
 */

public class GameScreen implements Screen, InputProcessor {

    final TDShooterGdxGame game;
    private Stage stage;
    private final int VIEWPORTHEIGHT = 1280;
    private final int VIEWPORTWIDTH = 720;
    private final int PLAYERSIZE_X = 128;
    private final int PLAYERSIZE_Y = 128;
    private final int FLIGHTZONE_X_MIN = (PLAYERSIZE_X / 2);
    private final int FLIGHTZONE_X_MAX = (VIEWPORTWIDTH - (PLAYERSIZE_X / 2));
    private final int FLIGHTZONE_Y_MIN = (PLAYERSIZE_X / 2);
    private final int FLIGHTZONE_Y_MAX = (VIEWPORTHEIGHT - 200);

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

    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        encounters = new ArrayList<Encounter>();
        playerProjectiles = new ArrayList<Projectile>();
        enemyProjectiles = new ArrayList<Projectile>();

        oldSoundIds = new ArrayList<Long>();

        mission = new Mission("Missions/mission01.txt", game.assets, encounters);
        player = game.player;
        background = new ScrollingBackground(mission.getBackground());
        background.setLooping(mission.isBackgroundLooping());
        background.setScrollSpeed(mission.getScrollSpeed());
        backgroundMusic = mission.getBackgroundMusic();
        backgroundMusic.play();
        items = new ArrayList<Item>();

        //Play sound Effects once, to initialize prev_sound_id
        oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play(0.0f));
        oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play(0.0f));

        camera.setToOrtho(false, VIEWPORTWIDTH, VIEWPORTHEIGHT);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
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
            processUserInputWhenPaused();
        } else {
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

    private void processUserInputWhenPaused() {
        if(Gdx.input.isTouched()) {
            gamePaused = false;
        }
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
            if (encounter.hitbox.y + 64 < 0) {
                encounter.getsDamage(1000);
            } else if (encounter.overlaps(player)){
                encounter.collidesWith(player);
                player.collidesWith(encounter);
                ((Sound)game.assets.get("hitSound.wav")).stop(oldSoundIds.get(0)); //stop oldest
                oldSoundIds.remove(0); // remove oldest
                oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play()); // play and add new
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if ( (bullet.hitbox.y > VIEWPORTHEIGHT + 64)
                        || (bullet.hitbox.x < -32)
                        || (bullet.hitbox.x > VIEWPORTWIDTH + 32) ) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter)){
                    ((Sound)game.assets.get("hitSound.wav")).stop(oldSoundIds.get(0)); //stop oldest
                    oldSoundIds.remove(0); // remove oldest
                    oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play()); // play and add new
                    encounter.getsDamage(bullet.damage);
                    playerProjectiles.remove(j);
                    if (encounter.isDestroyed()){
                        player.setPoints(encounter.getPoints());
                        encounter.dropItem(items);
                    }
                }
            }
            if (encounter.isDestroyed()){
                encounters.remove(i);
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
                oldSoundIds.add(((Sound)game.assets.get("hitSound.wav")).play());
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
        if (gamePaused){
            game.font.draw(game.batch, "GAME PAUSED", 150, 400, 200, 200, true);
        }
        game.font.draw(game.batch, "FPS: " + fps, 0, VIEWPORTHEIGHT - 30);
        game.font.draw(game.batch, "Player points: " + player.getPoints(), 0, VIEWPORTHEIGHT);
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , VIEWPORTHEIGHT - 60);
        game.font.draw(game.batch, "Projectiles: " + playerProjectiles.size(), 0 , VIEWPORTHEIGHT - 90);
        game.font.draw(game.batch, "Encounters: " + encounters.size(), 0 , VIEWPORTHEIGHT - 120);
        game.font.draw(game.batch, "Currency: " + player.getCurrency(), 0 , VIEWPORTHEIGHT - 150);
        game.font.draw(game.batch, "WEAPONCHOICE: " + player.getWeaponChoice(), 0 , VIEWPORTHEIGHT - 180);
        game.batch.end();
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
