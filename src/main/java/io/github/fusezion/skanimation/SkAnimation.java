package io.github.fusezion.skanimation;

import ch.njol.skript.Skript;
import io.github.fusezion.skanimation.api.Animation;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SkAnimation extends JavaPlugin {

    public static final Map<String, Animation> ANIMATION_MAP = new HashMap<>();
    private static SkAnimation instance;

    @Override
    public void onEnable() {
        instance = this;
	    try {
		    Skript.registerAddon(this)
		            .loadClasses("io.github.fusezion.skanimation", "elements")
		            .setLanguageFileDirectory("lang");
	    } catch (IOException e) {
		    throw new RuntimeException(e);
	    }
	    // Plugin startup logic

    }

    public static SkAnimation getPlugin() {
        return instance;
    }

}
