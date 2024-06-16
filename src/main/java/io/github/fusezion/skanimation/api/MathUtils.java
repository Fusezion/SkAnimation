package io.github.fusezion.skanimation.api;

import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operator;

public class MathUtils {

	public static <T> T addition(Object left, Object right, Class<T> returnType) {
		return Arithmetics.calculate(Operator.ADDITION, left, right, returnType);
	}

	public static <T> T subtraction(Object left, Object right, Class<T> returnType) {
		return Arithmetics.calculate(Operator.SUBTRACTION, left, right, returnType);
	}

	public static <T> T division(Object left, Object right, Class<T> returnType) {
		return Arithmetics.calculate(Operator.DIVISION, left, right, returnType);
	}

	public static <T> T multiplication(Object left, Object right, Class<T> returnType) {
		return Arithmetics.calculate(Operator.MULTIPLICATION, left, right, returnType);
	}

	public static <T> T exponential(Object left, Object right, Class<T> returnType) {
		return Arithmetics.calculate(Operator.EXPONENTIATION, left, right, returnType);
	}

}
