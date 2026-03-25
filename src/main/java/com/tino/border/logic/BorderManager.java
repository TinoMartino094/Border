package com.tino.border.logic;

import com.tino.border.GrowingBorder;
import com.tino.border.state.BorderState;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.WorldBorder;

public class BorderManager {

    public static void addFunds(ServerLevel level, int amount) {
        BorderState state = BorderState.getServerState(level);
        state.dimension_deposited_funds += amount;
        state.setDirty();

        checkThreshold(level, state);
    }

    private static void checkThreshold(ServerLevel level, BorderState state) {
        WorldBorder border = level.getWorldBorder();

        while (true) {
            // Calculate cost threshold: floor(((4*n) + 4) / chunks_per_item) with min 1.
            double current_n = border.getSize() / 16.0;
            int targetCost = (int) Math.max(1,
                    Math.floor(((4 * current_n) + 4) / GrowingBorder.CONFIG.chunks_per_item));

            if (state.dimension_deposited_funds >= targetCost) {
                // We have enough to expand!
                state.dimension_deposited_funds -= targetCost;
                state.setDirty();

                // Expand border size by exactly 32 blocks (1 chunk in all directions means +2
                // chunks diameter).
                border.setSize(border.getSize() + 32.0);

                // 4. Broadcast
                level.getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal("§aThe border in " + level.dimension().identifier().getPath() + " expanded!"),
                        false);
            } else {
                // Not enough funds for another tier loop, break out.
                break;
            }
        }
    }
}
