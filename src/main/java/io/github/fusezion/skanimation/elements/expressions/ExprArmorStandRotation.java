package io.github.fusezion.skanimation.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import io.github.fusezion.skanimation.api.ArmorStandPose;
import io.papermc.paper.math.Rotations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operator;

@Name("ArmorStand Pose")
@Description("Allows you to get/change an armor stand's limb positions")
@Examples({
		"broadcast \"Left Arm: %left arm pose of clicked entity%\"",
		"set right arm rotation of clicked entity to rotation(-120,45,0)",
		"add rotation(-30,0,0) to right arm pose of clicked entity",
		"remove rotation(0,45,0) from right arm rotation of clicked entity",
		"reset left arm pose of clicked entity"
})
@Since("INSERT VERSION")
public class ExprArmorStandRotation extends PropertyExpression<LivingEntity, Rotations> {

	static {
		register(ExprArmorStandRotation.class, Rotations.class, "%armorstandposes% (rotation|pose)", "livingentities");
	}

	private Expression<ArmorStandPose> armorStandPose;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		armorStandPose = (Expression<ArmorStandPose>) (matchedPattern == 1 ? expressions[1] : expressions[0]);
		setExpr((Expression<? extends LivingEntity>) (matchedPattern == 1 ? expressions[0] : expressions[1]));
		return true;
	}

	@Override
	protected Rotations[] get(Event event, LivingEntity[] source) {
		ArmorStandPose pose = this.armorStandPose.getSingle(event);
		if (pose == null) return null;
		return get(source, entity -> {
			if (entity instanceof ArmorStand armorStand) {
				return pose.getRotation(armorStand);
			}
			return null;
		});
	}

	@Override
	public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case ADD,REMOVE -> CollectionUtils.array(Rotations[].class);
			case RESET, SET -> CollectionUtils.array(Rotations.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		ArmorStandPose pose = this.armorStandPose.getSingle(event);
		if (pose == null) return;
		Rotations newRotation = Rotations.ZERO;
		if (delta != null) {
			for (Object object : delta) {
				if (object instanceof Rotations rotations)
					newRotation = Arithmetics.calculate(Operator.ADDITION, newRotation, rotations, Rotations.class);
			}
		}

		switch (mode) {
			case REMOVE:
				newRotation = Arithmetics.calculate(Operator.MULTIPLICATION, newRotation, -1, Rotations.class);
			case ADD:
				for (LivingEntity entity : getExpr().getArray(event)) {
					if (!(entity instanceof ArmorStand armorStand)) continue;
					pose.setRotation(armorStand, Arithmetics.calculate(Operator.ADDITION, pose.getRotation(armorStand), newRotation, Rotations.class));
				}
				break;
			case RESET, SET:
				for (LivingEntity entity : getExpr().getArray(event)) {
					if (!(entity instanceof ArmorStand armorStand)) continue;
					pose.setRotation(armorStand, newRotation);
				}
		}

	}

	@Override
	public @NotNull Class<Rotations> getReturnType() {
		return Rotations.class;
	}

	@Override
	public @NotNull String toString(@Nullable Event event, boolean debug) {
		return String.format("%s rotation of %s",
				this.armorStandPose.toString(event, debug),
				getExpr().toString(event, debug));
	}
}
