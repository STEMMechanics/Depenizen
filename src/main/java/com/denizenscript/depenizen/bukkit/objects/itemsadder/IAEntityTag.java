package com.denizenscript.depenizen.bukkit.objects.itemsadder;

import com.denizenscript.denizen.objects.EntityFormObject;
import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.depenizen.bukkit.bridges.ItemsAdderBridge;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomCrop;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomFire;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IAEntityTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name IAEntityTag
    // @prefix iaentity
    // @base ElementTag
    // @format
    // The identity format for IAEntityTag is <uuid>
    // For example, 'iaentity@1234-1234-1234'.
    //
    // @plugin Depenizen, ItemsAdder
    // @description
    // A IAEntityTag represents an Items Adder entity in the world.
    //
    // -->

    public static IAEntityTag valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("iaentity")
    public static IAEntityTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("iaentity@", "");
            UUID uuid = UUID.fromString(string);
            Entity ent = EntityTag.getEntityForID(uuid);

            if (ent == null) {
                return null;
            }

            CustomEntity customEnt = CustomEntity.byAlreadySpawned(ent);
            if (customEnt == null) {
                return null;
            }

            return new IAEntityTag(customEnt);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public IAEntityTag(CustomEntity ent) {
        customEntity = ent;
    }

    String prefix = "IAEntity";
    CustomEntity customEntity = null;

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "iaentity@" + customEntity.getEntity().getUniqueId();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    public static ObjectTagProcessor<IAEntityTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {

        // <--[tag]
        // @attribute <IAEntityTag.animations>
        // @returns ListTag(ElementTag)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns a list of model animations for this entity.
        // -->
        tagProcessor.registerTag(ListTag.class, "animations", (attribute, object) -> {
            ArrayList<ElementTag> animations = new ArrayList<>();
            try {
                for (String s : object.customEntity.getAnimationsNames()) {
                    animations.add(new ElementTag(s));
                }
            } catch (Exception e) {
                // None
            }

            return new ListTag(animations);
        });

        // <--[tag]
        // @attribute <IAEntityTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the entity associated with this ItemsAdder entity.
        // -->
        tagProcessor.registerTag(EntityTag.class, "entity", (attribute, object) -> {
            try {
                return new EntityTag(object.customEntity.getEntity());
            } catch (Exception e) {
                // None
            }

            return null;
        });

        // <--[tag]
        // @attribute <IAEntityTag.location>
        // @returns LocationTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the location of the entity within the world.
        // -->
        tagProcessor.registerTag(LocationTag.class, "location", (attribute, object) -> {
            try {
                return new LocationTag(object.customEntity.getLocation());
            } catch (Exception e) {
                // None
            }

            return null;
        });

        // <--[tag]
        // @attribute <IAEntityTag.namespace>
        // @returns ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the namespace of the entity.
        // -->
        tagProcessor.registerTag(ElementTag.class, "namespace", (attribute, object) -> {
            try {
                return new ElementTag(object.customEntity.getNamespace());
            } catch (Exception e) {
                // None
            }

            return null;
        });

        // <--[tag]
        // @attribute <IAEntityTag.passengers>
        // @returns ListTag(EntityTag)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns a list of all the passengers of the entity.
        // -->
        tagProcessor.registerTag(ListTag.class, "passengers", (attribute, object) -> {
            ArrayList<EntityTag> passengers = new ArrayList<>();
            try {
                for (Entity ent : object.customEntity.getPassengers()) {
                    passengers.add(new EntityTag(ent));
                }
            } catch (Exception e) {
                // None
            }

            return new ListTag(passengers);
        });

        // <--[tag]
        // @attribute <IAEntityTag.has_passenger[(<entity>)]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the entity supplied is a passenger of the entity.
        // If no entity is supplied, if the entity has any passengers.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_passenger", (attribute, object) -> {
            try {
                if(attribute.hasParam()) {
                    return new ElementTag(object.customEntity.hasPassenger(
                            attribute.paramAsType(EntityTag.class).getLivingEntity()));
                }
                else {
                    return new ElementTag(object.customEntity.hasPassenger());
                }
            } catch (Exception e) {
                // None
            }

            return new ElementTag(false);
        });

        // <--[tag]
        // @attribute <IAEntityTag.is_empty>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the entity does not contain passengers.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_empty", (attribute, object) -> {
            try {
                if (attribute.hasParam()) {
                    return new ElementTag(object.customEntity.hasPassenger(
                            attribute.paramAsType(EntityTag.class).getLivingEntity()));
                }

            } catch (Exception e) {
                // None
            }

            return new ElementTag(false);
        });

        // <--[tag]
        // @attribute <IAEntityTag.is_animating[<animation>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the entity is playing the specfied animation.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_animating", (attribute, object) -> {
            try {
                return new ElementTag(!object.customEntity.isPlayingAnimation(attribute.getParam()));
            } catch (Exception e) {
                // None
            }

            return new ElementTag(true);
        });
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply properties to an ItemsAdder entity!");
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object IAEntityTag
        // @name add_passenger
        // @input EntityTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Adds a passenger to the first available slot of this entity
        // @tags
        // <IAEntityTag.passengers>
        // <IAEntityTag.is_empty>
        // -->
        if (mechanism.matches("add_passenger") && mechanism.requireObject(EntityTag.class)) {
            EntityTag passenger = mechanism.valueAsType(EntityTag.class);
            if (passenger.isSpawned()) {
                customEntity.addPassenger(passenger.getLivingEntity());
            }
        }

        // <--[mechanism]
        // @object IAEntityTag
        // @name remove
        // @plugin Depenizen, ItemsAdder
        // @description
        // Removes the entity from the world.
        // -->
        else if (mechanism.matches("remove")) {
            customEntity.destroy();
        }

        CoreUtilities.autoPropertyMechanism(this, mechanism);
        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }
    }
}
