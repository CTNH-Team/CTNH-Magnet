package tech.vixhentx.mcmod.ctnhmagnet.api.datamodel;

import net.minecraft.util.Mth;

public class MagnetUtils {
    public static int fastLog2(int x){
        return Integer.SIZE-1 - Integer.numberOfLeadingZeros(Math.max(x, 0));
    }
    public static int getMagVTier(int strength){
        return Mth.clamp(fastLog2(strength-1) - 2, 0 , MagnetStages.Magnetometers.MAX.ordinal());
    }
    public static int getMagVTier(float strength){
        return getMagVTier(Mth.ceil(strength));
    }
}
