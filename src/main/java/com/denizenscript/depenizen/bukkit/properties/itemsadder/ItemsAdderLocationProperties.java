package com.denizenscript.depenizen.bukkit.properties.itemsadder;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;

import org.bukkit.inventory.ItemStack;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.MaterialTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.bridges.ItemsAdderBridge;
import com.denizenscript.depenizen.bukkit.objects.itemsadder.ItemsAdderItemTag;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomCrop;
import dev.lone.itemsadder.api.CustomFire;
import dev.lone.itemsadder.api.CustomStack;

public class ItemsAdderLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ItemsAdderLocation";
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }

    public static boolean describes(ObjectTag pl) {
        return pl instanceof LocationTag;
    }

    public static ItemsAdderLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        return new ItemsAdderLocationProperties((LocationTag) object);
    }

    public static final String[] handledTags = new String[] {
        "is_itemsadder", "itemsadder"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ItemsAdderLocationProperties(LocationTag location) {
        this.location = location;
    }

    LocationTag location;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <LocationTag.is_itemsadder>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if item is an itemsadder created item
        // -->
        if (attribute.startsWith("is_itemsadder")) {
            ItemsAdderItemTag customItem = new ItemsAdderItemTag(location.getBlock());
            return new ElementTag(customItem != null)
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <LocationTag.itemsadder>
        // @returns ItemsAdderItemTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the itemsadder item
        // -->
        else if (attribute.startsWith("itemsadder")) {
            ItemsAdderItemTag customItem = new ItemsAdderItemTag(location.getBlock());
            if (customItem != null) {
                return customItem.getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
