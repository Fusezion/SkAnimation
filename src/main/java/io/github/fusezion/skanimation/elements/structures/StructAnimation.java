package io.github.fusezion.skanimation.elements.structures;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.VariableString;
import io.github.fusezion.skanimation.api.Animation;
import io.github.fusezion.skanimation.api.Animation.AnimationTrigger;
import io.github.fusezion.skanimation.api.AnimationEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.TriggerEntryData;
import org.skriptlang.skript.lang.entry.util.VariableStringEntryData;
import org.skriptlang.skript.lang.structure.Structure;

@Name("Create Animation")
@Description("Create an animation base that can be applied to entities at a later date")
@Examples({
		"register animation \"wave\"",
		"\tvariable: {animations::wave} # variable reference optional",
		"\tanimation:",
		"\t\tanimate left leg of entity using rotation(0,0,-120) with duration 1 second from zero",
		"\t\twait a second",
		"\t\tloop 3 times:",
		"\t\t\tanimate left leg of entity using rotation(0,0,-120) with duration 0.5 second",
		"\t\t\twait 0.5 seconds",
		"\t\t\tanimate left leg of entity using rotation(0,0,-90) with duration 0.5 second",
		"\t\t\twait 0.5 seconds",
		"\t\tanimate left leg of entity using rotation(0,0,0) with duration 1 second",
})
@Since("1.0.0")
public class StructAnimation extends Structure {

	static {
		EntryValidator entryValidator = EntryValidator.builder()
				.addEntryData(new VariableStringEntryData("variable", null, true))
				.addEntryData(new TriggerEntryData("animation", null, false))
				.build();
		Skript.registerStructure(StructAnimation.class, entryValidator, "(register|create) [a] [new] animation [[named|with (id|name)] [%-string%]]");
	}

	private Literal<String> animationName;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Literal<?>[] literals, int i, ParseResult parseResult, EntryContainer entryContainer) {
		animationName = (Literal<String>) literals[0];
		return true;
	}

	@Override
	public boolean load() {
		getParser().setCurrentEvent("register   animation", AnimationEvent.class);
		EntryContainer entryContainer = getEntryContainer();

		String animationName = this.animationName != null ? this.animationName.getSingle() : null;
		VariableString variableString = entryContainer.getOptional("variable", VariableString.class, true);
		if (animationName == null && variableString == null) {
			Skript.error("When registering a new animation you must either define a variable to store it in or a animation name/id");
			getParser().deleteCurrentEvent();
			return false;
		}
		if (animationName != null && animationName.isBlank()) {
			Skript.error("An animation name cannot be blank, you must give it some sort of structure");
			getParser().deleteCurrentEvent();
			return false;
		}

		Trigger trigger = entryContainer.get("animation", Trigger.class, true);
		Animation.registerAnimation(new AnimationTrigger(trigger), variableString, animationName);
		getParser().deleteCurrentEvent();
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "register a new animation" + (this.animationName == null ? "" : " named " + this.animationName);
	}

}
