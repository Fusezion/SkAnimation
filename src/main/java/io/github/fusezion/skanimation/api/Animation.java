package io.github.fusezion.skanimation.api;

import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.variables.Variables;
import io.github.fusezion.skanimation.SkAnimation;
import io.papermc.paper.math.Rotations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operator;

public abstract class Animation {

	public abstract String toReadableString();
	public abstract void animate(ArmorStand armorStand);

	public static void registerAnimation(Animation animation, @Nullable VariableString variableString, @Nullable String animationName) {
		if (animationName != null && !animationName.isBlank())
			SkAnimation.ANIMATION_MAP.put(animationName, animation);
		if (variableString != null) {
			String string = Util.getVariableName(variableString);
			if (string != null)
				Variables.setVariable(string, animation, null, false);
		}
	}

	public static class AnimationPose extends Animation {

		private final ArmorStandPose armorStandPose;
		private final Rotations targetRotation;
		private final Long duration;
		private final boolean fromZero;
		private final boolean runAsync;

		public AnimationPose(ArmorStandPose armorStandPose, Rotations rotations, Timespan length, boolean fromZero, boolean runAsync) {
			this.armorStandPose = armorStandPose;
			this.targetRotation = rotations;
			this.duration = length.getTicks();
			this.fromZero = fromZero;
			this.runAsync = runAsync;
		}

		@Override
		public String toReadableString() {
			return String.format("Animation[targetRotation=%s, pose=%s, duration=%s, fromZero=%s]",
					Classes.toString(targetRotation),
					Classes.toString(armorStandPose),
					Timespan.fromTicks(duration),
					fromZero
			);
		}

		@Override
		public void animate(ArmorStand armorStand) {
			if (fromZero) {
				armorStandPose.setRotation(armorStand, Rotations.ZERO);
			}
			if (!fromZero && armorStandPose.getRotation(armorStand).equals(targetRotation)) return;

			Rotations minimumAngleMovement = Arithmetics.calculate(Operator.SUBTRACTION, targetRotation, armorStandPose.getRotation(armorStand), Rotations.class);
			BukkitRunnable runnable = createLinearRunnable(armorStand, minimumAngleMovement);
			if (runAsync) {
				runnable.runTaskTimer(SkAnimation.getPlugin(), 0, 1);
			} else {
				runnable.runTaskTimerAsynchronously(SkAnimation.getPlugin(), 0, 1);
			}
		}

		private BukkitRunnable createLinearRunnable(ArmorStand armorStand, Rotations angleMovement) {

			Rotations divAngleMovement = MathUtils.division(angleMovement, duration.doubleValue(), Rotations.class);
			return new BukkitRunnable() {

				long ticks;

				@Override
				public void run() {
					ticks++;
					armorStandPose.setRotation(armorStand, MathUtils.addition(armorStandPose.getRotation(armorStand), divAngleMovement, Rotations.class));
					if (ticks == duration || armorStandPose.getRotation(armorStand).equals(targetRotation)) {
						armorStandPose.setRotation(armorStand, targetRotation);
						this.cancel();
					}
				}
			};

		}

	}

	public static class AnimationTrigger extends Animation {

		private final Trigger trigger;

		public AnimationTrigger(Trigger trigger) {
			this.trigger = trigger;
		}

		@Override
		public String toReadableString() {
			return String.format("Animation[trigger=%s, animation=%s]", Integer.toHexString(trigger.hashCode()), Integer.toHexString(hashCode()));
		}

		@Override
		public void animate(ArmorStand armorStand) {
			trigger.execute(new AnimationEvent(armorStand));
		}
	}

}
