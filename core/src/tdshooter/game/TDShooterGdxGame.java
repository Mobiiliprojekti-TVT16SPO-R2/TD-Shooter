package tdshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TDShooterGdxGame extends ApplicationAdapter {

    private BitmapFont fpsFont;
	private SpriteBatch batch;
	private Texture img;
	private Texture testi43;
	private int i = 0;
    private int fps = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		fpsFont = new BitmapFont();
		fpsFont.getData().setScale(4,4);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		fps = Gdx.graphics.getFramesPerSecond();
		batch.begin();
		fpsFont.draw(batch, "" + fps, 10, 1070);
		batch.draw(img, 0, 0);
		batch.draw(img, 400, 400);
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
