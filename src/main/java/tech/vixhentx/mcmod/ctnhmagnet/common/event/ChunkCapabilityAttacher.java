package tech.vixhentx.mcmod.ctnhmagnet.common.event;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhmagnet.CTNHMagnet;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetChunkStorage;
import tech.vixhentx.mcmod.ctnhmagnet.common.chunkdata.MagnetChunkStorage;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

@Mod.EventBusSubscriber(modid = CTNHMagnet.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChunkCapabilityAttacher {
    private static final ResourceLocation MAGNETIC_FIELD_ID = CTNHMagnet.ID("magnetic_field_storage");

    @SubscribeEvent
    public static void onAttachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        final MagnetChunkStorage storage = new MagnetChunkStorage(event.getObject());
        final LazyOptional<IMagnetChunkStorage> lazyOptional = LazyOptional.of(() -> storage);

        ICapabilitySerializable<CompoundTag> provider = new ICapabilitySerializable<>() {
            @Override
            public CompoundTag serializeNBT() {
                return storage.serializeNBT();
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                storage.deserializeNBT(nbt);
            }

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == MagnetCapabilities.CAPABILITY_MAGNET_CHUNK_STORAGE) {
                    return lazyOptional.cast();
                }
                return LazyOptional.empty();
            }
        };

        event.addCapability(MAGNETIC_FIELD_ID, provider);

        event.addListener(lazyOptional::invalidate);
    }
}
