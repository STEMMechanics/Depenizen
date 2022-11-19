package com.denizenscript.depenizen.bukkit.objects.itemsadder;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomCrop;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomFire;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemsAdderItemTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name ItemsAdderItemTag
    // @prefix itemsadder_item
    // @base ElementTag
    // @format
    // The identity format for ItemsAdderItemTag is <string>
    // For example, 'itemsadder_item@myitems:custom_block'.
    //
    // @plugin Depenizen, ItemsAdder
    // @description
    // A ItemsAdderItemTag represents a defined Items Adder item.
    //
    // -->

    public static ItemsAdderItemTag valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("itemsadder_item")
    public static ItemsAdderItemTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("itemsadder_item@", "");
            ItemsAdderItemTag item = new ItemsAdderItemTag(string);

            if (item.isValid()) {
                return item;
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public ItemsAdderItemTag(String name) {
        if ((customFire = ItemsAdderItemTag.getCustomFire(name)) != null) {
            return;
        } else if ((customBlock = ItemsAdderItemTag.getCustomBlock(name)) != null) {
            return;
        } else if ((customStack = ItemsAdderItemTag.getCustomStack(name)) != null) {
            return;
        }
    }

    public ItemsAdderItemTag(ItemStack stack) {
        if ((customFire = ItemsAdderItemTag.getCustomFire(stack)) != null) {
            return;
        } else if ((customBlock = ItemsAdderItemTag.getCustomBlock(stack)) != null) {
            return;
        } else if ((customStack = ItemsAdderItemTag.getCustomStack(stack)) != null) {
            return;
        }
    }

    public ItemsAdderItemTag(Block block) {
        if ((customBlock = ItemsAdderItemTag.getCustomBlock(block)) != null) {
            return;
        } else if ((customCrop = ItemsAdderItemTag.getCustomCrop(block)) != null) {
            return;
        } else if ((customFire = ItemsAdderItemTag.getCustomFire(block)) != null) {
            return;
        }
    }

    public ItemsAdderItemTag(Entity entity) {
        if ((customFurniture = CustomFurniture.byAlreadySpawned(entity)) != null) {
            return;
        } else if ((customEntity = CustomEntity.byAlreadySpawned(entity)) != null) {
            return;
        }
    }

    public ItemsAdderItemTag(CustomStack stack) {
        customStack = stack;
    }

    public ItemsAdderItemTag(CustomFurniture furniture) {
        customFurniture = furniture;
    }

    public Boolean isValid() {
        return !(customFire == null && customBlock == null && customStack == null && customCrop == null
                && customFurniture == null);
    }

    public static CustomStack getCustomStack(String name) {
        try {
            CustomStack item;
            if ((item = CustomStack.getInstance(name)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static CustomStack getCustomStack(ItemStack stack) {
        try {
            CustomStack item;
            if ((item = CustomStack.byItemStack(stack)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static CustomBlock getCustomBlock(String name) {
        try {
            CustomBlock item;
            if ((item = CustomBlock.getInstance(name)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static CustomBlock getCustomBlock(ItemStack stack) {
        try {
            CustomBlock item;
            if ((item = CustomBlock.byItemStack(stack)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static CustomBlock getCustomBlock(Block block) {
        try {
            CustomBlock item;
            if ((item = CustomBlock.byAlreadyPlaced(block)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static CustomFire getCustomFire(String name) {
        try {
            CustomFire item;
            if ((item = CustomFire.getInstance(name)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static CustomFire getCustomFire(ItemStack stack) {
        try {
            CustomFire item;
            if ((item = CustomFire.byItemStack(stack)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static CustomFire getCustomFire(Block block) {
        try {
            CustomFire item;
            if ((item = CustomFire.byAlreadyPlaced(block)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static CustomCrop getCustomCrop(Block block) {
        try {
            CustomCrop item;
            if ((item = CustomCrop.byAlreadyPlaced(block)) != null) {
                return item;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    private String prefix;
    CustomBlock customBlock = null;
    CustomCrop customCrop = null;
    CustomEntity customEntity = null;
    CustomFire customFire = null;
    CustomFurniture customFurniture = null;
    CustomStack customStack = null;

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
        return "itemsadder_item@" + getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    public String getName() {
        if (customBlock != null) {
            return customBlock.getNamespacedID();
        } else if (customCrop != null) {
            return customCrop.getSeed().getNamespacedID();
        } else if (customFire != null) {
            return customFire.getNamespacedID();
        } else if (customFurniture != null) {
            return customFurniture.getNamespacedID();
        } else if (customStack != null) {
            return customStack.getNamespacedID();
        }

        return null;
    }

    public boolean can_drop() {
        return (customBlock != null || customFire != null || customFurniture != null || customStack != null);
    }

    public void drop(Location location) {
        if (customBlock != null) {
            customBlock.drop(location);
        } else if (customFire != null) {
            customFire.drop(location);
        } else if (customFurniture != null) {
            customFurniture.drop(location);
        } else if (customStack != null) {
            customStack.drop(location);
        }
    }

    public boolean can_place() {
        return (customBlock != null || customCrop != null || customFire != null);
    }

    public void place(Location location) {
        if (customBlock != null) {
            customBlock.place(location);
        } else if (customCrop != null) {
            CustomCrop.place(customCrop.getSeed().getNamespacedID(), location);
        } else if (customFire != null) {
            customFire.place(location);
        }
    }

    public boolean can_spawn() {
        return (customFurniture != null);
    }

    public void spawn(Location location) {
        if (customFurniture != null) {
            CustomFurniture.spawnPreciseNonSolid(customFurniture.getNamespacedID(), location);
        }
    }

    public static ObjectTagProcessor<ItemsAdderItemTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {

        // <--[tag]
        // @attribute <ItemsAdderItemTag.age>
        // @returns ElementTag(Number)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the age of the ItemsAdder item
        // -->
        tagProcessor.registerTag(ElementTag.class, "age", (attribute, object) -> {
            if (object.customCrop != null) {
                return new ElementTag(object.customCrop.getAge());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.getAge());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.damage_main_hand>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the amount of damage the ItemsAdder item does in the main hand.
        // -->
        tagProcessor.registerTag(ElementTag.class, "damage_main_hand", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ElementTag(object.customBlock.getDamageMainhand());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.getDamageMainhand());
            } else if (object.customFurniture != null) {
                return new ElementTag(object.customFurniture.getDamageMainhand());
            } else if (object.customStack != null) {
                return new ElementTag(object.customStack.getDamageMainhand());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.display>
        // @returns ElementTag
        // @plugin Depenizen, ItemsAdder
        // @mechanism ItemsAdderItemTag.display
        // @description
        // Returns the ItemsAdder display name.
        // -->
        tagProcessor.registerTag(ElementTag.class, "display", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ElementTag(object.customBlock.getDisplayName());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.getDisplayName());
            } else if (object.customFurniture != null) {
                return new ElementTag(object.customFurniture.getDisplayName());
            } else if (object.customStack != null) {
                return new ElementTag(object.customStack.getDisplayName());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.drops[(<item>)]>
        // @returns ListTag(ItemTag)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns what items the ItemsAdder would drop if broken naturally.
        // Optionally specify a breaker item.
        // -->
        tagProcessor.registerTag(ListTag.class, "drops", (attribute, object) -> {
            ItemStack breaker = null;
            List<ItemStack> dropList = null;

            if (attribute.matches("item") && attribute.hasParam()
                    && attribute.getParamObject().canBeType(ItemTag.class)) {
                breaker = attribute.paramAsType(ItemTag.class).getItemStack();
            }

            if (object.customBlock != null) {
                dropList = object.customBlock.getLoot(breaker, false);
            } else if (object.customCrop != null) {
                dropList = object.customCrop.getLoot(breaker);
            }

            if (dropList != null) {
                ListTag drops = new ListTag();
                for (ItemStack i : dropList) {
                    drops.addObject(new ItemTag(i));
                }

                return drops;
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.durability>
        // @returns ElementTag(Number)
        // @plugin Depenizen, ItemsAdder
        // @mechanism ItemsAdderItemTag.durability
        // @description
        // Returns the durability of the ItemsAdder item.
        // -->
        tagProcessor.registerTag(ElementTag.class, "durability", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ElementTag(object.customBlock.getDurability());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.getDurability());
            } else if (object.customFurniture != null) {
                return new ElementTag(object.customFurniture.getDurability());
            } else if (object.customStack != null) {
                return new ElementTag(object.customStack.getDurability());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the entity of the ItemsAdder item.
        // -->
        tagProcessor.registerTag(EntityTag.class, "entity", (attribute, object) -> {
            if (object.customEntity != null) {
                return new EntityTag(object.customEntity.getEntity());
            } else if (object.customFurniture != null) {
                return new EntityTag(object.customFurniture.getArmorstand());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.has_custom_durability>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the if the ItemsAdder has custom durability.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_custom_durability", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ElementTag(object.customBlock.hasCustomDurability());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.hasCustomDurability());
            } else if (object.customStack != null) {
                return new ElementTag(object.customStack.hasCustomDurability());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.has_permission>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the if the ItemsAdder has a permission associated.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_permission", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ElementTag(object.customBlock.hasPermission());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.hasPermission());
            } else if (object.customFurniture != null) {
                return new ElementTag(object.customFurniture.hasPermission());
            } else if (object.customStack != null) {
                return new ElementTag(object.customStack.hasPermission());
            }

            return new ElementTag(false);
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.has_uses>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the if the ItemsAdder has uses attribute.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_uses", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ElementTag(object.customBlock.hasUsagesAttribute());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.hasUsagesAttribute());
            } else if (object.customFurniture != null) {
                return new ElementTag(object.customFurniture.hasUsagesAttribute());
            } else if (object.customStack != null) {
                return new ElementTag(object.customStack.hasUsagesAttribute());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.is_block>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the ItemsAdder item is block.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_block", (attribute, object) -> {
            return new ElementTag(
                    object.customBlock != null || (object.customStack != null && object.customStack.isBlock()));
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.is_crop>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the ItemsAdder item is crop.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_crop", (attribute, object) -> {
            return new ElementTag(object.customCrop != null);
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.is_entity>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the ItemsAdder item is entity.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_entity", (attribute, object) -> {
            return new ElementTag(object.customEntity != null);
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.is_fire>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the ItemsAdder item is fire.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_fire", (attribute, object) -> {
            return new ElementTag(object.customFire != null);
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.is_fully_grown>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @mechanism ItemsAdderItemTag.fully_grown
        // @description
        // Returns if the ItemsAdder item is fully grown.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_fully_grown", (attribute, object) -> {
            if (object.customCrop != null) {
                return new ElementTag(object.customCrop.isFullyGrown());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.is_furniture>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns if the ItemsAdder item is furniture.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_furniture", (attribute, object) -> {
            return new ElementTag(object.customFurniture != null);
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.item>
        // @returns ItemTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the ItemsAdder as an ItemTag.
        // -->
        tagProcessor.registerTag(ItemTag.class, "item", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ItemTag(object.customBlock.getItemStack());
            } else if (object.customFire != null) {
                return new ItemTag(object.customFire.getItemStack());
            } else if (object.customFurniture != null) {
                return new ItemTag(object.customFurniture.getItemStack());
            } else if (object.customStack != null) {
                return new ItemTag(object.customStack.getItemStack());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.max_durability>
        // @returns ElementTag(Number)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the max durability of the ItemsAdder item.
        // -->
        tagProcessor.registerTag(ElementTag.class, "max_durability", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ElementTag(object.customBlock.getMaxDurability());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.getMaxDurability());
            } else if (object.customFurniture != null) {
                return new ElementTag(object.customFurniture.getMaxDurability());
            } else if (object.customStack != null) {
                return new ElementTag(object.customStack.getMaxDurability());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.max_age>
        // @returns ElementTag(Number)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the maximum age of an ItemsAdder item.
        // -->
        tagProcessor.registerTag(ElementTag.class, "max_age", (attribute, object) -> {
            if (object.customCrop != null) {
                return new ElementTag(object.customCrop.getMaxAge());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.name>
        // @returns ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the name ItemsAdder identifies the item.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.getName());
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.permission>
        // @returns ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Returns the permission of the ItemsAdder item.
        // -->
        tagProcessor.registerTag(ElementTag.class, "permission", (attribute, object) -> {
            String permission = null;

            if (object.customBlock != null) {
                permission = object.customBlock.getPermission();
            } else if (object.customFire != null) {
                permission = object.customFire.getPermission();
            } else if (object.customFurniture != null) {
                permission = object.customFurniture.getPermission();
            } else if (object.customStack != null) {
                permission = object.customStack.getPermission();
            }

            if (permission != null) {
                return new ElementTag(permission);
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.seed>
        // @returns ItemsAdderItemTag
        // @plugin Depenizen, ItemsAdder
        // @mechanism ItemsAdderItemTag.uses
        // @description
        // Returns the number of uses of the ItemsAdder item.
        // -->
        tagProcessor.registerTag(ItemsAdderItemTag.class, "seed", (attribute, object) -> {
            if (object.customCrop != null) {
                return new ItemsAdderItemTag(object.customCrop.getSeed());
            }

            return null;
        });

        // <--[tag]
        // @attribute <ItemsAdderItemTag.uses>
        // @returns ElementTag(Number)
        // @plugin Depenizen, ItemsAdder
        // @mechanism ItemsAdderItemTag.uses
        // @description
        // Returns the number of uses of the ItemsAdder item.
        // -->
        tagProcessor.registerTag(ElementTag.class, "uses", (attribute, object) -> {
            if (object.customBlock != null) {
                return new ElementTag(object.customBlock.getUsages());
            } else if (object.customFire != null) {
                return new ElementTag(object.customFire.getUsages());
            } else if (object.customFurniture != null) {
                return new ElementTag(object.customFurniture.getUsages());
            } else if (object.customStack != null) {
                return new ElementTag(object.customStack.getUsages());
            }

            return null;
        });
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply properties to a ItemsAdder item!");
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object ItemsAdderItemTag
        // @name age
        // @input ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Set the age of the ItemsAdder item.
        // @tags
        // <ItemsAdderItemTag.age>
        // -->
        if (mechanism.matches("age")) {
            if (customCrop != null) {
                customCrop.setAge(mechanism.getValue().asInt());
            } else if (customFire != null) {
                customFire.setAge(mechanism.getValue().asInt());
            } else {
                mechanism.echoError("ItemsAdder item does not use age.");
            }
        }

        // <--[mechanism]
        // @object ItemsAdderItemTag
        // @name destroy
        // @input ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Removes the item from the world
        // @tags
        // <ItemsAdderItemTag.destroy>
        // -->
        if (mechanism.matches("destroy")) {
            if (customEntity != null) {
                customEntity.destroy();
            } else if (customFurniture != null) {
                customFurniture.remove(false);
            } else {
                mechanism.echoError("ItemsAdder item does not use destroy.");
            }
        }

        // <--[mechanism]
        // @object ItemsAdderItemTag
        // @name display
        // @input ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Set the display name of the ItemsAdder item.
        // @tags
        // <ItemsAdderItemTag.display>
        // -->
        if (mechanism.matches("display")) {
            if (customBlock != null) {
                customBlock.setDisplayName(mechanism.getValue().asString());
            } else if (customFire != null) {
                customFire.setDisplayName(mechanism.getValue().asString());
            } else if (customFurniture != null) {
                customFurniture.setDisplayName(mechanism.getValue().asString());
            } else if (customStack != null) {
                customStack.setDisplayName(mechanism.getValue().asString());
            } else {
                mechanism.echoError("ItemsAdder item does not use display.");
            }
        }

        // <--[mechanism]
        // @object ItemsAdderItemTag
        // @name durability
        // @input ElementTag
        // @plugin Depenizen, ItemsAdder
        // @description
        // Set the durability of the ItemsAdder item.
        // @tags
        // <ItemsAdderItemTag.durability>
        // -->
        else if (mechanism.matches("durability")) {
            if (customBlock != null) {
                customBlock.setDurability(mechanism.getValue().asInt());
            } else if (customFire != null) {
                customFire.setDurability(mechanism.getValue().asInt());
            } else if (customFurniture != null) {
                customFurniture.setDurability(mechanism.getValue().asInt());
            } else if (customStack != null) {
                customStack.setDurability(mechanism.getValue().asInt());
            } else {
                mechanism.echoError("ItemsAdder item does not use durability.");
            }
        }

        // <--[mechanism]
        // @object ItemsAdderItemTag
        // @name fully_grown
        // @input
        // @plugin Depenizen, ItemsAdder
        // @description
        // Set the ItemsAdder item to be fully grown.
        // @tags
        // <ItemsAdderItemTag.is_fully_grown>
        // -->
        else if (mechanism.matches("fully_grown")) {
            if (customCrop != null) {
                customCrop.setFullyGrown();
            } else {
                mechanism.echoError("ItemsAdder item does not use fully_grown.");
            }
        }

        // <--[mechanism]
        // @object ItemsAdderItemTag
        // @name uses
        // @input ElementTag(Number)
        // @plugin Depenizen, ItemsAdder
        // @description
        // Sets the number of uses
        // @tags
        // <ItemsAdderItemTag.uses>
        // -->
        else if (mechanism.matches("uses") && mechanism.requireInteger()) {
            if (customBlock != null) {
                customBlock.setUsages(mechanism.getValue().asInt());
            } else if (customFire != null) {
                customFire.setUsages(mechanism.getValue().asInt());
            } else if (customFurniture != null) {
                customFurniture.setUsages(mechanism.getValue().asInt());
            } else if (customStack != null) {
                customStack.setUsages(mechanism.getValue().asInt());
            } else {
                mechanism.echoError("ItemsAdder item does not use uses.");
            }
        }

        CoreUtilities.autoPropertyMechanism(this, mechanism);
        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }
    }
}
