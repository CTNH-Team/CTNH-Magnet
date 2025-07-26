package tech.vixhentx.mcmod.ctnhmagnet.client.render;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import tech.vixhentx.mcmod.ctnhmagnet.api.utils.MagnetFieldManager;
import tech.vixhentx.mcmod.ctnhmagnet.common.datamodel.MagnetVector;

public class MagnetFieldRenderer{

    public static void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float partialTick) {
        var mc = Minecraft.getInstance();
        var player = mc.player;
        var chunk = mc.level.getChunkAt(player.blockPosition());
        var map = MagnetFieldManager.getMagnetFields(player.getOnPos(), mc.level);
        for (var entry : map.long2ObjectEntrySet()) {
            BlockPos pos = BlockPos.of(entry.getLongKey());
            MagnetVector vec = entry.getValue();
        }
    }
}
