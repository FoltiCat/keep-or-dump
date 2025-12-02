// src/client/java/itemadvisor/ItemAdvisorClient.java
package itemadvisor;

import itemadvisor.core.ClientState;
import itemadvisor.core.InventoryAnalyzer;
import itemadvisor.ui.OverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.Screen;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Client initializer that hooks inventory/container screens and draws overlays.
 * - Registers per-screen afterRender with a duplicate guard to avoid flicker
 * - Resets animation state when the screen initializes
 * - Checks keybind toggle before drawing
 */
public class ItemAdvisorClient implements ClientModInitializer {
    private final InventoryAnalyzer analyzer = new InventoryAnalyzer();
    private final OverlayRenderer overlay = new OverlayRenderer(analyzer);
    private final ClientState state = new ClientState();
    private final Set<Screen> registered = Collections.newSetFromMap(new WeakHashMap<>());

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            analyzer.resetAnimations();
            if (screen instanceof HandledScreen<?>) {
                if (registered.add(screen)) {
                    HandledScreen<?> handled = (HandledScreen<?>) screen;
                    ScreenEvents.afterRender(screen).register((s, ctx, mouseX, mouseY, delta) -> {
                        if (!state.isEnabled()) return;
                        analyzer.update(handled, delta);
                        overlay.render(handled, ctx, mouseX, mouseY, delta);
                    });
                    ScreenEvents.remove(screen).register(s -> registered.remove(screen));
                }
            }
        });
    }
}