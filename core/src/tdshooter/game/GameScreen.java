package tdshooter.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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

public class GameScreen implements Screen, InputProcessor {
    final TDShooterGdxGame game;
    private final int viewPortHeight = 800;
    private final int viewPortWidth = 480;
    private boolean shooting = false;
    private float background_y = 0;
    private int scrollSpeed = 100;
    private long prevEnemyTime;
    private long lastBulletTime;
    private int enemiesDestroyed;

    private Player player;
    private Texture basicEnemy;
    private Texture basicMine;
    private Texture bulletImage;
    private Texture background;
    private Texture background_2;
    private Sound shootSound;
    private Sound hitSound;
    private OrthographicCamera camera;
    private ArrayList<Encounter> encounters;
    private ArrayList<Projectile> playerProjectiles;

    public GameScreen(final TDShooterGdxGame game) {
        this.game = game;
        player = new Player(viewPortWidth / 2 - 64 / 2,20, 96 , 96, 100,50);
        basicEnemy = new Texture(Gdx.files.internal("Encounters/AlienBeast_LVL_1.png"));
        basicMine = new Texture(Gdx.files.internal("Encounters/Alien_Mine.png"));
        bulletImage = new Texture(Gdx.files.internal("Bullets/bullet1_small.png"));
        background = new Texture(Gdx.files.internal("testistausta.png"));
        background_2 = background;

        // load the sound effects
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hitSound.wav"));

        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewPortWidth, viewPortHeight);

        // create the encounters arraylist
        encounters = new ArrayList<Encounter>();

        //create playerprojectilearraylist
        playerProjectiles = new ArrayList<Projectile>();

        // set inputProcessor for this screen
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        processUserInput();
        limitPlayerMovement();
        spawnObjects();
        moveAllObjects();
        checkCollisions();
        drawAllObjects();
    }

    private void spawnObjects() {
        // check if we need to create a new bullet
        if (shooting){
            if (TimeUtils.nanoTime() - lastBulletTime > 90000000)
                spawnBullets();
        }
        // check if we need to create a new enemy
        if (TimeUtils.nanoTime() - prevEnemyTime > 500000000)
            spawnEnemy();
    }

    private void processUserInput() {
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            player.setDestination(touchPos);
            player.setMoving(true);
            shooting = true;
        } else {
            player.setMoving(false);
            shooting = false;
        }
    }

    private void spawnEnemy() {
        int spawnLocation = MathUtils.random(0, viewPortWidth - 64);
        Encounter encounter = new Encounter(spawnLocation, viewPortHeight,
                64,64, 50, 10, basicEnemy);
        encounters.add(encounter);
        prevEnemyTime = TimeUtils.nanoTime();  // set timer again
    }

    private void spawnBullets() {   //Bullet positions with these images and hitboxes,  x: 48= mid, 0 and 96 wing edges
        Projectile bullet = new Projectile((int)(player.hitbox.x - 8 + 48 - 16) ,(int)player.hitbox.y + 32,
                16, 48, 5, 1100, bulletImage);
        playerProjectiles.add(bullet);
        Projectile bullet2 = new Projectile((int)(player.hitbox.x - 8 + 48 + 16),(int)player.hitbox.y + 32,
                16, 48, 5, 1100, bulletImage);
        playerProjectiles.add(bullet2);
        shootSound.play();
        lastBulletTime = TimeUtils.nanoTime();
    }

    private void checkCollisions() {
        for (int i = 0; i < encounters.size(); i++) {
            Encounter encounter = encounters.get(i);
            boolean score_given = false;
            if (encounter.hitbox.y + 64 < 0) {
                encounter.getsDamage(1000);
            } else if (encounter.overlaps(player)){
                encounter.collidesWith(player);
                player.collidesWith(encounter);
                hitSound.play();
            }
            for (int j = 0; j < playerProjectiles.size(); j++) {
                Projectile bullet = playerProjectiles.get(j);
                if (bullet.hitbox.y > viewPortHeight + 64) {
                    playerProjectiles.remove(j);
                } else if (bullet.overlaps(encounter)){
                    hitSound.play();
                    encounter.getsDamage(bullet.damage);
                    playerProjectiles.remove(j);
                    if ((encounter.isDestroyed()) && (score_given == false) ){
                        enemiesDestroyed++;
                        score_given = true;
                    }
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
        game.setScreen(new EndGameScreen(game, enemiesDestroyed));
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
        game.batch.begin();
        game.batch.draw(background, 0 , background_y);
        if (background_y < -2400) {
            game.batch.draw(background_2, 0, background_y + 3200);
        }
        game.font.draw(game.batch, "Player HP: " + player.getHitPoints(), 0 , viewPortHeight - 60);
        game.font.draw(game.batch, "EnemiesDestroyed: " + enemiesDestroyed, 0 , viewPortHeight - 90);
        game.batch.draw(player.playerImage, player.hitbox.getX(), player.hitbox.getY(), player.hitbox.getWidth(), player.hitbox.getHeight());

        for (Encounter encounter : encounters) {
            game.batch.draw(encounter.encounterImage, encounter.hitbox.x, encounter.hitbox.y, encounter.hitbox.width, encounter.hitbox.height);
        }
        for (Projectile bullet : playerProjectiles) {
            game.batch.draw(bullet.bulletImage, bullet.hitbox.x, bullet.hitbox.y, bullet.hitbox.width, bullet.hitbox.height);
        }
        game.batch.end();
    }

    private void moveAllObjects() {
        player.move(Gdx.graphics.getDeltaTime());
        for (Projectile bullet : playerProjectiles){
            bullet.hitbox.y += bullet.speed * Gdx.graphics.getDeltaTime();
        }
        for (Encounter encounter : encounters){
            encounter.hitbox.y -= 220 * Gdx.graphics.getDeltaTime();
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
        basicMine.dispose();
        bulletImage.dispose();
        background_2.dispose();
        background.dispose();
        shootSound.dispose();
        hitSound.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
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
