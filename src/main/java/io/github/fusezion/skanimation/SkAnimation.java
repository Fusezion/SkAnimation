package io.github.fusezion.skanimation;

import ch.njol.skript.ServerPlatform;
import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import io.github.fusezion.skanimation.api.Animation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SkAnimation extends JavaPlugin {

    public static final Map<String, Animation> ANIMATION_MAP = new HashMap<>();
	private static final Version MINIMUM_MC_VERSION = new Version(1,19,4);
	private static final Version MINIMUM_SKRIPT_VERSION = new Version(2,7,3);
    private static SkAnimation instance = null;

    @Override
    public void onEnable() {

		PluginManager pluginManager = Bukkit.getPluginManager();
	    Plugin skriptPlugin = pluginManager.getPlugin("Skript");
		if (skriptPlugin == null || !skriptPlugin.isEnabled()) {
			disablePlugin("Skript is either disabled or not found");
			return;
		}
	    if (Skript.getVersion().isSmallerThan(MINIMUM_SKRIPT_VERSION)) {
		    disablePlugin("SkAnimation requires skript 2.7.3+ in order to run, however it's encouraged to run 2.8+");
		    return;
	    }
	    if (Skript.getMinecraftVersion().isSmallerThan(MINIMUM_MC_VERSION)) {
		    disablePlugin("SkAnimation will only work on paper version of 1.19.4+");
		    return;
	    }
	    if (!Skript.classExists("io.papermc.paper.math.Rotations")) {
		    disablePlugin("SkAnimation will only work on paper and paper forks, or you're running an older build of paper that doesn't have \"Rotations\" class.");
		    return;
	    }
	    if (!Skript.isAcceptRegistrations() || instance != null) {
			disablePlugin("It appears something is doing some voodoo magic and making the addon load later than it should, skript is no longer accepting registrations");
			return;
		}
	    instance = this;
	    try {
		    Skript.registerAddon(instance)
		            .loadClasses("io.github.fusezion.skanimation", "elements")
		            .setLanguageFileDirectory("lang");
	    } catch (IOException exception) {
			disablePlugin(exception.getCause() + "\n" + exception.getMessage());
	    }
	    // Plugin startup logic

    }

	private void disablePlugin(String reason) {
		getLogger().severe("Plugin disabling due to:\n" + reason);
		Bukkit.getPluginManager().disablePlugin(this);
	}

    public static SkAnimation getPlugin() {
        return instance;
    }

}
