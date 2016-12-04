package mods.eln.integration.waila;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mods.eln.Eln;
import mods.eln.misc.Coordonate;
import mods.eln.packets.GhostNodeWailaRequestPacket;
import mods.eln.packets.SixNodeWailaRequestPacket;
import mods.eln.packets.TransparentNodeRequestPacket;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gregory Maddra on 2016-06-29.
 */
public class WailaCache {

    public static LoadingCache<Coordonate, Map<String, String>> nodes = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build(
            new CacheLoader<Coordonate, Map<String, String>>() {
                public Map<String, String> load(Coordonate key) throws Exception {
                    Eln.elnNetwork.sendToServer(new TransparentNodeRequestPacket(key));
                    return null;
                }
            }
        );

    public static LoadingCache<SixNodeCoordonate, Map<String, String>> sixNodes = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build(
            new CacheLoader<SixNodeCoordonate, Map<String, String>>() {
                public Map<String, String> load(SixNodeCoordonate key) throws Exception {
                    Eln.elnNetwork.sendToServer(new SixNodeWailaRequestPacket(key.getCoord(), key.getSide()));
                    return null;
                }
            }
        );

    public static LoadingCache<Coordonate, GhostNodeWailaData> ghostNodes = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build(
            new CacheLoader<Coordonate, GhostNodeWailaData>() {
                public GhostNodeWailaData load(Coordonate key) throws Exception {
                    Eln.elnNetwork.sendToServer(new GhostNodeWailaRequestPacket(key));
                    return null;
                }
            }
        );

}
