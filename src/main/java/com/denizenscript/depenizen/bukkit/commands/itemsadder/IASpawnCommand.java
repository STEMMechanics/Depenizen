package com.denizenscript.depenizen.bukkit.commands.itemsadder;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.objects.itemsadder.ItemsAdderItemTag;

import dev.lone.itemsadder.api.CustomFurniture;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class IASpawnCommand extends AbstractCommand {

    public IASpawnCommand() {
        setName("iaspawn");
        setSyntax("iaspawn [<name>] [<location>]");
        setRequiredArguments(2, 2);
    }

    // <--[command]
    // @Name ItemsAdderPlace
    // @Syntax iaplace [<name>] [<location>]
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

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("name")) {
                scriptEntry.addObject("name", arg.asElement());
            }
            else if (!scriptEntry.hasObject("location")
                    && arg.matchesArgumentType(LocationTag.class)) {
                scriptEntry.addObject("location", arg.asType(LocationTag.class));
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
        ElementTag name = scriptEntry.getElement("name");
        LocationTag location = scriptEntry.getObjectTag("location");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), name, location);
        }

        try {
            CustomFurniture furniture = CustomFurniture.spawn(name.asString(), location.getBlock());
            if(furniture != null) {
                scriptEntry.addObject("spawned_itemsadder", new ItemsAdderItemTag(furniture));
            } else {
                Debug.echoError("ItemsAdder item does not exist: " + name.asString());
            }
        } catch(Exception e) {
            Debug.echoError("ItemsAdder item cannot be spawned: " + name.asString());
        }
    }
}
