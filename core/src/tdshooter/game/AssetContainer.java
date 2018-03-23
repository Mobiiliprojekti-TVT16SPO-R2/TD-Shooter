package tdshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.Hashtable;

/**
 * Created by Samuli Lehto on 22.3.2018.
 */

public class AssetContainer
{

    public Hashtable<String, Texture> textures;
    public Hashtable<String, Sound> sounds;
    public Hashtable<String, Music> musics;

    public AssetContainer()
    {
        textures = new Hashtable<String, Texture>();
        sounds = new Hashtable<String, Sound>();
        musics = new Hashtable<String, Music>();
    }
}
