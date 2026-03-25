package com.tino.border.network;

import com.tino.border.GrowingBorder;
import com.tino.border.state.BorderState;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.WorldBorder;

public class TabListUpdater {

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> updateTabList(server));
    }

    private static void updateTabList(MinecraftServer server) {
        if (server.getTickCount() % 20 != 0)
            return; // Update once per second

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerLevel serverLevel = (ServerLevel) player.level();
            BorderState state = BorderState.getServerState(serverLevel);
            WorldBorder border = serverLevel.getWorldBorder();

            double current_n = border.getSize() / 16.0;
            int targetCost = (int) Math.max(1,
                    Math.floor(((4 * current_n) + 4) / GrowingBorder.CONFIG.chunks_per_item));

            String currencyName = GrowingBorder.CONFIG.expansion_currency;
            if (currencyName.contains(":")) {
                currencyName = currencyName.split(":")[1].replace("_", " ");
            }

            // Capitalize fully
            String[] words = currencyName.split(" ");
            StringBuilder fullyCapitalized = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    fullyCapitalized.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1))
                            .append(" ");
                }
            }
            currencyName = fullyCapitalized.toString().trim();

            Component header = Component.literal("§6§lBorder Progress");
            Component footer = Component
                    .literal("§e" + state.dimension_deposited_funds + " / " + targetCost + " §7" + currencyName);

            player.connection.send(new ClientboundTabListPacket(header, footer));
        }
    }
}
