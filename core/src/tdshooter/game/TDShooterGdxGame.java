package tdshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TDShooterGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture testi;
	BitmapFont font;
	int i = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		testi = new Texture("badlogic.jpg");
		font = new BitmapFont();
		font.getData().setScale(4,4);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, "" + i, 1000, 1000);
		batch.draw(img, 0, 0);
		batch.draw(testi, 400, 400);
		batch.end();
		i = i + 1;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
