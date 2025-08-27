package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetUtils;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetValues;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

import static tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetValues.renderDistance;
import static tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.MagnetFieldManagerSelector.getClientManager;

public final class MagnetFieldRenderEngine {
    //防GC以及传参优化
    static final Vector3f any0 = new Vector3f(1, 0, 0),
            any1 = new Vector3f(0, 0, 1).normalize();
    static Matrix4f poseMat;
    static Matrix3f norMat;
    static int colorR, colorG, colorB,colorA;
    static VertexConsumer lineConsumer, stripConsumer;
    static Vector3f tmpVec = new Vector3f();
    static Vector3f pos = new Vector3f();
    static Vector3f beginVec = new Vector3f(), endVec = new Vector3f();
    static Vector3f directionVec = new Vector3f(); //normalized
    static Vector3f orth1 = new Vector3f(), orth2 = new Vector3f();
    static Vector3f[] nodes = new Vector3f[4];
    static {
        for (int i = 0; i < nodes.length; i++) nodes[i] = new Vector3f();
    }



    public static void tick(RenderLevelStageEvent event){
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {

            var mc = Minecraft.getInstance();
            var player = mc.player;
            var mgr = getClientManager(mc.level);
            int playerChunkPosX = SectionPos.blockToSectionCoord(player.getX());
            int playerChunkPosZ = SectionPos.blockToSectionCoord(player.getZ());

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

            poseStack.pushPose();

            Camera camera = mc.gameRenderer.getMainCamera();
            poseStack.translate(-camera.getPosition().x,
                    -camera.getPosition().y,
                    -camera.getPosition().z);

            poseMat = poseStack.last().pose();
            norMat = poseStack.last().normal();

            for(int i=-renderDistance;i<=renderDistance;i++) for(int j=-renderDistance;j<=renderDistance;j++) {
                var map = mgr.getChunkMagnetFields(playerChunkPosX+i,playerChunkPosZ+j);
                for (var entry : map.long2ObjectEntrySet()) {
                    MagnetVector vec = entry.getValue();
                    final float length = vec.length();
                    if (length < MagnetValues.MagMin) continue;

                    //计算颜色和长度
                    //颜色根据磁场强度等级计算
                    //长度根据与等级标准之比决定, (1,2) -> (1/2,1)
                    final int tier = MagnetUtils.getMagVTier(length);
                    final float halfScale = length / MagnetValues.MagV[tier] / 4f;

                    //计算渲染位置
                    pos.x = BlockPos.getX(entry.getLongKey()) + 0.5f;
                    pos.y = BlockPos.getY(entry.getLongKey()) + 0.5f;
                    pos.z = BlockPos.getZ(entry.getLongKey()) + 0.5f;

                    //tmp
                    final var halfVec = vec.normalize(halfScale, tmpVec);
                    pos.sub(halfVec, beginVec);
                    pos.add(halfVec, endVec);
                    halfVec.div(halfScale, directionVec);

                    final int packedColor = MagnetValues.MagColor[tier];
                    colorR = (packedColor >> 16) & 0xFF;
                    colorG = (packedColor >> 8) & 0xFF;
                    colorB = packedColor & 0xFF;
                    colorA = (packedColor >> 24) & 0xFF;

                    //寻找d的正交,e1,e2为正交基
                    orth1 = any0;
                    if (directionVec.cross(orth1, tmpVec).lengthSquared() == 0)
                        orth1 = any1;
                    directionVec.cross(orth1, orth2);

                    //头部1/3画箭头
                    final float quadScale = halfScale / 2;
                    beginVec.fma(quadScale*3, directionVec, tmpVec); //底中心
                    tmpVec.fma(quadScale, orth1, nodes[0]);
                    tmpVec.fma(quadScale, orth2, nodes[1]);
                    tmpVec.fma(-quadScale, orth1, nodes[2]);
                    tmpVec.fma(-quadScale, orth2, nodes[3]);

                    //渲染开始
                    lineConsumer = bufferSource.getBuffer(RenderType.lines());
                    stripConsumer = bufferSource.getBuffer(RenderType.lineStrip());

                    //棍子
                    line(beginVec, endVec);
                    //侧棱
                    line(nodes[0], endVec);
                    line(nodes[1], endVec);
                    line(nodes[2], endVec);
                    line(nodes[3], endVec);
                    //底方形
                    strip(nodes[0], nodes[1], nodes[2], nodes[3], nodes[0]);
                }
            }
            poseStack.popPose();
        }
    }

    static Vector3f norVec = new Vector3f();

    static void line(Vector3f b, Vector3f e) {
        e.sub(b,norVec).normalize();
        lineConsumer.vertex(poseMat, b.x, b.y, b.z)
                .color(colorR, colorG, colorB, colorA)
                .normal(norMat, norVec.x, norVec.y, norVec.z)
                .endVertex();
        lineConsumer.vertex(poseMat, e.x, e.y, e.z)
                .color(colorR, colorG, colorB, colorA)
                .normal(norMat, norVec.x, norVec.y, norVec.z)
                .endVertex();
    }
    static void strip(Vector3f... vecs) {
        int a=0,b=1;
        do{
            vecs[b].sub(vecs[a],norVec).normalize();
            stripConsumer.vertex(poseMat, vecs[a].x, vecs[a].y, vecs[a].z)
                    .color(colorR, colorG, colorB, colorA)
                    .normal(norMat, norVec.x, norVec.y, norVec.z)
                    .endVertex();
            stripConsumer.vertex(poseMat, vecs[b].x, vecs[b].y, vecs[b].z)
                    .color(colorR, colorG, colorB, colorA)
                    .normal(norMat, norVec.x, norVec.y, norVec.z)
                    .endVertex();
            a=b++;
        }while(b<vecs.length);
    }
}
