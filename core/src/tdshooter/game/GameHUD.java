package tdshooter.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameHUD extends Stage
{
    private Player player;
    private ProgressBar healthBar;
    private Label scoreLabel;
    private TextButton superWeaponButton;
    private TextButton changeWeaponButton;
    private boolean superWeaponClicked = false;

    public GameHUD(Viewport viewport, Batch batch, Skin skin, final Player player)
    {
        super(viewport, batch);
        this.player = player;

        Table table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        superWeaponButton = new TextButton("Nuke", skin);
        superWeaponButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int i = player.getSuperWeaponInt();
                if (i > 0) {
                    superWeaponClicked = true;
                    player.setSuperWeaponInt();
                }
            }
        });
        changeWeaponButton = new TextButton("Switch", skin);
        changeWeaponButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    player.swapWeapons();
            }
        });
        healthBar = new ProgressBar(0, player.getMaxHitpoints(), player.getMaxHitpoints() / 50.0f,
                                                false, skin);
        scoreLabel = new Label("Score " + player.getPoints(), skin);
        scoreLabel.setFontScale(0.5f);

        table.add(scoreLabel).left().top();
        table.add(healthBar).width(300.0f).right().top().expand();
        table.row();
        table.add(superWeaponButton).width(200).bottom();
        table.add(changeWeaponButton).width(200).bottom().right();
        this.addActor(table);
    }

    public void update(float delta)
    {
        super.act(delta);
        healthBar.setRange(0, player.getMaxHitpoints());
        healthBar.setValue(player.getHitPoints());
        scoreLabel.setText("Score " + player.getPoints());
    }

    public boolean isSuperWeaponClicked() {
        return superWeaponClicked;
    }

    public void setSuperWeaponClicked() {
        superWeaponClicked = false;
    }
}
