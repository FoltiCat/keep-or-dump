// src/client/java/itemadvisor/core/ItemRating.java
package itemadvisor.core;

import net.minecraft.util.Formatting;

/**
 * Rating categories with associated transparent colors and text formatting.
 */
public enum ItemRating {
    // Transparent ARGB colors so items remain visible under the overlay.
    TRASH(0x66808080, Formatting.GRAY),
    OKAY(0x66A08000, Formatting.YELLOW),
    VALUABLE(0x662060C0, Formatting.BLUE),
    ESSENTIAL(0x66209040, Formatting.GREEN);

    public final int argb;
    public final Formatting formatting;

    ItemRating(int argb, Formatting formatting) {
        this.argb = argb;
        this.formatting = formatting;
    }
}