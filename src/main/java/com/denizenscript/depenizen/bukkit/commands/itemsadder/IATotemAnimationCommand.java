package com.denizenscript.depenizen.bukkit.commands.itemsadder;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.objects.itemsadder.ItemsAdderItemTag;

import dev.lone.itemsadder.api.ItemsAdder;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class IATotemAnimationCommand extends AbstractCommand {

    public IATotemAnimationCommand() {
        setName("iatotemanimation");
        setSyntax("iatotemanimation [<name>] [<player>]");
        setRequiredArguments(2, 2);
    }

    // <--[command]
    // @Name ItemsAdderTotemAnimation
    // @Syntax iatotemanimation [<name>] [<player>]
    // @Group Depenizen
    // @Plugin Depenizen, ItemsAdder
    // @Required 2
    // @Maximum 2
    // @Short Plays the ItemsAdder custom totem animation for a player
    //
    // @Description
    // This allows you to place a ItemsAdder custom block at a location using the blocks namespace name.
    //
    // @Usage
    // Use to place a itemsadder:something at player's cursor location.
    // - itemsadderplace itemsadder:something <player.cursor_on>
    //
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("name")) {
                scriptEntry.addObject("name", arg.asElement());
            }
            else if (!scriptEntry.hasObject("player")
                    && arg.matchesArgumentType(PlayerTag.class)) {
                scriptEntry.addObject("player", arg.asType(PlayerTag.class));
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("name") || !scriptEntry.hasObject("player")) {
            throw new InvalidArgumentsException("Must specify a name and player.");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag name = scriptEntry.getElement("name");
        PlayerTag player = scriptEntry.getObjectTag("player");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), name, player);
        }

        ItemsAdder.playTotemAnimation(player.getPlayerEntity(), name.asString());
    }
}
