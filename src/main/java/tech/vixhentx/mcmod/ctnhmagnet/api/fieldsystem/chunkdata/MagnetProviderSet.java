package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata;

import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Unmodifiable;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.sync.MagnetSyncUtils;
import tech.vixhentx.mcmod.ctnhmagnet.api.utils.CapInfoUtils;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C.MagnetFieldSyncPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.AbstractMagnetFieldManager.accumulate;
import static tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.AbstractMagnetFieldManager.dispersal;

@AllArgsConstructor
public class MagnetProviderSet {
    private final LevelChunk chunk;
    private final Int2ObjectSortedMap<ObjectSet<IMagnetProvider>> map = new Int2ObjectRBTreeMap<>();

    public void initFromRawProviders(long[] rawSources){
        map.clear();
        for(long rawSource : rawSources){
         CapInfoUtils.getMagnetProvider(chunk.getLevel(), BlockPos.of(rawSource))
                 .ifPresent(this::addProviderInner);
        }
    }
    public long[] toRawProviders(){
        return getAllProviders().stream()
                .map(IMagnetProvider::getPos)
                .mapToLong(BlockPos::asLong)
                .toArray();
    }
    public ObjectSet<IMagnetProvider> getProviders(int priority){
        return map.computeIfAbsent(priority,__->new ObjectOpenHashSet<>());
    }
    public List<IMagnetProvider> getProvidersAfter(int priority){
        List<IMagnetProvider> ret = new ArrayList<>();
        map.tailMap(priority+1).values().forEach(ret::addAll);
        Collections.reverse(ret);
        return ret;
    }
    public List<IMagnetProvider> getProvidersBefore(int priority){
        List<IMagnetProvider> ret = new ArrayList<>();
        map.headMap(priority).values().forEach(ret::addAll);
        return ret;
    }
    public ObjectSet<IMagnetProvider> getAllProviders(){
        ObjectSet<IMagnetProvider> providers = new ObjectOpenHashSet<>();
        map.values().forEach(providers::addAll);
        return providers;
    }
    void addProviderInner(IMagnetProvider provider){
        ObjectSet<IMagnetProvider> providers = getProviders(provider.getPriority());
        providers.add(provider);
    }
    void removeProviderInner(IMagnetProvider provider){
        ObjectSet<IMagnetProvider> providers = getProviders(provider.getPriority());
        providers.remove(provider);
    }
    public void addProvider(IMagnetProvider provider){
        addProviderInner(provider);

        Long2ObjectMap<MagnetVector> differ = new Long2ObjectOpenHashMap<>();

        getProvidersAfter(provider.getPriority()).forEach(p -> dispersal(p.unspread(),differ));
        accumulate(provider.spread(),differ);
        getProvidersAfter(provider.getPriority()).forEach(p-> accumulate(p.spread(),differ));

        chunk.setUnsaved(true);
        MagnetSyncUtils.syncChanges(chunk, differ);
    }
    public void removeProvider(IMagnetProvider provider){
        Long2ObjectMap<MagnetVector> differ = new Long2ObjectOpenHashMap<>();

        getProvidersAfter(provider.getPriority()).forEach(p -> dispersal(p.unspread(),differ));
        dispersal(provider.unspread(),differ);
        getProvidersAfter(provider.getPriority()).forEach(p-> accumulate(p.spread(),differ));

        removeProviderInner(provider);

        chunk.setUnsaved(true);
        MagnetSyncUtils.syncChanges(chunk, differ);
    }
}
