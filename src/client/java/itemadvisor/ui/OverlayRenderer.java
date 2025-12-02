// src/client/java/itemadvisor/ui/OverlayRenderer.java
package itemadvisor.ui;

import itemadvisor.core.InventoryAnalyzer;
import itemadvisor.core.ItemRating;
import itemadvisor.mixin.client.HandledScreenAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

/**
 * Draws transparent overlays and subtle glow on rated items.
 * Shows short text on hover without causing flicker.
 */
public final class OverlayRenderer {
    private final InventoryAnalyzer analyzer;

    public OverlayRenderer(InventoryAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void render(HandledScreen<?> screen, DrawContext ctx, int mouseX, int mouseY, float delta) {
        final int left = ((HandledScreenAccessor) screen).getLeft();
        final int top = ((HandledScreenAccessor) screen).getTop();

        for (Slot slot : screen.getScreenHandler().slots) {
            final int sx = left + slot.x;
            final int sy = top + slot.y;

            final ItemRating rating = analyzer.getRating(slot.getIndex());
            final float fade = analyzer.getFadeProgress(slot.getIndex());
            final int baseColor = applyAlpha(rating.argb, fade);

            if (!slot.getStack().isEmpty()) {
                ctx.fill(sx, sy, sx + 16, sy + 16, baseColor);

                if (rating == ItemRating.VALUABLE || rating == ItemRating.ESSENTIAL) {
                    final float glow = analyzer.getGlowAlpha();
                    final int glowColor = applyAlpha(0x66FFFFFF, glow * fade);
                    drawGlowRect(ctx, sx - 1, sy - 1, sx + 17, sy + 17, glowColor);
                }
            }

            if (isMouseOver(mouseX, mouseY, sx, sy)) {
                final Text tip = Text.literal(describe(slot.getStack(), rating)).formatted(rating.formatting);
                ctx.drawTooltip(net.minecraft.client.MinecraftClient.getInstance().textRenderer, tip, mouseX, mouseY);
            }
        }
    }

    private String describe(ItemStack stack, ItemRating rating) {
        if (stack.isEmpty()) return "Empty";
        return switch (rating) {
            case TRASH -> "Trash";
            case OKAY -> "Common";
            case VALUABLE -> "Rare";
            case ESSENTIAL -> "Essential";
        };
    }

    private boolean isMouseOver(int mouseX, int mouseY, int sx, int sy) {
        return mouseX >= sx && mouseX < sx + 16 && mouseY >= sy && mouseY < sy + 16;
    }

    private int applyAlpha(int argb, float mul) {
        mul = Math.max(0f, Math.min(1f, mul));
        final int a = (int) (((argb >>> 24) & 0xFF) * mul);
        return (a << 24) | (argb & 0x00FFFFFF);
    }

    private void drawGlowRect(DrawContext ctx, int x1, int y1, int x2, int y2, int color) {
        ctx.fill(x1, y1, x2, y1 + 1, color);
        ctx.fill(x1, y2 - 1, x2, y2, color);
        ctx.fill(x1, y1, x1 + 1, y2, color);
        ctx.fill(x2 - 1, y1, x2, y2, color);
    }
}