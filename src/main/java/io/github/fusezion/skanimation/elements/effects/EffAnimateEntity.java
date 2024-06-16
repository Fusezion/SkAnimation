package io.github.fusezion.skanimation.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import io.github.fusezion.skanimation.api.Animation.AnimationPose;
import io.github.fusezion.skanimation.api.ArmorStandPose;
import io.papermc.paper.math.Rotations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Animate ArmorStand")
@Description("Animation an armor stand pose to match a rotation with duration of time to animate, optionally from zero")
@Examples({
		"animate left arm pose of clicked entity using rotation(-120, -45, 0) with duration 0.5 from zero",
		"wait 0.5 seconds",
		"loop 2 times:",
		"\tanimate left arm pose of clicked entity to match rotation(-150, -45, 0) with duration 0.25",
		"\twait 0.25 seconds",
		"\tanimate left arm pose of clicked entity to match rotation(-120, -45, 0) with duration 0.25",
		"\twait 0.25 seconds"
})
@Since("INSERT VERSION")
public class EffAnimateEntity extends Effect {

	static {
		Skript.registerEffect(EffAnimateEntity.class,
				"[:async] animate %armorstandpose% [pose|rotation] of %livingentities% (using|with|to match) %rotation% with duration %timespan% [:from zero]");
	}

	private Expression<ArmorStandPose> armorStandPose;
	private Expression<LivingEntity> animationEntities;
	private Expression<Rotations> animationRotation;
	private Expression<Timespan> animationDuration;
	private boolean animationFromZero, runAsync;

	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.armorStandPose = (Expression<ArmorStandPose>) expressions[0];
		this.animationEntities = (Expression<LivingEntity>) expressions[1];
		this.animationRotation = (Expression<Rotations>) expressions[2];
		this.animationDuration = (Expression<Timespan>) expressions[3];
		this.animationFromZero = parseResult.hasTag("from zero");
		this.runAsync = parseResult.hasTag("async");
		return true;
	}

	@Override
	protected void execute(@NotNull Event event) {
		ArmorStand[] animationEntities = this.animationEntities.stream(event).filter(entity -> entity instanceof ArmorStand).toArray(ArmorStand[]::new);
		ArmorStandPose armorStandPose = this.armorStandPose.getSingle(event);
		Rotations rotations = this.animationRotation.getSingle(event);
		Timespan duration = this.animationDuration.getSingle(event);
		if (animationEntities.length == 0) return;
		if (armorStandPose == null) return;
		if (rotations == null) return;
		if (duration == null) return;
		AnimationPose animation = new AnimationPose(armorStandPose, rotations, duration, animationFromZero, runAsync);
		for (ArmorStand animationEntity : animationEntities) {
			animation.animate(animationEntity);
		}
	}

	@Override
	public @NotNull String toString(@Nullable Event event, boolean debug) {
		return String.format("animate %s of %s using %s with duration %s%s",
				this.armorStandPose.toString(event, debug),
				this.animationEntities.toString(event, debug),
				this.animationRotation.toString(event, debug),
				this.animationDuration.toString(event, debug),
				animationFromZero ? " from zero" : "");
	}
}
