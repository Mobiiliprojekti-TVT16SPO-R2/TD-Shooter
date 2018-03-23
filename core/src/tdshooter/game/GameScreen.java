package tdshooter.game;

import java.util.ArrayList;
import java.util.Random;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by leevi on 16.3.2018.
 */

public class GameScreen implements Screen {
    final TDShooterGdxGame game;
    private final int viewPortHeight = 800;
    private final int viewPortWidth = 480;
    private final int VIEWPORTHEIGHT = 800;
    private final int VIEWPORTWIDTH = 480;
    private final int PLAYERSIZE_X = 90;
    private final int PLAYERSIZE_Y = 90;
    private final int FLIGHTZONE_X_MIN = 32; //(PLAYERSIZE_X / 2);
    private final int FLIGHTZONE_X_MAX = 448; //(VIEWPORTWIDTH - (PLAYERSIZE_X / 2));
    private final int FLIGHTZONE_Y_MIN = 64;
    private final int FLIGHTZONE_Y_MAX = 300;  //((VIEWPORTHEIGHT / 4) + 100);

    private float background_y = 0;
    private int scrollSpeed = 100;
    private int randomNumber = 0;
    private int randomNumber2 = 0;
    private int encountersDestroyed;

    private float x_input;
    private float y_input;

    Item item;

    Player player;
    Texture basicEnemy;
    Texture shootingEnemy;
    Texture bulletImage;
    Texture healtpackTexture;
    Texture flightSpeedTexture;
    Texture currencyTexture;
    Texture background;
    Texture background_2;
    Sound hitSound;
    Music rainMusic;
    OrthographicCamera camera;
    ArrayList<Encounter> encounters;
    ArrayList<Projectile> playerProjectiles;
    ArrayList<Projectile> enemyProjectiles;
    ArrayList<Item> items;
    Random random;

    long lastDropTime;
    long lastBulletTime;
    private int encountersDestroyed;
    long oldHitsound;
    long oldHitsound2;

    //adding FPS-counter
    private int fps;

    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        player = new Player(VIEWPORTWIDTH / 2 - 64 / 2,20, PLAYERSIZE_X , PLAYERSIZE_Y, 100,50);

        random = new Random();
        // load the images for the enemies, 64x64 pixels each
        basicEnemy = new Texture(Gdx.files.internal("Encounters/AlienBeast_Test_1_small.png"));
        shootingEnemy = new Texture(Gdx.files.internal("Encounters/AlienFighter_Test_1_small.png"));
        bulletImage = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));
        Gdx.app.log("LOADING", "bullet and encounters loaded");

        background = new Texture(Gdx.files.internal("testistausta.png"));
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

        processUserInput();

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            long randomSeed = TimeUtils.nanoTime();
            random = new Random(randomSeed);
            randomNumber = random.nextInt(2);
            spawnEncounter(randomNumber);
            }

        moveAllObjects();

        checkCollisions();
        drawAllObjects();
    }

    private void processUserInput() {
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();

            x_input = Gdx.input.getX();
            y_input = Gdx.input.getY();

            touchPos.set(x_input, y_input, 0);
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
            Encounter encounter = new Encounter(MathUtils.random(0, viewPortWidth - 64), viewPortHeight,
                    64, 128, 100, 5, 120, basicEnemy);
            encounters.add(encounter);
        }
        else if (random == 1){
            ShootingEnemy encounter = new ShootingEnemy(MathUtils.random(0, viewPortWidth - 64), viewPortHeight,
                    64, 128, 75, 5, 120, shootingEnemy);
            encounters.add(encounter);
        }
        lastDropTime = TimeUtils.nanoTime();
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
                        || (bullet.hitbox.y < -32)
                        || (bullet.hitbox.y < VIEWPORTWIDTH + 32) ) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter)){
                    hitSound.stop(oldHitsound2);
                    oldHitsound2 = oldHitsound;
                    oldHitsound = hitSound.play();
                    encounter.getsDamage(bullet.damage);
                    playerProjectiles.remove(j);
                    if (encounter.isDestroyed()){
                        encountersDestroyed++;
                        player.setPoints(encounter.getPoints());
                        spawnItem(encounter);
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
            Gdx.app.log("Points", "Player points: " + points);
            endGame();
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.hitbox.y < 0) {
                items.remove(i);
            }
            else if (item.overlaps(player)) {
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
        if (background_y < -2400) {
            game.batch.draw(background_2, 0, background_y + 3200);
        }
        player.draw(game.batch);
        for (Encounter encounter : encounters) {
            encounter.draw(game.batch);
        }
        for (Projectile bullet : playerProjectiles) {
            bullet.draw(game.batch);
        }
        for (Projectile bullet : enemyProjectiles) {
            bullet.draw(game.batch);
        }
        for (Item item : items) {
            game.batch.draw(item.itemTexture, item.hitbox.x, item.hitbox.y, item.hitbox.getWidth(), item.hitbox.getHeight());
        }

        game.font.draw(game.batch, "FPS: " + fps, 0, VIEWPORTHEIGHT - 30);
        game.font.draw(game.batch, "Player points: " + player.getPoints(), 0, viewPortHeight);
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , viewPortHeight - 60);
        game.font.draw(game.batch, "Projectiles: " + playerProjectiles.size(), 0 , viewPortHeight - 90);
        game.font.draw(game.batch, "Encounters: " + encounters.size(), 0 , viewPortHeight - 120);
        game.font.draw(game.batch, "WEAPONCHOICE: " + player.getWeaponChoice(), 0 , viewPortHeight - 150);
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
        if (background_y < -3199) {
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
    }

    @Override
    public void pause() {
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
}
