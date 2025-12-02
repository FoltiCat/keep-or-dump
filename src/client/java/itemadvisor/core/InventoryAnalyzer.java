// src/client/java/itemadvisor/core/InventoryAnalyzer.java
package itemadvisor.core;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scans the handled screen every frame and rates each slot's item.
 * Maintains lightweight per-slot fade-in and global glow time.
 */
public final class InventoryAnalyzer {
    private final RatingPolicy policy = new RatingPolicy();

    private final Map<Integer, ItemRating> ratings = new HashMap<>();
    private final Map<Integer, Float> fadeProgress = new HashMap<>();
    private float glowTime = 0f;

    public void resetAnimations() {
        fadeProgress.clear();
        glowTime = 0f;
    }

    public void update(HandledScreen<?> screen, float delta) {
        if (screen == null) return;
        final List<Slot> slots = screen.getScreenHandler().slots;
        for (Slot slot : slots) {
            final int idx = slot.getIndex();
            final ItemStack stack = slot.getStack();
            final ItemRating rating = policy.rate(stack);
            ratings.put(idx, rating);
            final float p = fadeProgress.getOrDefault(idx, 0f);
            fadeProgress.put(idx, Math.min(1f, p + delta * 3f));
        }
        glowTime += delta;
    }

    public ItemRating getRating(int slotIndex) {
        return ratings.getOrDefault(slotIndex, ItemRating.TRASH);
    }

    public float getFadeProgress(int slotIndex) {
        return fadeProgress.getOrDefault(slotIndex, 0f);
    }

    public float getGlowAlpha() {
        return 0.25f + 0.25f * (float) Math.sin(glowTime * (Math.PI * 2f / 1.5f));
    }
}