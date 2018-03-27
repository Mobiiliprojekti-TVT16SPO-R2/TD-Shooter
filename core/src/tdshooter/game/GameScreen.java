package tdshooter.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;

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
    private float background_y = 0;
    private int scrollSpeed = 100;
    private int randomNumber = 0;

    private RandomXS128 random;
    private RandomXS128 randomSpawnPoint;

    private Player player;
    private Texture basicEnemy;
    private Texture shootingEnemy;
    private Texture shootingEnemyLVL2;
    private Texture bulletImage;
    private Texture background;
    private Texture background_2;
    private Sound hitSound;
    private Music rainMusic;
    private OrthographicCamera camera;
    private ArrayList<Encounter> encounters;
    private ArrayList<Projectile> playerProjectiles;
    private ArrayList<Projectile> enemyProjectiles;
    private ArrayList<Item> items;

    private long lastEnemySpawn;
    private long oldHitsound;
    private long oldHitsound2;

    //adding FPS-counter
    private int fps;

    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        player = new Player(VIEWPORTWIDTH / 2 - PLAYERSIZE_X / 2,20, PLAYERSIZE_X , PLAYERSIZE_Y, 100,50);

        random = new RandomXS128();
        randomSpawnPoint = new RandomXS128();
        Gdx.app.log("LOADING", "Start loading assets..");

        // load the images for the enemies, 64x64 pixels each
        basicEnemy = new Texture(Gdx.files.internal("Encounters/AlienBeast_LVL_1_Test.png"));

        Gdx.app.log("LOADING", "Asset 1 loaded..");
        shootingEnemy = new Texture(Gdx.files.internal("Encounters/AlienFighter_LVL_1_Test.png"));
        shootingEnemyLVL2 = new Texture(Gdx.files.internal("Encounters/AlienFighter_LVL_2.png"));

        Gdx.app.log("LOADING", "Asset 2 loaded..");
        bulletImage = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));
        Gdx.app.log("LOADING", "Asset 3 loaded..");

//        background = new Texture(Gdx.files.internal("testistausta.png"));
        background = new Texture(Gdx.files.internal("Backgrounds/Map_Test_720_2297_2.png"));
        Gdx.app.log("LOADING", "testistausta loaded");
        background_2 = background;

        // load the drop sound effect and the rain background "music"
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hitSound.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        Gdx.app.log("LOADING", "all assets loaded");

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORTWIDTH, VIEWPORTHEIGHT);

        // create the encounters arraylist
        encounters = new ArrayList<Encounter>();
     //   spawnEncounter();

        //create projectile-arraylists
        playerProjectiles = new ArrayList<Projectile>();
        enemyProjectiles = new ArrayList<Projectile>();
        items = new ArrayList<Item>();

        //Play sound Effects once, to initialize prev_sound_id
        oldHitsound = hitSound.play(0.0f);
        oldHitsound2 = hitSound.play(0.0f);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        fps = Gdx.graphics.getFramesPerSecond();

        if (gamePaused){
            processUserInputWhenPaused();
        } else {
            processUserInput();

        // check if we need to create a new enemy
        if (TimeUtils.nanoTime() - lastEnemySpawn > 1000000000) {
            long randomSeed = TimeUtils.nanoTime();
            random = new RandomXS128(randomSeed);
            randomNumber = random.nextInt(3);
            spawnEncounter(randomNumber);
            }

            moveAllObjects();
            checkCollisions();
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

    private void spawnEncounter(int random) {

        if (random == 0) {
            Encounter encounter = new Encounter(randomSpawnPoint.nextInt( VIEWPORTWIDTH - 64), VIEWPORTHEIGHT,
                    100, 128, 100, 5, 350, basicEnemy);
//            Encounter encounter = new Encounter(MathUtils.random(0, VIEWPORTWIDTH - 64), VIEWPORTHEIGHT,
//                    100, 128, 100, 5, 350, basicEnemy);
            encounters.add(encounter);
        }
        else if (random == 1){
            ShootingEnemy encounter = new ShootingEnemy(MathUtils.random(0, VIEWPORTWIDTH - 64), VIEWPORTHEIGHT,
                    62, 111, 75, 15, 320,1, 0 , 500000000, 30, 50, shootingEnemy);
            encounters.add(encounter);
        }
        else if (random == 2){
            ShootingEnemy encounter = new ShootingEnemy(MathUtils.random(0, VIEWPORTWIDTH - 64), VIEWPORTHEIGHT,
                    96, 128, 150, 5, 120,7, 60 , 2100000000, 300, 50, shootingEnemyLVL2);
            encounters.add(encounter);
        }
        lastEnemySpawn = TimeUtils.nanoTime();
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
                hitSound.stop(oldHitsound2);
                oldHitsound2 = oldHitsound;
                oldHitsound = hitSound.play();
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if ( (bullet.hitbox.y > VIEWPORTHEIGHT + 64)
                        || (bullet.hitbox.x < -32)
                        || (bullet.hitbox.x > VIEWPORTWIDTH + 32) ) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter)){
                    hitSound.stop(oldHitsound2);
                    oldHitsound2 = oldHitsound;
                    oldHitsound = hitSound.play();
                    encounter.getsDamage(bullet.damage);
                    playerProjectiles.remove(j);
                    if (encounter.isDestroyed()){
                        player.setPoints(encounter.getPoints());
                        encounter.dropItem(items, scrollSpeed);
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
                hitSound.stop(oldHitsound2);
                oldHitsound2 = oldHitsound;
                oldHitsound = hitSound.play();
                player.getsDamage(bullet.damage);
                enemyProjectiles.remove(i);
            }
        }
        if (player.isDestroyed()){
            Gdx.app.log("Points", "Player points: " + player.getPoints());
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
        game.setScreen(new MainMenuScreen(game));
        dispose();
    }

    private void drawAllObjects() {
        // DRAW ALL OBJECTS HERE
        game.batch.begin();
        game.batch.draw(background, 0 , background_y);
        if (background_y < -(2297-VIEWPORTHEIGHT)) {
            game.batch.draw(background_2, 0, background_y + 2297);
        }
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

    private void moveAllObjects() {
        player.move(Gdx.graphics.getDeltaTime());
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
        background_y -= scrollSpeed * Gdx.graphics.getDeltaTime();
        if (background_y < -2297) {
            background_y = 0;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
//        rainMusic.play();
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
        basicEnemy.dispose();
        shootingEnemy.dispose();
        bulletImage.dispose();
        background.dispose();
        background_2.dispose();
        hitSound.dispose();
        rainMusic.dispose();
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
