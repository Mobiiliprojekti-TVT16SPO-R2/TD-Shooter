package tdshooter.game;

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by leevi on 16.3.2018.
 */

public class GameScreen implements Screen {
    final TDShooterGdxGame game;
    private final int viewPortHeight = 800;
    private final int viewPortWidth = 480;

    Player player;
    Texture dropImage;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    ArrayList<Encounter> raindrops;
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
        dropImage = new Texture(Gdx.files.internal("droplet.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewPortWidth, viewPortHeight);

        // create the raindrops arraylist and spawn the first raindrop
        raindrops = new ArrayList<Encounter>();
        spawnRaindrop();

        //create playerprojectilearraylist
        playerProjectiles = new ArrayList<Projectile>();

    }

    private void spawnRaindrop() {
        Encounter raindrop = new Encounter(MathUtils.random(0, viewPortWidth - 64), viewPortHeight,
                64,64, 5, 5, "droplet.png");

        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    private void spawnBullet() {
        Projectile bullet = new Projectile((int)player.hitbox.x,(int)player.hitbox.y + 10,
                24, 36, 5, 400, "bullet.png");

        playerProjectiles.add(bullet);
        lastBulletTime = TimeUtils.nanoTime();
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

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            player.hitbox.x = touchPos.x - 64 / 2;

            // check if we need to create a new bullet
            if (TimeUtils.nanoTime() - lastBulletTime > 100000000)
                spawnBullet();
        }
//        if (Gdx.input.isKeyPressed(Keys.LEFT))
//            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
//        if (Gdx.input.isKeyPressed(Keys.RIGHT))
//            bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 200000000)
            spawnRaindrop();
        
        MoveAllObjects();

        // this is collisionchecking

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.

        for (int i = 0; i < raindrops.size(); i++) {
            Encounter raindrop = raindrops.get(i);
            if (raindrop.hitbox.y + 64 < 0) {
                raindrops.remove(i);
            } else if (raindrop.overlaps(player.hitbox)){
                dropsGathered++;
                dropSound.play();
                raindrop.collidesWith(player);
                player.collidesWith(raindrop);
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if (bullet.hitbox.y > viewPortHeight - 100) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(raindrop.hitbox)){
                    dropSound.play();
                    raindrop.getsDamage(bullet.damage);
                    playerProjectiles.remove(j);
                }
            }
            if (raindrop.isDestroyed()){
                raindrops.remove(i);
            }
        }

        // make sure the player stays within the screen bounds
        if (player.hitbox.x < 0)
            player.hitbox.x= 0;
        if (player.hitbox.x > viewPortWidth - 64)
            player.hitbox.x = viewPortWidth - 64;

        // DRAW ALL OBJECTS HERE
        game.batch.begin();
        game.font.draw(game.batch, "FPS: " + fps, 0, viewPortHeight - 30);
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, viewPortHeight);
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , viewPortHeight - 60);
        game.font.draw(game.batch, "Projectiles: " + playerProjectiles.size(), 0 , viewPortHeight - 90);
        game.font.draw(game.batch, "Raindrops: " + raindrops.size(), 0 , viewPortHeight - 120);
        game.batch.draw(player.playerImage, player.hitbox.getX(), player.hitbox.getY(), player.hitbox.getWidth(), player.hitbox.getHeight());
        for (Encounter raindrop : raindrops) {
            game.batch.draw(raindrop.encounterImage, raindrop.hitbox.x, raindrop.hitbox.y);
        }
        for (Projectile bullet : playerProjectiles) {
            game.batch.draw(bullet.bulletImage, bullet.hitbox.x, bullet.hitbox.y);
        }
        game.batch.end();
    }

    private void MoveAllObjects() {
        for (Projectile bullet : playerProjectiles){
            bullet.hitbox.y += bullet.speed * Gdx.graphics.getDeltaTime();
        }
        for (Encounter encounter : raindrops){
            encounter.hitbox.y -= 250 * Gdx.graphics.getDeltaTime();
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
        dropImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }
}
