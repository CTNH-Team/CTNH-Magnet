package tech.vixhentx.mcmod.ctnhmagnet.common.field;

import net.minecraft.core.Direction;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.common.datamodel.MagnetVector;

import static tech.vixhentx.mcmod.ctnhmagnet.api.utils.MagnetFieldManager.*;

public class MagnetFieldImpl {
    public static void updateField(IMagnetProvider provider){
        //TODO: Implement updateField logic
        var pos = provider.getPos();
        for(Direction direction : Direction.values()){
            setMagnetField(pos.relative(direction)
                    , provider.getLevel()
                    , (MagnetVector) provider.getMagnetField().rotate(direction.getRotation()));
        }
    }
}
