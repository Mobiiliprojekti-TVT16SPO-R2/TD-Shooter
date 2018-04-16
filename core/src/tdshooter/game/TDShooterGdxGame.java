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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class TDShooterGdxGame extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public BitmapFont fontSkin;
	public AssetManager assets;
//	private Texture texture;
	public final int VIEWPORTWIDTH = 720;
	public final int VIEWPORTHEIGHT = 1280;

	public void create() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Teko-Medium.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 24;
		font = generator.generateFont(parameter);// font size 12 pixels
		parameter.size = 50;
		fontSkin = generator.generateFont(parameter);
		generator.dispose();
//		texture = new Texture("Backgrounds/Loading_test.png");

		batch = new SpriteBatch();
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
		assets.load("powerups/PowerUp_Armor.png", Texture.class);
		assets.load("powerups/PowerUp_BulletAmountBoost_VERSION2.png", Texture.class);
		assets.load("powerups/PowerUp_BulletSpeed_VERSION2.png", Texture.class);
		assets.load("powerups/PowerUp_Currency.png", Texture.class);
		assets.load("powerups/PowerUp_FlightSpeed.png", Texture.class);
		assets.load("powerups/PowerUp_HealthPack.png", Texture.class);
		assets.load("Backgrounds/Map_Test_720_2297_2.png", Texture.class);
		assets.load("Backgrounds/Map_Test_1_original.png", Texture.class);
		assets.load("Backgrounds/Map_1_Forest_VER3_720.png", Texture.class);
		assets.load("Backgrounds/Map_1_Forest_VER3.png", Texture.class);
		assets.load("Backgrounds/Map_2_FarmLand.png", Texture.class);
		assets.load("Backgrounds/Map_3_Islands.png", Texture.class);
		assets.load("Menu/Background_BaseMenu_720_1280.png", Texture.class);
		assets.load("Menu/Background_StartMenu.png", Texture.class);
		assets.load("Menu/Character_Commander.png", Texture.class);
		assets.load("Menu/Character_QuarterMaster.png", Texture.class);
		assets.load("Menu/Character_Scientist.png", Texture.class);
		assets.load("Menu/valikko-ylapalkki.png", Texture.class);
		assets.load("Menu/valikko-puhekupla_Vaalennettu.png", Texture.class);
		assets.load("Menu/Bridge_Screen_Map.png", Texture.class);
		assets.load("Menu/Bridge_Screen_Map_with_lines.png", Texture.class);
		assets.load("Menu/hangarvalikkoboksi.png", Texture.class);
		assets.load("Menu/keskimmainen-normaali.png", Texture.class);
		assets.load("Menu/keskimmainen-valittu.png", Texture.class);
		assets.load("Menu/oikea-normaali.png", Texture.class);
		assets.load("Menu/oikea-valittu.png", Texture.class);
		assets.load("Menu/vasen-normaali.png", Texture.class);
		assets.load("Menu/vasen-valittu.png", Texture.class);
		assets.load("Menu/ylapalkki2.png", Texture.class);
		assets.load("Menu/puhekupla.png", Texture.class);
		assets.load("testistausta.png", Texture.class);
		assets.load("menu_test.png", Texture.class);
		assets.load("effects/flash_test.png", Texture.class);

		assets.load("Skin/glassy-ui.atlas", TextureAtlas.class);
//		assets.load("Skin/glassy-ui.json", Skin.class, new SkinLoader.SkinParameter("Skin/glassy-ui.atlas"));

		assets.load("hitSound.wav", Sound.class);

		assets.load("rain.mp3", Music.class);

		assets.finishLoading();

		// Luodaan pelin eka ase, jos sitä ei vielä ole
		Preferences prefs = Gdx.app.getPreferences("savedata");
		if(prefs.contains("weapon01") == false) {
			prefs.putInteger("weapon01", 1);
			prefs.flush();
		}
		// luodaan kassapohja pelaajalle
		if(prefs.contains("currency") == false) {
			prefs.putInteger("currency", 0);
			prefs.flush();
		}
		// luodaan tallennustilanne ( alkaen kentästä 1)
		if(prefs.contains("levelprogress") == false) {
			prefs.putInteger("levelprogress", 1);
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
		fontSkin.dispose();
		assets.dispose();
	}
}
