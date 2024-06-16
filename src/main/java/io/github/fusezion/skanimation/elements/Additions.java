package io.github.fusezion.skanimation.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.function.Functions;
import ch.njol.skript.lang.function.Parameter;
import ch.njol.skript.lang.function.SimpleJavaFunction;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.DefaultClasses;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.yggdrasil.Fields;
import io.github.fusezion.skanimation.SkAnimation;
import io.github.fusezion.skanimation.api.Animation;
import io.github.fusezion.skanimation.api.AnimationEvent;
import io.github.fusezion.skanimation.api.ArmorStandPose;
import io.papermc.paper.math.Rotations;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operator;

import java.io.StreamCorruptedException;

public class Additions {

	public static ClassInfo<Rotations> ROTATIONS_CLASS;

	// ClassInfos
	static {

		Classes.registerClass(new EnumClassInfo<>(ArmorStandPose.class, "armorstandpose", "armor stand poses")
				.user("armou?r ?stand ?poses?")
				.name("Armor Stand Poses")
				.description("Represents the poses of an armor stand")
				.since("1.0.0")
		);

		Classes.registerClass(new ClassInfo<>(Animation.class, "animation")
				.user("animations?")
				.name("Animation")
				.description("Represents an animation class that was registered via the structure")
				.examples("set {_animation} to animation \"waving\"")
				.since("1.0.0")
				.parser(new Parser<>() {

					@Override
					public @Nullable Animation parse(@NotNull String string, @NotNull ParseContext context) {
						return SkAnimation.ANIMATION_MAP.get(string);
					}

					@Override
					public @NotNull String toString(Animation animation, int flags) {
						return toVariableNameString(animation);
					}

					@Override
					public @NotNull String toVariableNameString(Animation animation) {
						return animation.toReadableString();
					}
				})
		);

		if (Classes.getExactClassInfo(Rotations.class) == null) {
			Classes.registerClass(new ClassInfo<>(Rotations.class, "rotation")
					.user("rotations?")
					.name("Rotations")
					.description("Represents a rotation angle of a armor stand limb, these values are between 0-360 and match how minecraft do poses")
					.examples(
							"set {_rotation} to rotation(10,10,10)",
							"set {_rotation} to left arm pose of clicked entity")
					.since("1.0.0")
					.cloner(rotations -> Rotations.ofDegrees(rotations.x(), rotations.y(), rotations.z()))
					.defaultExpression(new SimpleLiteral<>(Rotations.ZERO, true))
					.parser(new Parser<>() {

						@Override
						public boolean canParse(@NotNull ParseContext context) {
							return false;
						}

						@Override
						public @NotNull String toString(Rotations rotations, int flags) {
							return toVariableNameString(rotations);
						}

						@Override
						public @NotNull String toVariableNameString(Rotations rotations) {
							return String.format("Rotations{x:%.2f, y:%.2f, z:%.2f}", rotations.x(), rotations.y(), rotations.z());
						}
					})
					.serializer(new Serializer<>() {
						@Override
						public @NotNull Fields serialize(Rotations rotations) {
							Fields fields = new Fields();
							fields.putPrimitive("x", rotations.x());
							fields.putPrimitive("y", rotations.y());
							fields.putPrimitive("z", rotations.z());
							return fields;
						}

						@Override
						protected Rotations deserialize(@NotNull Fields fields) throws StreamCorruptedException {
							Double x = fields.getPrimitive("x", Double.class);
							Double y = fields.getPrimitive("y", Double.class);
							Double z = fields.getPrimitive("z", Double.class);
							if (x == null || y == null || z == null) return null;
							return Rotations.ofDegrees(x, y, z);
						}

						@Override
						public void deserialize(Rotations rotations, @NotNull Fields fields) {
							throw new RuntimeException("ew ew no don't ew, why does this still exist, ew");
						}

						@Override
						public boolean mustSyncDeserialization() {
							return false;
						}

						@Override
						protected boolean canBeInstantiated() {
							return false;
						}
					}));

			ROTATIONS_CLASS = Classes.getExactClassInfo(Rotations.class);

		}

	}

	// Arithmetic
	static {

		if (ROTATIONS_CLASS != null) {
			Arithmetics.registerOperation(Operator.ADDITION, Rotations.class, (left, right) -> left.add(right.x(), right.y(), right.z()));
			Arithmetics.registerOperation(Operator.SUBTRACTION, Rotations.class, (left, right) -> left.subtract(right.x(), right.y(), right.z()));
			Arithmetics.registerOperation(Operator.DIVISION, Rotations.class, Double.class, (left, right) -> {
				double x = left.x() / right;
				double y = left.y() / right;
				double z = left.z() / right;
				return Rotations.ofDegrees(x, y, z);
			});
			Arithmetics.registerOperation(Operator.MULTIPLICATION, Rotations.class, Double.class, (left, right) -> {
				double x = left.x() * right;
				double y = left.y() * right;
				double z = left.z() * right;
				return Rotations.ofDegrees(x, y, z);
			});
			Arithmetics.registerDifference(Rotations.class, (left, right) -> {
				double x = Math.abs(left.x() - right.x());
				double y = Math.abs(left.y() - right.y());
				double z = Math.abs(left.z() - right.z());
				return Rotations.ofDegrees(x, y, z);
			});
		}

	}

	// Converters

	// Functions
	static {

		if (ROTATIONS_CLASS != null) {

			Functions.registerFunction(new SimpleJavaFunction<>("rotation", new Parameter[]{
							new Parameter<>("x", DefaultClasses.NUMBER, true, null),
							new Parameter<>("y", DefaultClasses.NUMBER, true, null),
							new Parameter<>("z", DefaultClasses.NUMBER, true, null)
					}, ROTATIONS_CLASS, true) {

						@Override
						public @Nullable Rotations[] executeSimple(Object[][] params) {
							double x = ((Number) params[0][0]).doubleValue();
							double y = ((Number) params[1][0]).doubleValue();
							double z = ((Number) params[2][0]).doubleValue();
							return new Rotations[]{Rotations.ofDegrees(x, y, z)};
						}
					})
					.description("Creates a new rotation ")
					.examples("set {_rotation} to rotation(-150,-45,0)")
					.since("1.0.0");

		}

	}

	// EventValues
	static {

		EventValues.registerEventValue(AnimationEvent.class, LivingEntity.class, new Getter<>() {

			@Override
			public @Nullable LivingEntity get(AnimationEvent event) {
				return event.getArmorStand();
			}

		}, EventValues.TIME_NOW);

	}

}
