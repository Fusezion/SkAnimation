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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Animation from id")
@Description("Get an animation from an id that it was saved to")
@Examples("set {_wavingAnimation} to animation named \"waving\"")
@Since("1.0.0")
public class ExprAnimation extends SimpleExpression<Animation> {

	static {
		Skript.registerExpression(ExprAnimation.class, Animation.class, ExpressionType.SIMPLE, "animation [named|with (id|name)] %string%");
	}

	private Expression<String> animationName;

	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.animationName = (Expression<String>) expressions[0];
		return true;
	}

	@SuppressWarnings({"NullableProblems"})
	@Override
	protected @Nullable Animation[] get(Event event) {
		String animationName = this.animationName.getSingle(event);
		if (animationName == null || animationName.isBlank()) return null;
		Animation animation = SkAnimation.ANIMATION_MAP.get(animationName);
		if (animation == null) return null;
		return new Animation[]{animation};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends Animation> getReturnType() {
		return Animation.class;
	}

	@Override
	public @NotNull String toString(@Nullable Event event, boolean debug) {
		return "animation with id " + this.animationName.toString(event, debug);
	}
}
