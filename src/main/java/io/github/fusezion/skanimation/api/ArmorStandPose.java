package io.github.fusezion.skanimation.api;

import io.papermc.paper.math.Rotations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public enum ArmorStandPose {

	HEAD() {

		@Override
		public EulerAngle getPose(ArmorStand armorStand) {
			return armorStand.getBodyPose();
		}

		@Override
		public Rotations getRotation(ArmorStand armorStand) {
			return armorStand.getHeadRotations();
		}

		@Override
		public void setPose(ArmorStand armorStand, EulerAngle eulerAngle) {
			armorStand.setHeadPose(eulerAngle);
		}

		@Override
		public void setRotation(ArmorStand armorStand, Rotations rotations) {
			armorStand.setHeadRotations(rotations);
		}
	},
	BODY() {
		@Override
		public EulerAngle getPose(ArmorStand armorStand) {
			return armorStand.getBodyPose();
		}

		@Override
		public Rotations getRotation(ArmorStand armorStand) {
			return armorStand.getBodyRotations();
		}

		@Override
		public void setPose(ArmorStand armorStand, EulerAngle eulerAngle) {
			armorStand.setBodyPose(eulerAngle);
		}

		@Override
		public void setRotation(ArmorStand armorStand, Rotations rotations) {
			armorStand.setBodyRotations(rotations);
		}
	},
	LEFT_ARM() {
		@Override
		public EulerAngle getPose(ArmorStand armorStand) {
			return armorStand.getLeftArmPose();
		}

		@Override
		public Rotations getRotation(ArmorStand armorStand) {
			return armorStand.getLeftArmRotations();
		}

		@Override
		public void setPose(ArmorStand armorStand, EulerAngle eulerAngle) {
			armorStand.setLeftArmPose(eulerAngle);
		}

		@Override
		public void setRotation(ArmorStand armorStand, Rotations rotations) {
			armorStand.setLeftArmRotations(rotations);
		}
	},
	RIGHT_ARM() {
		@Override
		public EulerAngle getPose(ArmorStand armorStand) {
			return armorStand.getRightArmPose();
		}

		@Override
		public Rotations getRotation(ArmorStand armorStand) {
			return armorStand.getRightArmRotations();
		}

		@Override
		public void setPose(ArmorStand armorStand, EulerAngle eulerAngle) {
			armorStand.setRightArmPose(eulerAngle);
		}

		@Override
		public void setRotation(ArmorStand armorStand, Rotations rotations) {
			armorStand.setRightArmRotations(rotations);
		}
	},
	LEFT_LEG() {
		@Override
		public EulerAngle getPose(ArmorStand armorStand) {
			return armorStand.getLeftLegPose();
		}

		@Override
		public Rotations getRotation(ArmorStand armorStand) {
			return armorStand.getLeftLegRotations();
		}

		@Override
		public void setPose(ArmorStand armorStand, EulerAngle eulerAngle) {
			armorStand.setLeftLegPose(eulerAngle);
		}

		@Override
		public void setRotation(ArmorStand armorStand, Rotations rotations) {
			armorStand.setLeftLegRotations(rotations);
		}
	},
	RIGHT_LEG() {
		@Override
		public EulerAngle getPose(ArmorStand armorStand) {
			return armorStand.getRightLegPose();
		}

		@Override
		public Rotations getRotation(ArmorStand armorStand) {
			return armorStand.getRightLegRotations();
		}

		@Override
		public void setPose(ArmorStand armorStand, EulerAngle eulerAngle) {
			armorStand.setRightLegPose(eulerAngle);
		}

		@Override
		public void setRotation(ArmorStand armorStand, Rotations rotations) {
			armorStand.setRightLegRotations(rotations);
		}
	};

	abstract public EulerAngle getPose(ArmorStand armorStand);
	abstract public Rotations getRotation(ArmorStand armorStand);

	abstract public void setPose(ArmorStand armorStand, EulerAngle eulerAngle);
	abstract public void setRotation(ArmorStand armorStand, Rotations rotations);

}
