package tech.vixhentx.mcmod.ctnhmagnet.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import tech.vixhentx.mcmod.ctnhmagnet.api.utils.MagnetFieldManager;
import tech.vixhentx.mcmod.ctnhmagnet.common.datamodel.MagnetVector;

public class MagnetFieldRenderEvent {
    public static void tick(RenderLevelStageEvent event){
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

            poseStack.pushPose();

            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            poseStack.translate(-camera.getPosition().x,
                    -camera.getPosition().y,
                    -camera.getPosition().z);

            // 获取渲染参数
            float scale = 0.3f; // 箭头缩放比例
            float minVectorLength = 0.1f; // 最小可显示矢量长度
            float maxVectorLength = 1.5f; // 最大矢量长度

            // 设置颜色渐变（蓝到红表示磁场强度）
            int minColor = 0xFF0000FF; // 蓝色（弱场）
            int maxColor = 0xFFFF0000; // 红色（强场）

            // 创建顶点消费者
            VertexConsumer lineConsumer = bufferSource.getBuffer(RenderType.lines());
            VertexConsumer arrowConsumer = bufferSource.getBuffer(RenderType.LINE_STRIP);

            // 获取当前帧的变换矩阵
            Matrix4f poseMatrix = poseStack.last().pose();
            Matrix3f normalMatrix = poseStack.last().normal();

            // 遍历所有磁场矢量

            var mc = Minecraft.getInstance();
            var player = mc.player;
            if (player == null || mc.level == null) return;

            var map = MagnetFieldManager.getMagnetFields(player.getOnPos(), mc.level);
            if (map == null || map.isEmpty()) return;

            for (var entry : map.long2ObjectEntrySet()) {
                BlockPos pos = BlockPos.of(entry.getLongKey());
                MagnetVector vec = entry.getValue();

                // 计算世界坐标中的位置
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 0.5;
                double z = pos.getZ() + 0.5;

                // 获取矢量分量
                double vx = vec.x();
                double vy = vec.y();
                double vz = vec.z();

                // 计算矢量长度
                double length = Math.sqrt(vx * vx + vy * vy + vz * vz);
                if (length < minVectorLength) continue; // 忽略太小的矢量

                // 归一化方向
                double nx = vx / length;
                double ny = vy / length;
                double nz = vz / length;

                // 根据强度计算颜色插值
                float t = (float) Mth.clamp((length - minVectorLength) / (maxVectorLength - minVectorLength), 0, 1);
                int color = lerpColor(minColor, maxColor, t);

                // 计算实际渲染长度（带缩放）
                double renderLength = Math.min(length * scale, maxVectorLength);

                // 渲染矢量线
                float endX = (float) (x + nx * renderLength);
                float endY = (float) (y + ny * renderLength);
                float endZ = (float) (z + nz * renderLength);

                line(poseMatrix, normalMatrix, lineConsumer,
                        (float) x, (float) y, (float) z,
                        endX, endY, endZ,
                        color);

                // 渲染箭头（如果矢量足够长）
                if (renderLength > 0.5f) {
                    renderArrow(poseMatrix, normalMatrix, arrowConsumer,
                            endX, endY, endZ,
                            (float) nx, (float) ny, (float) nz,
                            (float) Math.min(0.3f, renderLength * 0.3f),
                            color);
                }
            }
            poseStack.popPose();
        }
    }

    // 渲染一条线
    private static void line(Matrix4f poseMatrix, Matrix3f normalMatrix, VertexConsumer consumer,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             int color) {
        // 计算方向向量
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float len = (float)Math.sqrt(dx*dx + dy*dy + dz*dz);

        // 归一化方向
        if (len > 0.001f) {
            dx /= len;
            dy /= len;
            dz /= len;
        }

        // 提取颜色分量
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        // 绘制线段
        consumer.vertex(poseMatrix, x1, y1, z1)
                .color(r, g, b, a)
                .normal(normalMatrix, dx, dy, dz)
                .endVertex();

        consumer.vertex(poseMatrix, x2, y2, z2)
                .color(r, g, b, a)
                .normal(normalMatrix, dx, dy, dz)
                .endVertex();
    }

    // 渲染箭头
    private static void renderArrow(Matrix4f poseMatrix, Matrix3f normalMatrix, VertexConsumer consumer,
                                    float x, float y, float z,
                                    float nx, float ny, float nz,
                                    float size, int color) {
        // 计算垂直方向（任意垂直向量）
        float ax = 0, ay = 0, az = 0;
        if (Math.abs(ny) > 0.5f) {
            ax = 1.0f;
            ay = 0.0f;
            az = 0.0f;
        } else {
            ax = 0.0f;
            ay = 1.0f;
            az = 0.0f;
        }

        // 计算叉积得到真正的垂直向量
        float ux = ny * az - nz * ay;
        float uy = nz * ax - nx * az;
        float uz = nx * ay - ny * ax;

        // 归一化
        float ulen = (float)Math.sqrt(ux*ux + uy*uy + uz*uz);
        if (ulen > 0.001f) {
            ux /= ulen;
            uy /= ulen;
            uz /= ulen;
        }

        // 计算第二个垂直向量
        float vx = ny * uz - nz * uy;
        float vy = nz * ux - nx * uz;
        float vz = nx * uy - ny * ux;

        // 归一化
        float vlen = (float)Math.sqrt(vx*vx + vy*vy + vz*vz);
        if (vlen > 0.001f) {
            vx /= vlen;
            vy /= vlen;
            vz /= vlen;
        }

        // 提取颜色分量
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        // 箭头点1
        float arrowX1 = x - nx * size * 1.5f + ux * size;
        float arrowY1 = y - ny * size * 1.5f + uy * size;
        float arrowZ1 = z - nz * size * 1.5f + uz * size;

        // 箭头点2
        float arrowX2 = x - nx * size * 1.5f + vx * size;
        float arrowY2 = y - ny * size * 1.5f + vy * size;
        float arrowZ2 = z - nz * size * 1.5f + vz * size;

        // 箭头点3
        float arrowX3 = x - nx * size * 1.5f - ux * size;
        float arrowY3 = y - ny * size * 1.5f - uy * size;
        float arrowZ3 = z - nz * size * 1.5f - uz * size;

        // 箭头点4
        float arrowX4 = x - nx * size * 1.5f - vx * size;
        float arrowY4 = y - ny * size * 1.5f - vy * size;
        float arrowZ4 = z - nz * size * 1.5f - vz * size;

        // 绘制箭头（四个三角形组成一个锥形）

        // 从尖端到点1
        consumer.vertex(poseMatrix, x, y, z)
                .color(r, g, b, a)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();
        consumer.vertex(poseMatrix, arrowX1, arrowY1, arrowZ1)
                .color(r, g, b, a)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();

        // 从尖端到点2
        consumer.vertex(poseMatrix, x, y, z)
                .color(r, g, b, a)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();
        consumer.vertex(poseMatrix, arrowX2, arrowY2, arrowZ2)
                .color(r, g, b, a)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();

        // 从尖端到点3
        consumer.vertex(poseMatrix, x, y, z)
                .color(r, g, b, a)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();
        consumer.vertex(poseMatrix, arrowX3, arrowY3, arrowZ3)
                .color(r, g, b, a)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();

        // 从尖端到点4
        consumer.vertex(poseMatrix, x, y, z)
                .color(r, g, b, a)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();
        consumer.vertex(poseMatrix, arrowX4, arrowY4, arrowZ4)
                .color(r, g, b, a)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();

        // 连接箭头底部形成一个环
        line(poseMatrix, normalMatrix, consumer, arrowX1, arrowY1, arrowZ1, arrowX2, arrowY2, arrowZ2, color);
        line(poseMatrix, normalMatrix, consumer, arrowX2, arrowY2, arrowZ2, arrowX3, arrowY3, arrowZ3, color);
        line(poseMatrix, normalMatrix, consumer, arrowX3, arrowY3, arrowZ3, arrowX4, arrowY4, arrowZ4, color);
        line(poseMatrix, normalMatrix, consumer, arrowX4, arrowY4, arrowZ4, arrowX1, arrowY1, arrowZ1, color);
    }

    // 颜色插值
    private static int lerpColor(int color1, int color2, float t) {
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = (int)(a1 + (a2 - a1) * t);
        int r = (int)(r1 + (r2 - r1) * t);
        int g = (int)(g1 + (g2 - g1) * t);
        int b = (int)(b1 + (b2 - b1) * t);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
