package otd.nms.v1_19_R3;

import forge_sandbox.greymerk.roguelike.worldgen.spawners.SpawnPotential;
import otd.nms.EquipHands;

public class EquipHands119R3 implements EquipHands {
	public Object get(Object mob, String weapon, String offhand, SpawnPotential sp) {
		net.minecraft.nbt.NBTTagList hands = new net.minecraft.nbt.NBTTagList();
		hands.add((net.minecraft.nbt.NBTBase) sp.getItem(weapon));
		hands.add((net.minecraft.nbt.NBTBase) sp.getItem(offhand));
		net.minecraft.nbt.NBTTagCompound nbt = (net.minecraft.nbt.NBTTagCompound) mob;
		nbt.a("HandItems", hands);
		return nbt;
	}
}
