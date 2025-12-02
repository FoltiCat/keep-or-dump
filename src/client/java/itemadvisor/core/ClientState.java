package itemadvisor.core;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Holds client-side toggle state and manages the keybind.
 * Default key: O. Press to toggle the overlay on/off.
 */
public final class ClientState {
    private boolean enabled = true;
    private final KeyBinding toggleKey;

    {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.itemadvisor.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                net.minecraft.client.option.KeyBinding.Category.MISC
        ));
    }

    public ClientState() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
            }
        });
    }

    public boolean isEnabled() {
        return enabled;
    }
}

