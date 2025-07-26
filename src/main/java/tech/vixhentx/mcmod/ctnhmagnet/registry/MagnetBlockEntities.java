package tech.vixhentx.mcmod.ctnhmagnet.registry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import tech.vixhentx.mcmod.ctnhmagnet.common.blockentity.MagnetProviderBE;

import static tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetRegistration.REGISTRATE;

public class MagnetBlockEntities {
    public static void init(){

    }
    public static BlockEntityEntry<MagnetProviderBE> MAGNET_BLOCK_ENTITY = REGISTRATE.blockEntity("magnet_block_entity", MagnetProviderBE::new)
            .validBlock(MagnetBlocks.MAGNET_BLOCK)
            .register();
}
