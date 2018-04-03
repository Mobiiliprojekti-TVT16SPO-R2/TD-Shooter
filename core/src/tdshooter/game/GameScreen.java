package tdshooter.game;

import java.util.ArrayList;

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
    private boolean shooting = false;
    private float background_y = 0;
    private int scrollSpeed = 100;

    Player player;
    Texture basicEnemy;
    Texture bulletImage;
    Texture background;
    Texture background_2;
    Sound hitSound;
    Music rainMusic;
    OrthographicCamera camera;
    ArrayList<Encounter> encounters;
    ArrayList<Projectile> playerProjectiles;

    long prevEnemyTime;
    long lastBulletTime;
    long oldHitsound;
    long oldHitsound2;
    int dropsGathered;
    //adding FPS-counter
    // private BitmapFont fpscounter;
    private int fps;


    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        player = new Player(viewPortWidth / 2 - 64 / 2,20, 64 , 64, 100,50);

        // load the images for the droplet and the bucket, 64x64 pixels each
        basicEnemy = new Texture(Gdx.files.internal("Encounters/AlienBeast_LVL_1.png"));
        bulletImage = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));
        background = new Texture(Gdx.files.internal("testistausta.png"));
        background_2 = background;
        // load the drop sound effect and the rain background "music"
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hitSound.wav"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewPortWidth, viewPortHeight);

        // create the encounters arraylist and spawn the first raindrop
        encounters = new ArrayList<Encounter>();
        spawnEnemy();

        //create playerprojectilearraylist
        playerProjectiles = new ArrayList<Projectile>();

        //Play sound Effects once, to initialize prev_sound_id
        oldHitsound = hitSound.play(0.0f);
        oldHitsound2 = hitSound.play(0.0f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        fps = Gdx.graphics.getFramesPerSecond();

        processUserInput();
        limitPlayerMovement();
        // check if we need to create a new bullet
        if (shooting){
            if (TimeUtils.nanoTime() - lastBulletTime > 70000000)
                spawnBullet();
        }
        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - prevEnemyTime > 200000000)
            spawnEnemy();
        
        moveAllObjects();
        checkCollisions();
        drawAllObjects();
    }

    private void processUserInput() {
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            player.setDestination(touchPos);
            player.setMoving(true);
            shooting = true;
        }
        else
        {
            player.setMoving(false);
            shooting = false;
        }

    }

    private void spawnEnemy() {
        Encounter encounter = new Encounter(MathUtils.random(128, viewPortWidth - 64), viewPortHeight,
                64,64, 220, 1, basicEnemy);

        encounters.add(encounter);
        prevEnemyTime = TimeUtils.nanoTime();
    }

    private void spawnBullet() {   //Bullet positions with these images and hitboxes,  x: 24= mid, 0 and 48 wing edges
        Projectile bullet = new Projectile((int)player.hitbox.x + 24,(int)player.hitbox.y + 48,
                24, 36, 5, 800, bulletImage);
        playerProjectiles.add(bullet);

        Projectile bullet2 = new Projectile((int)player.hitbox.x ,(int)player.hitbox.y + 32,
                24, 36, 5, 800, bulletImage);
        playerProjectiles.add(bullet2);

        Projectile bullet3 = new Projectile((int)player.hitbox.x + 48,(int)player.hitbox.y + 32,
                24, 36, 5, 800, bulletImage);
        playerProjectiles.add(bullet3);

        Projectile bullet4 = new Projectile((int)player.hitbox.x + 16,(int)player.hitbox.y + 40,
                24, 36, 5, 800, bulletImage);
        playerProjectiles.add(bullet4);

        Projectile bullet5 = new Projectile((int)player.hitbox.x + 32,(int)player.hitbox.y + 40,
                24, 36, 5, 800, bulletImage);
        playerProjectiles.add(bullet5);

        lastBulletTime = TimeUtils.nanoTime();
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
                if (bullet.hitbox.y > viewPortHeight + 64) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter)){
                    hitSound.stop(oldHitsound2);
                    oldHitsound2 = oldHitsound;
                    oldHitsound = hitSound.play();
                    encounter.getsDamage(bullet.damage);
                    playerProjectiles.remove(j);
                }
            }
            if (encounter.isDestroyed()){
                encounters.remove(i);
            }
        }
        if (player.isDestroyed()){
            endGame();
        }
    }

    private void endGame() {
        game.setScreen(new MainMenuScreen(game));
        dispose();
    }

    private void limitPlayerMovement() {
        // make sure the player stays within the screen bounds
        if (player.hitbox.x < 0)
            player.hitbox.x= 0;
        if (player.hitbox.x > viewPortWidth - 64)
            player.hitbox.x = viewPortWidth - 64;
    }

    private void drawAllObjects() {
        // DRAW ALL OBJECTS HERE
        game.batch.begin();
        game.batch.draw(background, 0 , background_y);
        if (background_y < -2400) {
            game.batch.draw(background_2, 0, background_y + 3200);
        }
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , viewPortHeight - 60);
        game.font.draw(game.batch, "Projectiles: " + playerProjectiles.size(), 0 , viewPortHeight - 90);
        game.font.draw(game.batch, "Encounters: " + encounters.size(), 0 , viewPortHeight - 120);
        game.batch.draw(player.playerImage, player.hitbox.getX(), player.hitbox.getY(), player.hitbox.getWidth(), player.hitbox.getHeight());
        for (Encounter encounter : encounters) {
            game.batch.draw(encounter.encounterImage, encounter.hitbox.x, encounter.hitbox.y);
        }
        for (Projectile bullet : playerProjectiles) {
            game.batch.draw(bullet.bulletImage, bullet.hitbox.x, bullet.hitbox.y);
        }
        game.batch.end();
    }

    private void moveAllObjects() {
        player.move(Gdx.graphics.getDeltaTime());
        for (Projectile bullet : playerProjectiles){
            bullet.hitbox.y += bullet.speed * Gdx.graphics.getDeltaTime();
        }
        for (Encounter encounter : encounters){
            encounter.hitbox.y -= 150 * Gdx.graphics.getDeltaTime();
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
    }
}
