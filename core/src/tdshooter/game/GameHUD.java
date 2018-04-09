package tdshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameHUD extends Stage
{
    private Player player;
    private ProgressBar healthBar;
    private Label scoreLabel;

    public GameHUD(Viewport viewport, Batch batch, Skin skin, Player player)
    {
        super(viewport, batch);
        this.player = player;

        Table table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        healthBar = new ProgressBar(0, player.getMaxHitpoints(), player.getMaxHitpoints() / 50.0f,
                                                false, skin);
        scoreLabel = new Label("Score " + player.getPoints(), skin);
        scoreLabel.setFontScale(2.0f);

        table.add(scoreLabel).left().top();
        table.add(healthBar).width(300.0f).right().top().expand();
        this.addActor(table);
    }

    public void update(float delta)
    {
        super.act(delta);
        healthBar.setRange(0, player.getMaxHitpoints());
        healthBar.setValue(player.getHitPoints());
        scoreLabel.setText("Score " + player.getPoints());
    }
}
