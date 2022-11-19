package com.denizenscript.depenizen.bukkit.properties.itemsadder;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;

public class ItemsAdderElementProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ItemsAdderElement";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag pl) {
        return pl instanceof ElementTag;
    }

    public static ItemsAdderElementProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        return new ItemsAdderElementProperties((ElementTag) object);
    }

    public static final String[] handledTags = new String[] {
            "itemsadder"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ItemsAdderElementProperties(ElementTag element) {
        this.element = element;
    }

    ElementTag element;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("itemsadder")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <ElementTag.itemsadder.parse>
            // @returns ElementTag(String)
            // @plugin Depenizen, ItemsAdder
            // @description
            // Returns element with itemsadder placeholders replaced with font images
            // -->
            if (attribute.startsWith("parse")) {
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <ElementTag.parse_itemsadder.as_player[<PlayerTag>]>
                // @returns ElementTag(String)
                // @plugin Depenizen, ItemsAdder
                // @description
                // Returns element with itemsadder placeholders replaced based on Player
                // permissions
                // -->
                if (attribute.startsWith("as_player") && attribute.hasParam()) {
                    PlayerTag player = attribute.paramAsType(PlayerTag.class);
                    if (player != null) {
                        return new ElementTag(
                                FontImageWrapper.replaceFontImages(player.getPlayerEntity(), element.asString()))
                                .getObjectAttribute(attribute.fulfill(1));
                    } else {
                        return null;
                    }
                }
                return new ElementTag(FontImageWrapper.replaceFontImages(element.asString())).getObjectAttribute(attribute);
            }
        }

        return null;
    }
}
