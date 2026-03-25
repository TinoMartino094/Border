package com.tino.border;

import com.tino.border.config.BorderConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrowingBorder implements ModInitializer {
	public static final String MOD_ID = "growing-border";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static BorderConfig CONFIG;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Growing Border...");
		CONFIG = BorderConfig.load();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			com.tino.border.command.BorderCommand.register(dispatcher, registryAccess);
		});

		com.tino.border.network.TabListUpdater.register();
	}
}