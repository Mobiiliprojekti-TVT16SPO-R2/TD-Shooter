package tdshooter.game;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
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
    private final int PLAYERSIZE_X = 64;
    private final int PLAYERSIZE_Y = 64;
    private final int FLIGHTZONE_X_MIN = 32; //(PLAYERSIZE_X / 2);
    private final int FLIGHTZONE_X_MAX = 448; //(VIEWPORTWIDTH - (PLAYERSIZE_X / 2));
    private final int FLIGHTZONE_Y_MIN = 64;
    private final int FLIGHTZONE_Y_MAX = 300;  //((VIEWPORTHEIGHT / 4) + 100);

    private float background_y = 0;
    private int scrollSpeed = 100;

    private float x_input;
    private float y_input;

    Hashtable<String, Texture> textures;
    Hashtable<String, Sound> sounds;
    Hashtable<String, Music> musics;

    Player player;
    OrthographicCamera camera;
    ArrayList<Encounter> encounters;
    ArrayList<Projectile> playerProjectiles;

    long lastDropTime;
    long lastBulletTime;
    ArrayList<Long> oldSoundIds;

    //adding FPS-counter
    private int fps;
    private float touchPos_x;
    private float touchPos_y;

    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        player = new Player(VIEWPORTWIDTH / 2 - 64 / 2,20, PLAYERSIZE_X , PLAYERSIZE_Y, 100,50);

        textures = new Hashtable<String, Texture>();
        sounds = new Hashtable<String, Sound>();
        musics = new Hashtable<String, Music>();
        oldSoundIds = new ArrayList<Long>();

        textures.put("enemy01", new Texture(Gdx.files.internal("Encounters/AlienBeast_Test_1_small.png")));
        textures.put("playerBullet01", new Texture(Gdx.files.internal("Bullets/bullet1_small.png")));
        textures.put("background01", new Texture(Gdx.files.internal("testistausta.png")));

        sounds.put("hitSound01", Gdx.audio.newSound(Gdx.files.internal("hitSound.wav")));

        musics.put("music01", Gdx.audio.newMusic(Gdx.files.internal("rain.mp3")));

        Enumeration<String> keys = musics.keys();
        while(keys.hasMoreElements())
        {
            musics.get(keys.nextElement()).setLooping(true);
        }

        //Play sound Effects once, to initialize prev_sound_id
        oldSoundIds.add(sounds.get("hitSound01").play(0.0f));
        oldSoundIds.add(sounds.get("hitSound01").play(0.0f));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORTWIDTH, VIEWPORTHEIGHT);

        encounters = new ArrayList<Encounter>();
        spawnRaindrop();

        playerProjectiles = new ArrayList<Projectile>();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        fps = Gdx.graphics.getFramesPerSecond();

        processUserInput();

        if (player.isShooting()){
            if (TimeUtils.nanoTime() - lastBulletTime > 70000000)
                spawnBullet();
        }

        if (TimeUtils.nanoTime() - lastDropTime > 200000000)
            spawnRaindrop();
        
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


    private void spawnRaindrop() {
        Encounter raindrop = new Encounter(MathUtils.random(128, VIEWPORTWIDTH - 64), VIEWPORTHEIGHT,
                64,64, 220, 1, textures.get("enemy01"));

        encounters.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    private void spawnBullet() {   //Bullet positions with these images and hitboxes,  x: 24= mid, 0 and 48 wing edges
        Projectile bullet = new Projectile((int)player.hitbox.x + 24,(int)player.hitbox.y + 48,
                24, 36, 5, 800, textures.get("playerBullet01"));
        playerProjectiles.add(bullet);

        Projectile bullet2 = new Projectile((int)player.hitbox.x ,(int)player.hitbox.y + 32,
                24, 36, 5, 800, textures.get("playerBullet01"));
        playerProjectiles.add(bullet2);

        Projectile bullet3 = new Projectile((int)player.hitbox.x + 48,(int)player.hitbox.y + 32,
                24, 36, 5, 800, textures.get("playerBullet01"));
        playerProjectiles.add(bullet3);

        Projectile bullet4 = new Projectile((int)player.hitbox.x + 16,(int)player.hitbox.y + 40,
                24, 36, 5, 800, textures.get("playerBullet01"));
        playerProjectiles.add(bullet4);

        Projectile bullet5 = new Projectile((int)player.hitbox.x + 32,(int)player.hitbox.y + 40,
                24, 36, 5, 800, textures.get("playerBullet01"));
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
                sounds.get("hitSound01").stop(oldSoundIds.get(0)); //stop oldest
                oldSoundIds.remove(0); // remove oldest
                oldSoundIds.add(sounds.get("hitSound01").play()); // play and add new
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if (bullet.hitbox.y > VIEWPORTHEIGHT + 64) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter)){
                    sounds.get("hitSound01").stop(oldSoundIds.get(0)); //stop oldest
                    oldSoundIds.remove(0); // remove oldest
                    oldSoundIds.add(sounds.get("hitSound01").play()); // play and add new
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

    private void drawAllObjects() {
        // DRAW ALL OBJECTS HERE
        game.batch.begin();
        game.batch.draw(textures.get("background01"), 0 , background_y);
        if (background_y < -2400) {
            game.batch.draw(textures.get("background01"), 0, background_y + 3200);
        }
        game.font.draw(game.batch, "FPS: " + fps, 0, VIEWPORTHEIGHT - 30);
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , VIEWPORTHEIGHT - 60);
        game.font.draw(game.batch, "Projectiles: " + playerProjectiles.size(), 0 , VIEWPORTHEIGHT - 90);
        game.font.draw(game.batch, "Encounters: " + encounters.size(), 0 , VIEWPORTHEIGHT - 120);
        player.draw(game.batch);
        for (Encounter encounter : encounters) {
            encounter.draw(game.batch);
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
        musics.get("music01").play();
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
        disposeAssetTable(textures);
        disposeAssetTable(sounds);
        disposeAssetTable(musics);
    }

    private <T> void disposeAssetTable(Hashtable<String, T> table)
    {
        Enumeration<String> keys = table.keys();
        while(keys.hasMoreElements())
        {
            Disposable asset = (Disposable)table.get(keys.nextElement());
            if(asset != null)
            {
                asset.dispose();
            }
        }
    }
}
