package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.itemsadder.IADropCommand;
import com.denizenscript.depenizen.bukkit.commands.itemsadder.IAEntityCommand;
import com.denizenscript.depenizen.bukkit.commands.itemsadder.IAMountCommand;
import com.denizenscript.depenizen.bukkit.commands.itemsadder.IAPlaceCommand;
import com.denizenscript.depenizen.bukkit.commands.itemsadder.IAPlayerCommand;
import com.denizenscript.depenizen.bukkit.commands.itemsadder.IASpawnCommand;
import com.denizenscript.depenizen.bukkit.commands.itemsadder.IATotemAnimationCommand;
import com.denizenscript.depenizen.bukkit.properties.itemsadder.ItemsAdderElementProperties;
import com.denizenscript.depenizen.bukkit.properties.itemsadder.ItemsAdderItemProperties;
import com.denizenscript.depenizen.bukkit.properties.itemsadder.ItemsAdderLocationProperties;
import com.denizenscript.depenizen.bukkit.utilities.itemsadder.ItemsAdderLoaders;
import com.denizenscript.depenizen.bukkit.events.itemsadder.ItemsAdderLoadDataScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.itemsadder.IAEntityTag;
import com.denizenscript.depenizen.bukkit.objects.itemsadder.ItemsAdderItemTag;
import com.denizenscript.depenizen.bukkit.objects.itemsadder.IAPlayerTag;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomCrop;
import dev.lone.itemsadder.api.CustomFire;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderBridge extends Bridge {
    public static Boolean loaded;

    @Override
    public void init() {
        loaded = false;
        ObjectFetcher.registerWithObjectFetcher(IAEntityTag.class, IAEntityTag.tagProcessor);
        ObjectFetcher.registerWithObjectFetcher(ItemsAdderItemTag.class, ItemsAdderItemTag.tagProcessor);
        ObjectFetcher.registerWithObjectFetcher(IAPlayerTag.class, IAPlayerTag.tagProcessor);
        DenizenCore.commandRegistry.registerCommand(IADropCommand.class);
        DenizenCore.commandRegistry.registerCommand(IAEntityCommand.class);
        DenizenCore.commandRegistry.registerCommand(IAMountCommand.class);
        DenizenCore.commandRegistry.registerCommand(IAPlaceCommand.class);
        DenizenCore.commandRegistry.registerCommand(IAPlayerCommand.class);
        DenizenCore.commandRegistry.registerCommand(IASpawnCommand.class);
        DenizenCore.commandRegistry.registerCommand(IATotemAnimationCommand.class);
        PropertyParser.registerProperty(ItemsAdderElementProperties.class, ElementTag.class);
        PropertyParser.registerProperty(ItemsAdderItemProperties.class, ItemTag.class);
        PropertyParser.registerProperty(ItemsAdderLocationProperties.class, LocationTag.class);
        ScriptEvent.registerScriptEvent(ItemsAdderLoadDataScriptEvent.class);
        new ItemsAdderLoaders().RegisterEvents();

        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                itemsadderTagEvent(event);
            }
        }, "itemsadder");

        // <--[tag]
        // @attribute <itemsadder_item[<name>]>
        // @returns ItemsAdderItemTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns an ItemsAdderItemTag of the named Items Adder item.
        // -->
        TagManager.registerTagHandler(ItemsAdderItemTag.class, "itemsadder_item", (attribute) -> {
            if (!attribute.hasParam()) {
                attribute.echoError("The itemsadder_item tag must have input.");
                return null;
            }
            String name = attribute.getParam();
            ItemsAdderItemTag item = new ItemsAdderItemTag(name);
            if(!item.isValid()) {
                attribute.echoError("'" + name + "' is not a valid ItemsAdder item.");
                return null;
            }

            return item;
        });
    }

    public void itemsadderTagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <itemsadder.is_loaded>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if ItemsAdder has loaded successfully
        // -->
        if (attribute.startsWith("is_loaded")) {
            event.setReplacedObject(new ElementTag(loaded));
        }

        // <--[tag]
        // @attribute <itemsadder.list_items>
        // @returns ListTag(ItemsAdderItemTag)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns a list of ItemsAdder item names loaded
        // -->
        else if (attribute.startsWith("list_items")) {
            ListTag items = new ListTag();

            if (!attribute.hasParam()) {
                for (CustomStack i : ItemsAdder.getAllItems()) {
                    items.addObject(new ElementTag(i.getNamespacedID()));
                }
            }

            event.setReplacedObject(items);
        }

        // <--[tag]
        // @attribute <itemsadder.item[<item_name>]>
        // @returns ItemsAdderItemTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns a the ItemsAdder item
        // -->
        else if (attribute.startsWith("item")) {
            if (attribute.hasParam()) {
                ItemsAdderItemTag item = new ItemsAdderItemTag(attribute.getParam());
                if(item.isValid()) {
                    event.setReplacedObject(item.getObjectAttribute(attribute.fulfill(1)));
                }
            }
        }
    }
}
