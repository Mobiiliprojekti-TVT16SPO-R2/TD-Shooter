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
    private int points = 0;

    private float x_input;
    private float y_input;

    Item item;
//    int item_x = 0;
//    int item_y = 0;

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

    private long lastDropTime;
    private long lastBulletTime;
    private long oldHitsound;
    private long oldHitsound2;

    //adding FPS-counter
    private int fps;
    private float touchPos_x;
    private float touchPos_y;

    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        player = new Player(VIEWPORTWIDTH / 2 - 64 / 2,20, PLAYERSIZE_X , PLAYERSIZE_Y, 1500,50);


        // load the images for the enemies, 64x64 pixels each
        basicEnemy = new Texture(Gdx.files.internal("Encounters/AlienBeast_Test_1_small.png"));
        shootingEnemy = new Texture(Gdx.files.internal("Encounters/AlienFighter_Test_1_small.png"));
        bulletImage = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));
        healtpackTexture = new Texture(Gdx.files.internal("items/healtpack_test.png"));
        flightSpeedTexture = new Texture(Gdx.files.internal("items/flightspeed_test.png"));
        currencyTexture = new Texture(Gdx.files.internal("items/currency_test.png"));

        background = new Texture(Gdx.files.internal("testistausta.png"));
        background_2 = background;

        // load the drop sound effect and the rain background "music"
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hitSound.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORTWIDTH, VIEWPORTHEIGHT);

        // create the encounters arraylist and spawn the first raindrop
        encounters = new ArrayList<Encounter>();
     //   spawnEncounter();

        //create playerprojectilearraylist
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

        // check if we need to create a new bullet
        if (player.isShooting()){
            if (TimeUtils.nanoTime() - lastBulletTime > 70000000)
                spawnBullet();
        }

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
            player.setShooting(true);
        }
        else
        {
            player.setMoving(false);
            player.setShooting(false);
        }
    }


    private void spawnEncounter(int random) {

        if (random == 0) {
            Encounter encounter = new Encounter(MathUtils.random(0, viewPortWidth - 64), viewPortHeight,
                    64, 64, 100, 5, 120, basicEnemy);

            encounters.add(encounter);
            lastDropTime = TimeUtils.nanoTime();
        }
        else if (random == 1){
            ShootingEnemy encounterS = new ShootingEnemy(MathUtils.random(0, viewPortWidth - 64), viewPortHeight,
                    64, 64, 75, 5, 120, 50, shootingEnemy);

            encounters.add(encounterS);
            lastDropTime = TimeUtils.nanoTime();
        }
    }

    private void spawnBullet() {

        Projectile bullet = new Projectile((int)player.hitbox.x + 48 - 6,(int)player.hitbox.y + 58,
                24, 36, 5, 800, bulletImage);
        playerProjectiles.add(bullet);
        Projectile bullet2 = new Projectile((int)player.hitbox.x + 6,(int)player.hitbox.y + 50,
                24, 36, 5, 800, bulletImage);
        playerProjectiles.add(bullet2);
        Projectile bullet3 = new Projectile((int)player.hitbox.x + 96 - 6,(int)player.hitbox.y + 50,
                24, 36, 5, 800, bulletImage);
        playerProjectiles.add(bullet3);

        lastBulletTime = TimeUtils.nanoTime();
    }
    private void spawnItem(Encounter encounter){
        randomNumber2 = random.nextInt(3);
        if (randomNumber2 == 0) {
            item = new Item((int) encounter.hitbox.x, (int) encounter.hitbox.y, 32, 32, scrollSpeed, 1, healtpackTexture);
            items.add(item);
        }
        else if (randomNumber2 == 1){
            item = new Item((int) encounter.hitbox.x, (int) encounter.hitbox.y, 32, 32, scrollSpeed, 2, flightSpeedTexture);
            items.add(item);
        }
        else if (randomNumber2 == 2){
            item = new Item((int) encounter.hitbox.x, (int) encounter.hitbox.y, 32, 32, scrollSpeed, 4, currencyTexture);
            items.add(item);
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
                hitSound.stop(oldHitsound2);
                oldHitsound2 = oldHitsound;
                oldHitsound = hitSound.play();
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if (bullet.hitbox.y > VIEWPORTHEIGHT + 64) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter)){
                    hitSound.stop(oldHitsound2);
                    oldHitsound2 = oldHitsound;
                    oldHitsound = hitSound.play();
                    encounter.getsDamage(bullet.damage);
                    playerProjectiles.remove(j);
                    if (encounter.isDestroyed()){
                        encountersDestroyed++;
                        points += encounter.getPoints();
                        spawnItem(encounter);

//                        if (encounter instanceof ShootingEnemy) {
//                            points +=  encounter.getPoints();
//                        }
//                        else {
//                            points += encounter.getPoints();
//                        }
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

        game.batch.draw(player.playerImage, player.hitbox.getX(), player.hitbox.getY(), player.hitbox.getWidth() + 6, player.hitbox.getHeight() +6);
        for (Encounter raindrop : encounters) {
            game.batch.draw(raindrop.encounterImage, raindrop.hitbox.x, raindrop.hitbox.y, raindrop.hitbox.getWidth(), raindrop.hitbox.getHeight() * 2);
        }
        for (Projectile bullet : playerProjectiles) {
            game.batch.draw(bullet.bulletImage, bullet.hitbox.x, bullet.hitbox.y);
        }
        for (Projectile bullet : enemyProjectiles) {
            game.batch.draw(bullet.bulletImage, bullet.hitbox.x, bullet.hitbox.y, bullet.hitbox.getWidth(), bullet.hitbox.getHeight());
        }for (Item item : items) {
            game.batch.draw(item.itemTexture, item.hitbox.x, item.hitbox.y, item.hitbox.getWidth(), item.hitbox.getHeight());
        }

        game.font.draw(game.batch, "FPS: " + fps, 0, VIEWPORTHEIGHT - 30);
        game.font.draw(game.batch, "Player points: " + points, 0, viewPortHeight);
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , viewPortHeight - 60);
        game.font.draw(game.batch, "Projectiles: " + playerProjectiles.size(), 0 , viewPortHeight - 90);
        game.font.draw(game.batch, "Encounters: " + encounters.size(), 0 , viewPortHeight - 120);
        game.font.draw(game.batch, "Currency: " + player.getCurrency(), 0, viewPortHeight -150);
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
        hitSound.dispose();
        rainMusic.dispose();
    }
}
