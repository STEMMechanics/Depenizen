package com.denizenscript.depenizen.bukkit.events.itemsadder;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.ItemsAdderBridge;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderLoadDataScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // itemsadder loaded
    //
    // @Cancellable false
    //
    // @Triggers when a itemsadder is loaded or reloaded.
    //
    // @Context
    // <context.reload> returns if itemsadder was reloaded.
    //
    // @Plugin Depenizen, ItemsAdder
    //
    // @Group Depenizen
    //
    // -->

    public ItemsAdderLoadDataScriptEvent() {
        instance = this;
    }

    public static ItemsAdderLoadDataScriptEvent instance;
    public ItemsAdderLoadDataEvent event;
    public Boolean reload;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("itemsadder loaded");
    }

    @Override
    public String getName() {
        return "ItemsAdderLoadData";
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "reload":
                return new ElementTag(this.reload);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        this.event = event;
        ItemsAdderBridge.loaded = true;
        this.reload = (event.getCause() == ItemsAdderLoadDataEvent.Cause.RELOAD);
        fire(event);
    }
}
