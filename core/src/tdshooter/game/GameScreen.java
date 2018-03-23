package tdshooter.game;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.badlogic.gdx.Application;
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

    Player player;
    ScrollingBackground background;
    OrthographicCamera camera;
    ArrayList<Encounter> encounters;
    ArrayList<Projectile> playerProjectiles;
    Mission mission;
    
    AssetContainer assets;

    long lastDropTime;
    long lastBulletTime;
    ArrayList<Long> oldSoundIds;

    //adding FPS-counter
    private int fps;

    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        encounters = new ArrayList<Encounter>();
        playerProjectiles = new ArrayList<Projectile>();
        assets = new AssetContainer();

        oldSoundIds = new ArrayList<Long>();

        assets.textures.put("enemy01", new Texture(Gdx.files.internal("Encounters/AlienBeast_Test_1_small.png")));
        assets.textures.put("playerBullet01", new Texture(Gdx.files.internal("Bullets/bullet1_small.png")));
        assets.textures.put("background01", new Texture(Gdx.files.internal("testistausta.png")));
        assets.sounds.put("hitSound01", Gdx.audio.newSound(Gdx.files.internal("hitSound.wav")));
        assets.musics.put("music01", Gdx.audio.newMusic(Gdx.files.internal("rain.mp3")));

        mission = new Mission("Missions/mission01.txt", assets, encounters);
        player = new Player(VIEWPORTWIDTH / 2 - 64 / 2,20, PLAYERSIZE_X , PLAYERSIZE_Y, 100,50);
        background = new ScrollingBackground(mission.getBackground());
        background.setLooping(mission.isBackgroundLooping());

        Enumeration<String> keys = assets.musics.keys();
        while(keys.hasMoreElements())
        {
            assets.musics.get(keys.nextElement()).setLooping(true);
        }

        //Play sound Effects once, to initialize prev_sound_id
        oldSoundIds.add(assets.sounds.get("hitSound01").play(0.0f));
        oldSoundIds.add(assets.sounds.get("hitSound01").play(0.0f));

        camera.setToOrtho(false, VIEWPORTWIDTH, VIEWPORTHEIGHT);
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

        if(mission.isMissionOver())
        {
            background.setLooping(false);
        }
        
        moveAllObjects(delta);
        checkCollisions();
        mission.update(delta);
        drawAllObjects();
    }

    private void processUserInput()
    {
        if(Gdx.input.isTouched())
        {
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
            player.setShooting(true);
        }
        else
        {
            player.setMoving(false);
            player.setShooting(false);
        }
    }

    private void spawnBullet() {   //Bullet positions with these images and hitboxes,  x: 24= mid, 0 and 48 wing edges
        Projectile bullet = new Projectile((int)player.hitbox.x + 24,(int)player.hitbox.y + 48,
                24, 36, 5, 800, assets.textures.get("playerBullet01"));
        playerProjectiles.add(bullet);

        Projectile bullet2 = new Projectile((int)player.hitbox.x ,(int)player.hitbox.y + 32,
                24, 36, 5, 800, assets.textures.get("playerBullet01"));
        playerProjectiles.add(bullet2);

        Projectile bullet3 = new Projectile((int)player.hitbox.x + 48,(int)player.hitbox.y + 32,
                24, 36, 5, 800, assets.textures.get("playerBullet01"));
        playerProjectiles.add(bullet3);

        Projectile bullet4 = new Projectile((int)player.hitbox.x + 16,(int)player.hitbox.y + 40,
                24, 36, 5, 800, assets.textures.get("playerBullet01"));
        playerProjectiles.add(bullet4);

        Projectile bullet5 = new Projectile((int)player.hitbox.x + 32,(int)player.hitbox.y + 40,
                24, 36, 5, 800, assets.textures.get("playerBullet01"));
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
                assets.sounds.get("hitSound01").stop(oldSoundIds.get(0)); //stop oldest
                oldSoundIds.remove(0); // remove oldest
                oldSoundIds.add(assets.sounds.get("hitSound01").play()); // play and add new
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if (bullet.hitbox.y > VIEWPORTHEIGHT + 64) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter)){
                    assets.sounds.get("hitSound01").stop(oldSoundIds.get(0)); //stop oldest
                    oldSoundIds.remove(0); // remove oldest
                    oldSoundIds.add(assets.sounds.get("hitSound01").play()); // play and add new
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

    private void drawAllObjects()
    {
        game.batch.begin();
        background.draw(game.batch);
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

    private void moveAllObjects(float delta)
    {
        background.update(delta);
        player.move(delta);
        for (Projectile bullet : playerProjectiles){
            bullet.hitbox.y += bullet.speed * delta;
        }
        for (Encounter encounter : encounters){
            encounter.hitbox.y -= 150 * delta;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        assets.musics.get("music01").play();
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
        disposeAssetTable(assets.textures);
        disposeAssetTable(assets.sounds);
        disposeAssetTable(assets.musics);
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
