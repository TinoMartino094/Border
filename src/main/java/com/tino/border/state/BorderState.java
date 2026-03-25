package com.tino.border.state;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class BorderState extends SavedData {

    public static final Codec<BorderState> CODEC = Codec.INT
            .fieldOf("dimension_deposited_funds")
            .codec()
            .xmap(BorderState::new, state -> state.dimension_deposited_funds);

    public static final SavedDataType<BorderState> TYPE = new SavedDataType<>(
            Identifier.withDefaultNamespace("growing_border_state"),
            BorderState::new,
            CODEC,
            DataFixTypes.LEVEL);

    public int dimension_deposited_funds = 0;

    public BorderState() {
        super();
    }

    public BorderState(int funds) {
        super();
        this.dimension_deposited_funds = funds;
    }

    public static BorderState getServerState(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(TYPE);
    }
}
