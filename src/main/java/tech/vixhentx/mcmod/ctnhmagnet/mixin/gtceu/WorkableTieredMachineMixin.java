package tech.vixhentx.mcmod.ctnhmagnet.mixin.gtceu;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.IMufflableMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import org.spongepowered.asm.mixin.Mixin;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetReceiver;

@Mixin(WorkableTieredMachine.class)
public abstract class WorkableTieredMachineMixin implements IMagnetReceiver {
}
