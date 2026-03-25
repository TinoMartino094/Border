package com.tino.border.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.tino.border.GrowingBorder;
import com.tino.border.logic.BorderManager;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class BorderCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(Commands.literal("border")
                .then(Commands.literal("grow")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes(context -> executeGrow(context.getSource(),
                                        IntegerArgumentType.getInteger(context, "amount")))))
                .then(Commands.literal("coords")
                        .executes(context -> executeCoords(context.getSource()))));
    }

    private static int executeGrow(CommandSourceStack source, int amountToDeposit) {
        ServerPlayer player = source.getPlayer();
        if (player == null)
            return 0;

        Item currencyItem = BuiltInRegistries.ITEM.get(Identifier.tryParse(GrowingBorder.CONFIG.expansion_currency))
                .map(net.minecraft.core.Holder::value)
                .orElse(null);
        if (currencyItem == null) {
            source.sendFailure(Component.literal("Invalid config currency item!"));
            return 0;
        }

        int available = countItems(player, currencyItem);
        if (available < amountToDeposit) {
            source.sendFailure(Component.literal("You don't have enough " + currencyItem.getDescriptionId()
                    + " (Need " + amountToDeposit + ", have " + available + ")"));
            return 0;
        }

        takeItems(player, currencyItem, amountToDeposit);
        BorderManager.addFunds(source.getLevel(), amountToDeposit);
        source.sendSuccess(() -> Component.literal("Deposited " + amountToDeposit + " towards border expansion!"),
                false);

        return 1;
    }

    private static int countItems(ServerPlayer player, Item item) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private static void takeItems(ServerPlayer player, Item item, int amount) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                int take = Math.min(stack.getCount(), remaining);
                stack.shrink(take);
                remaining -= take;
                if (remaining <= 0) {
                    break;
                }
            }
        }
    }

    private static int executeCoords(CommandSourceStack source) {
        net.minecraft.world.level.border.WorldBorder border = source.getLevel().getWorldBorder();

        int minX = (int) Math.floor(border.getMinX());
        int maxX = (int) Math.floor(border.getMaxX());
        int minZ = (int) Math.floor(border.getMinZ());
        int maxZ = (int) Math.floor(border.getMaxZ());

        source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                "§bBorder Bounds: §fX: [" + minX + " to " + maxX + "] §7| §fZ: [" + minZ + " to " + maxZ + "]"), false);

        return 1;
    }
}
