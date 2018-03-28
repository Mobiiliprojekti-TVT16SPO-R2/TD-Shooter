package tdshooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TDShooterGdxGame extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public AssetManager assets;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
        font.getData().setScale(1.5f,1.5f);
        assets = new AssetManager();

        assets.load("planes/Player_FighterPlane.png", Texture.class);
        assets.load("Encounters/AlienBeast_LVL_1_Test.png", Texture.class);
        assets.load("Encounters/AlienFighter_LVL_1_Test.png", Texture.class);
        assets.load("Encounters/AlienFighter_LVL_2.png", Texture.class);
        assets.load("Bullets/bullet1_small.png", Texture.class);
        assets.load("Bullets/bullet1.png", Texture.class);
        assets.load("Bullets/alien_bullet_test.png", Texture.class);
        assets.load("Backgrounds/Map_Test_720_2297_2.png", Texture.class);
        assets.load("testistausta.png", Texture.class);

		assets.finishLoading();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); //important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		assets.dispose();
	}
}
