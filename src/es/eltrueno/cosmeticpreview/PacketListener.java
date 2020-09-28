package es.eltrueno.cosmeticpreview;

import es.eltrueno.cosmeticpreview.utils.tinyprotocol.Reflection;
import es.eltrueno.cosmeticpreview.utils.tinyprotocol.TinyProtocol;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class PacketListener {

    private static TinyProtocol protocol = null;

    private static Class<?> EntityInteractClass = Reflection.getClass("{nms}.PacketPlayInUseEntity");
    private static Reflection.FieldAccessor<Integer> EntityID = Reflection.getField(EntityInteractClass, int.class, 0);

    public void startListening(Plugin plugin){
        if(protocol==null) {
            protocol = new TinyProtocol(plugin) {
                @Override
                public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                    if(EntityInteractClass.isInstance(packet)){
                        if(sender.getEntityId()==EntityID.get(packet)){
                            //cancel packet
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @SuppressWarnings("deprecation")
                                @Override
                                public void run() {
                                    //sender.updateInventory();
                                }
                            });
                            return null;
                        }else return super.onPacketInAsync(sender, channel, packet);
                    }
                    else return super.onPacketInAsync(sender, channel, packet);
                }
            };
        }
    }

}
