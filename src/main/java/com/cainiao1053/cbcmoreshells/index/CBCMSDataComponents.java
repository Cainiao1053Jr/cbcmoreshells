package com.cainiao1053.cbcmoreshells.index;

import java.util.function.UnaryOperator;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;

public class CBCMSDataComponents {
    public static final DataComponentType<Boolean> AUTOCANNON_TRACER = register(
        "autocannon_tracer",
        builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Cbcmoreshells.resource(name), builder.apply(DataComponentType.builder()).build());
    }

    public static void init() {}

}
