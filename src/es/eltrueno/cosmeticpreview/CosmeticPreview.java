package es.eltrueno.cosmeticpreview;

import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public abstract class CosmeticPreview {

    private Player player;
    private CPCamera camera;
    private boolean shiftexit = true;
    private Object[] datadump = new Object[3];
    private boolean running;

    protected Listener listener;

    public CosmeticPreview(Player player, CPCamera camera, Plugin plugin){
        this.player = player;
        this.camera = camera;
        this.listener = new Listener() {
            @EventHandler
            public void exitCamera(PlayerToggleSneakEvent ev){
                if(isRunning() && getShiftexit())finish();
            }
        };
        Bukkit.getPluginManager().registerEvents(this.listener, plugin);
    }

    public void setShiftToExit(boolean b){
        this.shiftexit = b;
    }

    public boolean getShiftexit() {
        return shiftexit;
    }

    public Player getPlayer(){
        return player;
    }

    public CPCamera getCamera(){
        return this.camera;
    }

    protected void dumpData(){
        GameMode gm;
        Boolean fly;
        Location lastloc;
        gm = this.player.getGameMode();
        fly = this.player.getAllowFlight();
        lastloc = this.player.getLocation().clone();
        this.datadump = new Object[]{gm, fly, lastloc};
    }

    protected void backData(){
        this.player.setGameMode((GameMode) this.datadump[0]);
        this.player.setAllowFlight((Boolean) this.datadump[1]);
        this.player.setFlying((Boolean) this.datadump[1]);
       // this.player.teleport((Location) this.datadump[2]);
    }

    protected void setRunning(boolean b){
        this.running = b;
    }

    public boolean isRunning() {
        return running;
    }

    protected void joinCamera(){
        try {
            Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
                    .split(",")[3] + "." + "PlayerConnection")
                    .getMethod("sendPacket", Class.forName("net.minecraft.server."
                            + Bukkit.getServer().getClass().getPackage().getName()
                            .replace(".", ",").split(",")[3] + "." + "Packet"))
                    .invoke(this.player.getClass().getMethod("getHandle").invoke(this.player).getClass()
                                    .getField("playerConnection").get(this.player.getClass().getMethod("getHandle").invoke(player)),
                            Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName()
                                    .replace(".", ",").split(",")[3] + "." + "PacketPlayOutGameStateChange")
                                    .getConstructor(int.class, float.class).newInstance(3, 3));
            PacketPlayOutCamera specpacket = new PacketPlayOutCamera();
            Field a = specpacket.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.set(specpacket, this.getCamera().getEntity().getEntityId());
            ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket(specpacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void exitCamera(){
        try{
            PacketPlayOutCamera specpacket = new PacketPlayOutCamera();
            Field a = specpacket.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.set(specpacket, this.player.getEntityId());
            ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket(specpacket);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        try {
            Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
                    .split(",")[3] + "." + "PlayerConnection")
                    .getMethod("sendPacket", Class.forName("net.minecraft.server."
                            + Bukkit.getServer().getClass().getPackage().getName()
                            .replace(".", ",").split(",")[3] + "." + "Packet"))
                    .invoke(this.player.getClass().getMethod("getHandle").invoke(this.player).getClass()
                                    .getField("playerConnection").get(this.player.getClass().getMethod("getHandle").invoke(this.player)),
                            Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName()
                                    .replace(".", ",").split(",")[3] + "." + "PacketPlayOutGameStateChange")
                                    .getConstructor(int.class, float.class).newInstance(3, 2));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        dumpData();
        //this.player.setGameMode(GameMode.SPECTATOR);
        joinCamera();
        setRunning(true);
    }

    public void finish(){
        exitCamera();
        backData();
        setRunning(false);
    }


}
