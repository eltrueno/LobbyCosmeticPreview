package es.eltrueno.cosmeticpreview.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Schematic {

    private short[] blocks;
    private byte[] data;
    private short width;
    private short length;
    private short height;

    public Schematic(short[] blocks, byte[] data, short width, short length, short height) {
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public short[] getBlocks() {
        return blocks;
    }
    public byte[] getData() {
        return data;
    }

    public short getWidth() {
        return width;
    }

    public short getLength() {
        return length;
    }

    public short getHeight() {
        return height;
    }

    public Location getCenterBlock(Location loc){
        int rel = width/2;
        Block block = new Location(loc.getWorld(), rel + loc.getX(), loc.getY(), rel + loc.getZ()).getBlock();
        int index = 0 * width * length + rel * width + rel;
        if(blocks[index]==0){
            for(int y = 0; y<height ; ++y){
                int ind = y * width * length + rel * width + rel;
                if(blocks[index]!=0){
                    Block b = new Location(loc.getWorld(), rel + loc.getX(), loc.getY(), rel + loc.getZ()).getBlock();
                    break;
                }
            }
        }
        return block.getLocation().add(0.5, 0, 0.5);
    }

}