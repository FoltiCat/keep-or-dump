// src/client/java/itemadvisor/core/RatingPolicy.java
package itemadvisor.core;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

/**
 * Simple, fast heuristics to classify items for UI highlighting.
 */
public final class RatingPolicy {
    public ItemRating rate(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return ItemRating.TRASH;
        final Item item = stack.getItem();
        if (isEssential(item, stack)) return ItemRating.ESSENTIAL;
        if (isValuable(item, stack)) return ItemRating.VALUABLE;
        if (isOkay(item, stack)) return ItemRating.OKAY;
        return ItemRating.TRASH;
    }

    private boolean isEssential(Item item, ItemStack stack) {
        if (item == Items.NETHERITE_SWORD || item == Items.NETHERITE_PICKAXE || item == Items.NETHERITE_AXE
                || item == Items.NETHERITE_SHOVEL || item == Items.NETHERITE_HOE || item == Items.NETHERITE_HELMET
                || item == Items.NETHERITE_CHESTPLATE || item == Items.NETHERITE_LEGGINGS || item == Items.NETHERITE_BOOTS) return true;
        if (item == Items.DIAMOND_SWORD || item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_AXE
                || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_HOE) return true;
        if (item == Items.ELYTRA || item == Items.SHULKER_BOX || item == Items.ENDER_CHEST) return true;
        if (item == Items.ENCHANTED_GOLDEN_APPLE || item == Items.TOTEM_OF_UNDYING) return true;
        if (stack.getRarity() == Rarity.EPIC) return true;
        return false;
    }

    private boolean isValuable(Item item, ItemStack stack) {
        if (item == Items.DIAMOND || item == Items.EMERALD || item == Items.NETHERITE_INGOT || item == Items.GOLD_INGOT) return true;
        if (item == Items.ANCIENT_DEBRIS || item == Items.NETHER_STAR || item == Items.DRAGON_EGG) return true;
        if (item == Items.ENCHANTED_BOOK) return true;
        if (stack.getRarity() == Rarity.RARE) return true;
        if (stack.hasEnchantments()) return true;
        if (item == Items.ENDER_PEARL || item == Items.GOLDEN_APPLE || item == Items.EXPERIENCE_BOTTLE) return true;
        return stack.isDamageable() && isDurableUseful(stack);
    }

    private boolean isOkay(Item item, ItemStack stack) {
        if (stack.isDamageable() && isDurableUseful(stack)) return true;
        if (stack.get(DataComponentTypes.FOOD) != null) return true;
        if (item == Items.TORCH || item == Items.CHEST || item == Items.FURNACE || item == Items.CRAFTING_TABLE) return true;
        if (item == Items.BUCKET || item == Items.WATER_BUCKET || item == Items.LAVA_BUCKET) return true;
        return false;
    }

    private boolean isDurableUseful(ItemStack stack) {
        final int max = stack.getMaxDamage();
        final int dmg = stack.getDamage();
        if (max <= 0) return true;
        final float remaining = (max - dmg) / (float) max;
        return remaining >= 0.25f;
    }
}