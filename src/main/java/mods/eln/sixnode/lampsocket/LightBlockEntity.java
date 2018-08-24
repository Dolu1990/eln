package mods.eln.sixnode.lampsocket;

import mods.eln.Eln;
import mods.eln.misc.Coordinate;
import mods.eln.misc.INBTTReady;
import mods.eln.misc.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class LightBlockEntity extends TileEntity {

    ArrayList<LightHandle> lightList = new ArrayList<LightHandle>();

    public static final ArrayList<LightBlockObserver> observers = new ArrayList<LightBlockObserver>();

    static void addObserver(LightBlockObserver observer) {
        observers.add(observer);
    }

    static void removeObserver(LightBlockObserver observer) {
        observers.remove(observer);
    }


    public interface LightBlockObserver {
        void lightBlockDestructor(Coordinate coord);
    }

    static class LightHandle implements INBTTReady {
        byte value;
        int timeout;

        public LightHandle() {
            value = 0;
            timeout = 0;
        }

        public LightHandle(byte value, int timeout) {
            this.value = value;
            this.timeout = timeout;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt, String str) {
            value = nbt.getByte(str + "value");
            timeout = nbt.getInteger(str + "timeout");
        }

        @Override
        public void writeToNBT(NBTTagCompound nbt, String str) {
            nbt.setByte(str + "value", value);
            nbt.setInteger(str + "timeout", timeout);
        }
    }

    void addLight(int light, int timeout) {
        lightList.add(new LightHandle((byte) light, timeout));
        lightManager();
    }

	/*void removeLight(int light) {
        //int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		for (int idx = 0; idx < lightList.size(); idx++) {
			if (lightList.get(idx) == light) {
				lightList.remove(idx);
				lightManager();
				return;
			}
		}
		Utils.println("Assert void removeLight(int light)");
	}*/
	/*
	void replaceLight(int oldLight, int newLight) {
		for (int idx = 0; idx < lightList.size(); idx++) {
			if (lightList.get(idx) == oldLight) {
				lightList.set(idx, newLight);
				lightManager();
				return;
			}
		}	
		Utils.println("Assert void replaceLight(int oldLight, int newLight)");
	}*/

	/*
	int getLight() {
		int light = 0;
		for (LightHandle l : lightList) {
			if (light < l.value) light = l.value;
		}
		return light;
	}*/

    void lightManager() {
		/*if (lightList.size() == 0) {
			worldObj.setBlock(xCoord, yCoord, zCoord, 0);
		} else {
			int light = getLight();
			if (light != worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, light, 2);
				worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
			}
		}*/
    }

    public void updateEntity() {
        if (worldObj.isRemote) return;
        BlockPos pos = this.pos;
        if (lightList.isEmpty()) {

            //	worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
            worldObj.setBlockToAir(pos);
            //worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
            //Eln.instance.tileEntityDestructor.add(this);
            Utils.println("Destroy light at " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " ");
            //MIGHT CHANGE THE STRING LATER
            return;
        }

        int light = 0;
        Iterator<LightHandle> iterator = lightList.iterator();

        while (iterator.hasNext()) {
            LightHandle l = iterator.next();
            if (light < l.value) light = l.value;

            l.timeout--;
            if (l.timeout <= 0) {
                iterator.remove();
            }
        }

        if (light != worldObj.getBlockState(pos).getBlock().getMetaFromState(worldObj.getBlockState(pos))) {
            worldObj.getBlockState(pos).getBlock().setLightLevel(2);
            worldObj.notifyLightSet(pos);
        }
    }

    public static void addLight(World w, BlockPos pos, int light, int timeout) {
        Block block = w.getBlockState(pos).getBlock();
        if (block != Eln.lightBlock) {
            if (block != Blocks.AIR) return;
            w.setBlockState(pos, Eln.lightBlock.getDefaultState());
            w.setLightFor(EnumSkyBlock.BLOCK, pos, 2);
        }

        TileEntity t = w.getTileEntity(pos);
        if (t != null && t instanceof LightBlockEntity)
            ((LightBlockEntity) t).addLight(light, timeout);
        else
            Utils.println("ASSERT if(t != null && t instanceof LightBlockEntity)");
    }

    public static void addLight(Coordinate coord, int light, int timeout) {
        addLight(coord.world(), new BlockPos(coord.pos.getX(), coord.pos.getY(), coord.pos.getZ()), light, timeout);
    }

	/*public static void removeLight(Coordinate coord, int light) {
		int blockId = coord.getBlockId();
		if (blockId != Eln.lightBlockId) return;
		((LightBlockEntity)coord.getTileEntity()).removeLight(light);
	}
	
	public static void replaceLight(Coordinate coord, int oldLight, int newLight) {
		int blockId = coord.getBlockId();
		if (blockId != Eln.lightBlockId) {
			//coord.setBlock(Eln.lightBlockId, newLight);
			Utils.println("ASSERT public static void replaceLight(Coordinate coord, int oldLight, int newLight) " + coord);
			return;
		}
		((LightBlockEntity)coord.getTileEntity()).replaceLight(oldLight,newLight);
	}*/

	/*public int getClientLight() {
		return clientLight;
	}
	
	int clientLight = 0;*/
}
