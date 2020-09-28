package es.eltrueno.cosmeticpreview;

import es.eltrueno.cosmeticpreview.utils.Schematic;
import es.eltrueno.cosmeticpreview.utils.SchematicManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;


public class SchematicPreview extends CosmeticPreview{

    private Location location;
    private Schematic schematic;

    public SchematicPreview(Player player, CPCamera camera, Plugin plugin, Location location, String schematicdir) throws Exception {
        super(player, camera, plugin);
        this.location = location;
        this.schematic = SchematicManager.loadSchematic(schematicdir);

        this.listener = new Listener() {
            @EventHandler
            public void exitCamera(PlayerToggleSneakEvent ev){
                if(getShiftexit() && isRunning())finish();
            }
        };
        Bukkit.getPluginManager().registerEvents(this.listener, plugin);
    }

    public Location getLocation(){
        return this.location;
    }

    public Schematic getSchematic(){
        return this.schematic;
    }

    public void showSchematic(){
        SchematicManager.fakePasteSchematic(this.location, this.schematic, this.getPlayer());
    }

    public void hideSchematic(){
        SchematicManager.fakeDeleteSchematic(this.location, this.schematic, this.getPlayer());
    }

    public void start(){
        this.dumpData();
        //this.getPlayer().setGameMode(GameMode.SPECTATOR);
        this.joinCamera();
        setRunning(true);
        showSchematic();
    }

    public void finish(){
        this.exitCamera();
        //this.backData();
        setRunning(false);
        hideSchematic();
    }
}
