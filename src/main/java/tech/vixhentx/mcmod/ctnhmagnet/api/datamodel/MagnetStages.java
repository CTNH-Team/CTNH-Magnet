package tech.vixhentx.mcmod.ctnhmagnet.api.datamodel;

import net.minecraft.util.Mth;

public class MagnetStages {
    public static enum ElectroMagnetics{
        START,
        KNOW,
        EFFECT,
        MASTER,
        INVERT,
        TRANSFIXION,
        MAX;
        public int getMagV(){
            return MagnetValues.MagV[this.ordinal()<MAX.ordinal()?this.ordinal():(MAX.ordinal()-1)];
        }
    }
    public static enum Magnetometers{
        SMS,
        KMS,
        EMS,
        MMS,
        IMS,
        TMS,
        MAX;
        public int getMagV(){
            return MagnetValues.MagV[this.ordinal()<MAX.ordinal()?this.ordinal():(MAX.ordinal()-1)];
        }
        public static Magnetometers fromTier(int tier){
            return Magnetometers.values()[Mth.clamp(tier,0,MAX.ordinal()-1)];
        }
    }
    public static enum Magnetizations{
        SMP,
        KMP,
        EMP,
        MMP,
        IMP,
        TMP,
        MAX;
        public int getMagV(){
            return MagnetValues.MagV[this.ordinal()<MAX.ordinal()?this.ordinal():(MAX.ordinal()-1)];
        }
    }
}
