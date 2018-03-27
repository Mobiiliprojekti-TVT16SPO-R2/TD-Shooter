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
		assets = new AssetManager();

		assets.load("Encounters/AlienBeast_Test_1_small.png", Texture.class);
		assets.load("Encounters/AlienFighter_Test_1_small.png", Texture.class);
		assets.load("Bullets/bullet1_small.png", Texture.class);
		assets.load("testistausta.png", Texture.class);
		assets.load("hitSound.wav", Sound.class);
		assets.load("rain.mp3", Music.class);

		assets.finishLoading();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render()
	{
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		assets.dispose();
	}
}
