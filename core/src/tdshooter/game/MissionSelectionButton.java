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

        lockedButtonTexture = game.assets.get("mapitems/karttapainike-locked.png");
        unlockedButtonTexture = game.assets.get("mapitems/karttapainike-unlocked.png");
        selectedButtonTexture = game.assets.get("mapitems/karttapainike-selected.png");

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

    public void setPosition(int x, int y){
        lockedButtonImage.setPosition(x,y);
        unlockedButtonImage.setPosition(x,y);
        selectedButtonImage.setPosition(x,y);
    }

    public void setSize(int diameter){
        lockedButtonImage.setWidth(diameter);
        unlockedButtonImage.setWidth(diameter);
        selectedButtonImage.setWidth(diameter);
        lockedButtonImage.setHeight(diameter);
        unlockedButtonImage.setHeight(diameter);
        selectedButtonImage.setHeight(diameter);
    }

    public void setLocked(){
        lockedButtonImage.setVisible(true);
    }
    public void setUnlocked(){
        lockedButtonImage.setVisible(false);
        selectedButtonImage.setVisible(false);
    }
    public void setSelected(){
        selectedButtonImage.setVisible(true);
    }
    public void setHidden(){
        selectedButtonImage.setVisible(false);
        lockedButtonImage.setVisible(false);
        unlockedButtonImage.setVisible(false);
    }

}
