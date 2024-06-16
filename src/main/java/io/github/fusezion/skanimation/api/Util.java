package io.github.fusezion.skanimation.api;

import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.ContextlessEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Util {

	/**
	 *
	 * @param variableString the variable string reference to get the name of
	 * @param event the event to use when parsing expressions, if null default to {@link ContextlessEvent}
	 * @return Returns the formatted variable string based off the given event
	 */
	@Nullable
	public static String getVariableName(@NotNull VariableString variableString, @Nullable Event event) {
		if (event == null) event = ContextlessEvent.get();
		String string = variableString.getSingle(event);
		if (string.startsWith("{"))
			string = string.substring(1);
		if (string.endsWith("}"))
			string = string.substring(0, string.length() - 1);
		return string;
	}

	/**
	 *
	 * @param variableString the variable string reference to get the name of
	 * @return Returns the formatted variable string based off the given event
	 */
	@Nullable
	public static String getVariableName(@NotNull VariableString variableString) {
		return getVariableName(variableString, ContextlessEvent.get());
	}

}
