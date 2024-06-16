package io.github.fusezion.skanimation.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.github.fusezion.skanimation.api.Animation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Apply Animation")
@Description("Applies a previously created animation onto an existing armor stand entity")
@Examples("animate target entity of player with animation named \"waving\"")
@Since("1.0.0")
public class EffApplyAnimation extends Effect {

	static {
		Skript.registerEffect(EffApplyAnimation.class, "animate %livingentities% (using|with) [animation] %animation%");
	}

	private Expression<LivingEntity> animationEntities;
	private Expression<Animation> animation;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.animationEntities = (Expression<LivingEntity>) expressions[0];
		this.animation = (Expression<Animation>) expressions[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Animation animation = this.animation.getSingle(event);
		if (animation == null) return;
		animationEntities.stream(event)
				.filter(entity -> entity instanceof ArmorStand)
				.map(entity -> (ArmorStand) entity)
				.forEach(animation::animate);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return String.format("animate %s using %s",
				this.animationEntities.toString(event, debug),
				this.animation.toString(event, debug));
	}

}
