package tech.vixhentx.mcmod.ctnhmagnet.mixin.gtceu;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetReceiver;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

import static com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity.getCapabilitiesFromTraits;

@Mixin(value = MetaMachineBlockEntity.class)
public abstract class MetaMachineBlockEntityMixin{

    @Inject(method = "getCapability(Lcom/gregtechceu/gtceu/api/machine/MetaMachine;Lnet/minecraftforge/common/capabilities/Capability;Lnet/minecraft/core/Direction;)Lnet/minecraftforge/common/util/LazyOptional;",at=@At("RETURN"),cancellable = true,remap = false)
    private static <T> void getCapability(MetaMachine machine, @NotNull Capability<T> cap, @Nullable Direction side, CallbackInfoReturnable<LazyOptional<T>> cir) {
        if (cap == MagnetCapabilities.CAPABILITY_MAGNET_RECEIVER){
            if(machine instanceof IMagnetReceiver magnetReceiver){
                cir.setReturnValue(MagnetCapabilities.CAPABILITY_MAGNET_RECEIVER.orEmpty(cap, LazyOptional.of(() -> magnetReceiver)));
            }
        }
        else if (cap == MagnetCapabilities.CAPABILITY_MAGNET_PROVIDER){
            if(machine instanceof IMagnetProvider magnetProvider){
                cir.setReturnValue(MagnetCapabilities.CAPABILITY_MAGNET_PROVIDER.orEmpty(cap, LazyOptional.of(() -> magnetProvider)));
            }
        }
    }

}
