package io.github.fusezion.skanimation.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.github.fusezion.skanimation.SkAnimation;
import io.github.fusezion.skanimation.api.Animation;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("All Animations")
@Description("Retrieve all the animations or animation keys")
@Examples("set {_animationNames::*} to all animation keys")
@Since("INSERT NAME")
public class ExprAllAnimations extends SimpleExpression<Object> {

	static {
		Skript.registerExpression(ExprAllAnimations.class, Object.class, ExpressionType.SIMPLE,
				"all [[of] the] animations",
				"all [[of] the] animation keys");
	}

	private boolean retrieveKeys;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.retrieveKeys = matchedPattern == 1;
		return true;
	}

	@Override
	protected @Nullable Object[] get(Event event) {
		if (retrieveKeys) {
			SkAnimation.ANIMATION_MAP.keySet().toArray(String[]::new);
		}
		return SkAnimation.ANIMATION_MAP.values().toArray(Animation[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<?> getReturnType() {
		return this.retrieveKeys ? String.class : Animation.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "all aniumations" + (retrieveKeys ? " keys" : "s");
	}
}
