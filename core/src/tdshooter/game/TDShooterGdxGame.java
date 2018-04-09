package tdshooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TDShooterGdxGame extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public AssetManager assets;
	public final int VIEWPORTWIDTH = 720;
	public final int VIEWPORTHEIGHT = 1280;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
        font.getData().setScale(1.5f,1.5f);
        assets = new AssetManager();

		assets.load("planes/Player_FighterPlane.png", Texture.class);
		assets.load("Encounters/AlienBeast_LVL_1_Test.png", Texture.class);
		assets.load("Encounters/AlienBeast_LVL_2.png", Texture.class);
		assets.load("Encounters/AlienBeast_LVL_3.png", Texture.class);
		assets.load("Encounters/AlienBeast_BigMomma.png", Texture.class);
		assets.load("Encounters/AlienFighter_LVL_1_Test.png", Texture.class);
		assets.load("Encounters/AlienFighter_LVL_2_VERSION2.png", Texture.class);
		assets.load("Encounters/AlienFighter_LVL_3_VERSION2.png", Texture.class);
		assets.load("Encounters/AlienFighter_Bruiser.png", Texture.class);
		assets.load("Encounters/Alien_Mine.png", Texture.class);
		assets.load("Encounters/AlienMine_Machine.png", Texture.class);
		assets.load("Encounters/AlienMine_Organic.png", Texture.class);
		assets.load("Bullets/bullet1_small.png", Texture.class);
		assets.load("Bullets/bullet1.png", Texture.class);
		assets.load("Bullets/alien_bullet_test.png", Texture.class);
		assets.load("Bullets/alien_bullet_test.png", Texture.class);
        assets.load("Items/armor_test.png", Texture.class);
		assets.load("Items/currency_test.png", Texture.class);
		assets.load("Items/flightspeed_test.png", Texture.class);
		assets.load("Items/healthpack_test.png", Texture.class);
		assets.load("powerups/PowerUp_Armor.png", Texture.class);
		assets.load("powerups/PowerUp_BulletAmountBoost_VERSION2.png", Texture.class);
		assets.load("powerups/PowerUp_BulletSpeed_VERSION2.png", Texture.class);
		assets.load("powerups/PowerUp_Currency.png", Texture.class);
		assets.load("powerups/PowerUp_FlightSpeed.png", Texture.class);
		assets.load("powerups/PowerUp_HealthPack.png", Texture.class);
		assets.load("Backgrounds/Map_Test_720_2297_2.png", Texture.class);
		assets.load("Backgrounds/Map_1_Forest_VER3_720.png", Texture.class);
		assets.load("Menu/Background_BaseMenu_720_1280.png", Texture.class);
		assets.load("Menu/Character_Commander.png", Texture.class);
		assets.load("Menu/Character_QuarterMaster.png", Texture.class);
		assets.load("Menu/Character_Scientist.png", Texture.class);
		assets.load("testistausta.png", Texture.class);
		assets.load("menu_test.png", Texture.class);


		assets.load("Skin/glassy-ui.atlas", TextureAtlas.class);
		assets.load("Skin/glassy-ui.json", Skin.class, new SkinLoader.SkinParameter("Skin/glassy-ui.atlas"));

		assets.load("hitSound.wav", Sound.class);

		assets.load("rain.mp3", Music.class);

		assets.finishLoading();

		// Luodaan pelin eka ase, jos sitä ei vielä ole
		Preferences prefs = Gdx.app.getPreferences("savedata");
		if(prefs.contains("weapon01") == false) {
			prefs.putInteger("weapon01", 1);
			prefs.flush();
		}

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
