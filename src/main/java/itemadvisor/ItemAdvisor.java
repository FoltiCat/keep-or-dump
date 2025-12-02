package itemadvisor;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ItemAdvisor server/common initializer.
 * Currently minimal because the mod is purely client-side visual.
 */
public class ItemAdvisor implements ModInitializer {
    public static final String MOD_ID = "itemadvisor";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // No server-side mechanics; kept for potential future extensions.
        LOGGER.info("ItemAdvisor initialized");
    }
}

