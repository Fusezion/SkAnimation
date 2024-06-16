package io.github.fusezion.skanimation.api;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AnimationEvent extends Event {

	private final ArmorStand armorStand;

	public AnimationEvent(ArmorStand armorStand) {
		this.armorStand = armorStand;
	}

	public ArmorStand getArmorStand() {
		return this.armorStand;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		throw new IllegalStateException("This method isn't suppose to ever be called");
	}

}
