// src/client/java/itemadvisor/mixin/client/HandledScreenAccessor.java
package itemadvisor.mixin.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor to get GUI position offsets for proper overlay alignment.
 */
@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor("x")
    int getLeft();

    @Accessor("y")
    int getTop();
}