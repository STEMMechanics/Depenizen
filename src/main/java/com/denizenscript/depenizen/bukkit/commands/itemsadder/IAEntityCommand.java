package com.denizenscript.depenizen.bukkit.commands.itemsadder;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.objects.itemsadder.IAPlayerTag;

import dev.lone.itemsadder.api.CustomPlayer;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class IAEntityCommand extends AbstractCommand {

    public IAEntityCommand() {
        setName("iaentity");
        setSyntax("iaentity [playanimation] (animation:<animation>)");
        setRequiredArguments(1, 2);
    }

    // <--[command]
    // @Name ItemsAdderEntity
    // @Syntax iaentity [playanimation] (animation:<animation>)
    // @Group Depenizen
    // @Plugin Depenizen, ItemsAdder
    // @Required 2
    // @Maximum 2
    // @Short Places an ItemsAdder custom block at a location
    //
    // @Description
    // This allows you to place a ItemsAdder custom block at a location using the blocks namespace name.
    //
    // @Tags
    // <entry[saveName].spawned_itemsadder> returns the spawned ItemsAdder item.
    //
    // @Usage
    // Use to place a itemsadder:something at player's cursor location.
    // - itemsadderplace itemsadder:something <player.cursor_on>
    //
    // -->

    public enum IAEntityInstruction { EJECT }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("instruction")
                    && arg.matchesEnum(IAEntityInstruction.class)) {
                scriptEntry.addObject("instruction", arg.asElement());
            }
            else if (!scriptEntry.hasObject("animation")
                    && arg.matchesPrefix("animation")) {
                scriptEntry.addObject("animation", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag instruction = scriptEntry.getElement("instruction");
        ElementTag animation = scriptEntry.getElement("animation");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), instruction, animation);
        }

        BiFunction<Object, String, Boolean> requireObject = (obj, name) -> {
            if (obj == null) {
                handleError(scriptEntry, "Failed to process IAEntity " + instruction.asString() + " command: no " + name + " given!");
                scriptEntry.setFinished(true);
                return true;
            }
            return false;
        };

        Supplier<Boolean> requireAnimation = () -> requireObject.apply(animation, "animation");

        IAEntityInstruction instructionEnum = IAEntityInstruction.valueOf(instruction.asString().toUpperCase());

        // switch(instructionEnum) {
        //     case PLAYANIMATION: {
        //         if (requireAnimation.get()) {
        //             return;
        //         }

        //         CustomEntity customPlayer = CustomPlayer.spawn(skin.asString(), location);
        //         scriptEntry.addObject("spawned_iaplayer", new IAPlayerTag(customPlayer));
        //         break;
        //     }
        // }
    }

    public void handleError(ScriptEntry entry, String message) {
        Debug.echoError(entry, "Error in " + getName() + " command: " + message);
    }

    public void handleError(ScriptEntry entry, Throwable ex) {
        Debug.echoError(entry, "Exception in " + getName() + " command:");
        Debug.echoError(ex);
    }
}
