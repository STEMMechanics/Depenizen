package com.denizenscript.depenizen.bukkit.utilities.itemsadder;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.depenizen.bukkit.bridges.ItemsAdderBridge;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderLoaders implements Listener {

    public void RegisterEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Denizen.getInstance());
    }

    @EventHandler
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        ItemsAdderBridge.loaded = true;
    }
}
