package es.eltrueno.cosmeticpreview;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class CPCamera {

    private Location location;
    private boolean created = false;
    private ArmorStand entity;

    public CPCamera (Location location){
        this.location = location;
    }

    public Location getLocation(){
        return this.location;
    }

    public boolean isCreated(){
        return this.created;
    }

    public ArmorStand getEntity(){
        return this.entity;
    }

    public void create(){
        ArmorStand stand = (ArmorStand) this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        this.created = true;
        this.entity = stand;
    }
    public void destroy(){
        this.entity.remove();
    }

}
