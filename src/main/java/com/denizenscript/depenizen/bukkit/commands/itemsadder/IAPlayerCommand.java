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

public class IAPlayerCommand extends AbstractCommand {

    public IAPlayerCommand() {
        setName("iaplayer");
        setSyntax("iaplayer [spawn/playemote/stopemote] (skin:<skin>) (location:<location>) (player:<player>) (emote:<emote>)");
        setRequiredArguments(1, 2);
    }

    // <--[command]
    // @Name ItemsAdderPlayer
    // @Syntax iaplayer [spawn/playemote/stopemote] (skin:<skin>) (location:<location>) (player:<player>) (emote:<emote>)
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

    public enum IAPlayerInstruction { SPAWN, PLAYEMOTE, STOPEMOTE }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("instruction")
                    && arg.matchesEnum(IAPlayerInstruction.class)) {
                scriptEntry.addObject("instruction", arg.asElement());
            }
            else if (!scriptEntry.hasObject("skin")
                    && arg.matchesPrefix("skin")) {
                scriptEntry.addObject("skin", arg.asElement());
            }
            else if (!scriptEntry.hasObject("location")
                    && arg.matchesPrefix("location")
                    && arg.matchesArgumentType(LocationTag.class)) {
                scriptEntry.addObject("location", arg.asType(LocationTag.class));
            }
            else if (!scriptEntry.hasObject("player")
                    && arg.matchesPrefix("player")
                    && arg.matchesArgumentType(PlayerTag.class)) {
                scriptEntry.addObject("location", arg.asType(PlayerTag.class));
            }
            else if (!scriptEntry.hasObject("emote")
                    && arg.matchesPrefix("emote")) {
                scriptEntry.addObject("emote", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("name") || !scriptEntry.hasObject("location")) {
            throw new InvalidArgumentsException("Must specify a name and location.");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag instruction = scriptEntry.getElement("instruction");
        ElementTag skin = scriptEntry.getElement("skin");
        LocationTag location = scriptEntry.getObjectTag("location");
        PlayerTag player = scriptEntry.getObjectTag("player");
        ElementTag emote = scriptEntry.getElement("emote");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), instruction, skin, location, player, emote);
        }

        BiFunction<Object, String, Boolean> requireObject = (obj, name) -> {
            if (obj == null) {
                handleError(scriptEntry, "Failed to process IAPlayer " + instruction.asString() + " command: no " + name + " given!");
                scriptEntry.setFinished(true);
                return true;
            }
            return false;
        };

        Supplier<Boolean> requireSkin = () -> requireObject.apply(skin, "skin");
        Supplier<Boolean> requireLocation = () -> requireObject.apply(location, "location");
        Supplier<Boolean> requirePlayer = () -> requireObject.apply(player, "player");
        Supplier<Boolean> requireEmote = () -> requireObject.apply(emote, "emote");

        IAPlayerInstruction instructionEnum = IAPlayerInstruction.valueOf(instruction.asString().toUpperCase());

        switch(instructionEnum) {
            case SPAWN: {
                if (requireSkin.get() || requireLocation.get()) {
                    return;
                }

                CustomPlayer customPlayer = CustomPlayer.spawn(skin.asString(), location);
                scriptEntry.addObject("spawned_iaplayer", new IAPlayerTag(customPlayer));
                break;
            }
            case PLAYEMOTE: {
                if (requirePlayer.get() || requireEmote.get()) {
                    return;
                }

                CustomPlayer.playEmote(player.getPlayerEntity(), emote.asString());
                break;
            }
            case STOPEMOTE: {
                if (requirePlayer.get()) {
                    return;
                }

                CustomPlayer.stopEmote(player.getPlayerEntity());
                break;
            }
        }
    }

    public void handleError(ScriptEntry entry, String message) {
        Debug.echoError(entry, "Error in " + getName() + " command: " + message);
    }

    public void handleError(ScriptEntry entry, Throwable ex) {
        Debug.echoError(entry, "Exception in " + getName() + " command:");
        Debug.echoError(ex);
    }
}
