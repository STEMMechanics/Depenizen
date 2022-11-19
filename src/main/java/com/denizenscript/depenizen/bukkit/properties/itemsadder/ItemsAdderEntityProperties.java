package com.denizenscript.depenizen.bukkit.properties.itemsadder;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.objects.itemsadder.IAEntityTag;

import dev.lone.itemsadder.api.CustomEntity;

public class ItemsAdderEntityProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ItemsAdderItem";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag pl) {
        return pl instanceof ItemTag;
    }

    public static ItemsAdderEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        return new ItemsAdderEntityProperties((EntityTag) object);
    }

    public static final String[] handledTags = new String[] {
            "is_iaentity", "iaentity"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ItemsAdderEntityProperties(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <EntityTag.is_iaentity>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the entity is an ItemsAdder entity.
        // -->
        if (attribute.startsWith("is_iaentity")) {
            try {
                return new ElementTag(CustomEntity.isCustomEntity(entity.getBukkitEntity()));
            } catch (Exception e) {
                // None
            }

            return new ElementTag(false);
        }

        // <--[tag]
        // @attribute <EntityTag.iaentity>
        // @returns IAEntityTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the ItemsAdder Entity object associated with this Entity, if any.
        // -->
        else if (attribute.startsWith("iaentity")) {
            try {
                CustomEntity customEntity = CustomEntity.byAlreadySpawned(entity.getBukkitEntity());
                if(customEntity != null) {
                    return new IAEntityTag(customEntity);
                }

                return null;
            }
            catch(Exception e) {
                return null;
            }
        }

        return null;
    }
}
