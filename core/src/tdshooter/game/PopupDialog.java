package tdshooter.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PopupDialog extends Stage {

    private Label textLabel;
    private Texture dialogTexture;
    private Image dialogImage;
    private float padding = 20;
    private float margin = 10;
    private float dialogWidth = 720;
    private float dialogHeight = 400;

    public PopupDialog(Viewport viewport, Batch batch, Skin skin, AssetManager assets) {

         super(viewport, batch);

        Table table = new Table();
            table.setDebug(true);
            table.setFillParent(true);

        BitmapFont font = skin.getFont("font");
        font.getData().setLineHeight(font.getLineHeight() - font.getSpaceWidth() + 5);
        Label.LabelStyle myLabelstyle =
                new Label.LabelStyle(font, Color.BLACK);

        dialogTexture = assets.get("Menu/valikko-puhekupla_Vaalennettu.png");
        dialogImage = new Image(dialogTexture);
        textLabel = new Label("no text", myLabelstyle);
        textLabel.setFontScale(0.9f);
        textLabel.setWidth(dialogWidth - margin - padding);
        textLabel.setHeight(dialogHeight - margin - padding);
        textLabel.setWrap(true);

        this.addActor(dialogImage);
        this.addActor(textLabel);
        setPosition(0,0);
    }

    public void update(float delta)
    {
        super.act(delta);
    }

    public void setText(String message) {
        textLabel.setText(message);
        textLabel.setPosition(margin+padding, (((dialogHeight*0.65f)/2) -  (textLabel.getPrefHeight()*0.5f)) );
    }

    public void setPosition(float x, float y) {
        dialogImage.setPosition(x + margin,y + margin);
        textLabel.setPosition(x + padding + margin,y + padding + margin);
    }

    public void setSize(float width, float height) {
        dialogHeight = height;
        dialogWidth = width;
        dialogImage.setWidth(width - (margin*2));
        dialogImage.setHeight(height - (margin*2));
        textLabel.setWidth(width - ((padding+margin)*2));
        textLabel.setHeight(height - ((padding+margin)*2));
    }
}
