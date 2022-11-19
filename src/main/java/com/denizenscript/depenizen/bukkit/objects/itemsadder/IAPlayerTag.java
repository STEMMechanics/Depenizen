package com.denizenscript.depenizen.bukkit.objects.itemsadder;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;

import dev.lone.itemsadder.api.CustomPlayer;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;

import org.bukkit.entity.Entity;

import java.util.UUID;

public class IAPlayerTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name IAPlayerTag
    // @prefix iaplayer
    // @base ElementTag
    // @format
    // The identity format for IAPlayerTag is <uuid>
    // For example, 'iaplayer@1234-1234-1234'.
    //
    // @plugin Depenizen, ItemsAdder
    // @description
    // A IAPlayerTag represents a ItemsAdder Player entity in the world.
    //
    // -->

    public static IAPlayerTag valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("iaplayer")
    public static IAPlayerTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("iaplayer@", "");
            UUID uuid = UUID.fromString(string);
            Entity entity = EntityTag.getEntityForID(uuid);

            if(entity == null) {
                return null;
            }

            CustomPlayer player = CustomPlayer.byAlreadySpawned(entity);
            if(player == null) {
                return null;
            }

            return new IAPlayerTag(player);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public IAPlayerTag(CustomPlayer player) {
        this.customPlayer = player;
    }

    String prefix = "IAPlayer";
    CustomPlayer customPlayer = null;

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
        return "iaplayer@" + customPlayer.getEntity().getUniqueId();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    public static ObjectTagProcessor<IAPlayerTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {

        // <--[tag]
        // @attribute <ItemsAdderItemTag.name>
        // @returns ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the player name
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.customPlayer.getPlayerName());
        });
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply properties to a ItemsAdder item!");
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object IAPlayerTag
        // @name playAnimation
        // @input ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Plays an animation.
        // -->
        if (mechanism.matches("playanimation")) {
            customPlayer.playAnimation(mechanism.getValue().asString());
        }

        // <--[mechanism]
        // @object IAPlayerTag
        // @name stopAnimation
        // @plugin Depenizen, ItemsAdder
        // @description
        // Stops the current playing animation.
        // -->
        if (mechanism.matches("stopanimation")) {
            customPlayer.stopAnimation();
        }

        CoreUtilities.autoPropertyMechanism(this, mechanism);
        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }
    }
}
