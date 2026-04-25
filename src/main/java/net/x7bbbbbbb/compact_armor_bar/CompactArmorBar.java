package net.x7bbbbbbb.compact_armor_bar;

import net.fabricmc.api.ModInitializer;
import net.x7bbbbbbb.compact_armor_bar.config.CompactArmorBarConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompactArmorBar implements ModInitializer {
	public static final String MOD_ID = "compact-armor-bar";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final CompactArmorBarConfig CONFIG = CompactArmorBarConfig.createAndLoad();

	@Override
	public void onInitialize() {
		LOGGER.info("Loaded Compact Armor Bar mod by x7b!");
	}
}