package tdshooter.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MissionSelectionButton extends Stage {

    private TDShooterGdxGame game;
    private Texture lockedButtonTexture;
    private Texture unlockedButtonTexture;
    private Texture selectedButtonTexture;
    private Image lockedButtonImage;
    private Image unlockedButtonImage;
    private Image selectedButtonImage;

    public MissionSelectionButton(Viewport viewport, TDShooterGdxGame gam) {
        super(viewport, gam.batch);
        game = gam;

        lockedButtonTexture = game.assets.get("mapitems/karttapainike-locked.png.png");
        unlockedButtonTexture = game.assets.get("mapitems/karttapainike-selected.png.png");
        selectedButtonTexture = game.assets.get("mapitems/karttapainike-unlocked.png.png");

        lockedButtonImage = new Image(lockedButtonTexture);
        unlockedButtonImage = new Image(unlockedButtonTexture);
        selectedButtonImage = new Image(selectedButtonTexture);

        this.addActor(unlockedButtonImage);
        this.addActor(selectedButtonImage);
        selectedButtonImage.setVisible(false);
        this.addActor(lockedButtonImage);
    }

    public void update(float delta) {
        super.act(delta);
    }

    public void setLocked(){
        lockedButtonImage.setVisible(true);
    }
    public void setUnlocked(){
        lockedButtonImage.setVisible(false);
    }
    public void setSelected(){
        selectedButtonImage.setVisible(true);
    }
}
