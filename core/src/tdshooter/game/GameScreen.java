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
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    ArrayList<Encounter> encounters;
    ArrayList<Projectile> playerProjectiles;

    long lastDropTime;
    long lastBulletTime;
    int dropsGathered;
    //adding FPS-counter
    // private BitmapFont fpscounter;
    private int fps;
//    fpscounter = new BitmapFont();
//		fpscounter.setColor(Color.RED);
//		fpscounter.getData().setScale(4,4);
//
//    fps = Gdx.graphics.getFramesPerSecond();
//		fpscounter.draw(batch, "" + fps, 20, 450);


    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        player = new Player(viewPortWidth / 2 - 64 / 2,20, 64 , 64, 100,50);

        // load the images for the droplet and the bucket, 64x64 pixels each
        basicEnemy = new Texture(Gdx.files.internal("Encounters/AlienBeast_Test_1_small.png"));
        bulletImage = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));

        background = new Texture(Gdx.files.internal("testistausta.png"));
        background_2 = background;

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewPortWidth, viewPortHeight);

        // create the encounters arraylist and spawn the first raindrop
        encounters = new ArrayList<Encounter>();
        spawnRaindrop();

        //create playerprojectilearraylist
        playerProjectiles = new ArrayList<Projectile>();

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

        limitPlayerMovement();

        // check if we need to create a new bullet
        if (shooting){
            if (TimeUtils.nanoTime() - lastBulletTime > 100000000)
                spawnBullet();
        }

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 200000000)
            spawnRaindrop();
        
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


    private void spawnRaindrop() {
        Encounter raindrop = new Encounter(MathUtils.random(0, viewPortWidth - 64), viewPortHeight,
                64,64, 5, 5, basicEnemy);

        encounters.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    private void spawnBullet() {
        Projectile bullet = new Projectile((int)player.hitbox.x + 16,(int)player.hitbox.y + 46,
                24, 36, 5, 400, bulletImage);

        playerProjectiles.add(bullet);
        Projectile bullet2 = new Projectile((int)player.hitbox.x,(int)player.hitbox.y + 32,
                24, 36, 5, 400, bulletImage);

        playerProjectiles.add(bullet2);
        Projectile bullet3 = new Projectile((int)player.hitbox.x + 32,(int)player.hitbox.y + 32,
                24, 36, 5, 400, bulletImage);

        playerProjectiles.add(bullet3);
        lastBulletTime = TimeUtils.nanoTime();
    }

    private void checkCollisions() {
        // this is collisionchecking

        // move the encounters, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.

        for (int i = 0; i < encounters.size(); i++) {
            Encounter encounter = encounters.get(i);
            if (encounter.hitbox.y + 64 < 0) {
                encounters.remove(i);
            } else if (encounter.overlaps(player.hitbox)){
                dropsGathered++;
                dropSound.play();
                encounter.collidesWith(player);
                player.collidesWith(encounter);
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if (bullet.hitbox.y > viewPortHeight - 100) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter.hitbox)){
                    dropSound.play();
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
        game.font.draw(game.batch, "FPS: " + fps, 0, viewPortHeight - 30);
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, viewPortHeight);
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , viewPortHeight - 60);
        game.font.draw(game.batch, "Projectiles: " + playerProjectiles.size(), 0 , viewPortHeight - 90);
        game.font.draw(game.batch, "Raindrops: " + encounters.size(), 0 , viewPortHeight - 120);
        game.batch.draw(player.playerImage, player.hitbox.getX(), player.hitbox.getY(), player.hitbox.getWidth(), player.hitbox.getHeight());
        for (Encounter raindrop : encounters) {
            game.batch.draw(raindrop.encounterImage, raindrop.hitbox.x, raindrop.hitbox.y);
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
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();
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
        dropSound.dispose();
        rainMusic.dispose();
    }
}
