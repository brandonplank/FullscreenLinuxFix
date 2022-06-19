package org.crypticplank.fullscreenfix.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.logging.Logger;

@Environment(EnvType.CLIENT)
public class FullscreenFixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Logger.getGlobal().info("Prepping Linux fix");
    }
}
