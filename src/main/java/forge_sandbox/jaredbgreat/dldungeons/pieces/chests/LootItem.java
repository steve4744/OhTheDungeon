package forge_sandbox.jaredbgreat.dldungeons.pieces.chests;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */

//import forge_sandbox.jaredbgreat.dldungeons.ConfigHandler;
//import forge_sandbox.jaredbgreat.dldungeons.nbt.NBTHelper;
//import forge_sandbox.jaredbgreat.dldungeons.nbt.tags.ITag;
//import forge_sandbox.jaredbgreat.dldungeons.nbt.NBTHelper;
//import forge_sandbox.jaredbgreat.dldungeons.nbt.tags.ITag;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import otd.util.Logging;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

//import net.minecraft.block.Block;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.registries.IForgeRegistry;

/**
 * A class to represent entries in loot tables.
 * 
 * This stores an item for use as loot in terms of the Minecraft Item instance
 * along with minimum and maximum quantities and the items damage value (or
 * metadata in block terms).
 * 
 * @author Jared Blackburn
 * 
 */
public class LootItem {
//	private static IForgeRegistry ItemRegistry;
	private static Map<ItemPrototype, Integer> prototypes;

	Material item;
	int min, max, meta, level;
//	ArrayList<ITag> nbtData;

	private static class ItemPrototype {
		public final Material item;
		public final int meta;

		public ItemPrototype(Material i, int m) {
			item = i;
			meta = m;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof ItemPrototype)) {
				return false;
			}
			ItemPrototype p = (ItemPrototype) obj;
			return (p.item == item) && (p.meta == meta);
		}

		@Override
		public int hashCode() {
			return item.hashCode() + (meta * 102181);
		}
	}

	static {
		prototypes = new HashMap<>();
	}

	/**
	 * Add the named item, where name is a string including the items name and an
	 * optional damage value.
	 * 
	 * @param id
	 * @param min
	 * @param max
	 */
	public LootItem(String id, int min, int max, int level) {
		metaParse(id);
		if (min > max)
			min = max;
		this.min = min;
		this.max = max;
		this.level = level;
		if (item == null) {
			String error = "[DLDUNGEONS] ERROR! Item read as \"" + id + "\" was was not in registry (returned null).";
			Logging.logError(error);
//			if(ConfigHandler.failfast) {
//				throw new NoSuchElementException(error);
//			}
		} else {
			fixLevel();
		}
	}

	/**
	 * Create a LootItem using Item.
	 * 
	 * @param item
	 * @param min
	 * @param max
	 */
	public LootItem(Material item, int min, int max) {
		this.item = item;
		if (min > max)
			min = max;
		this.min = min;
		this.max = max;
		// For these items, don't care about level
		this.level = 6;
	}

	/**
	 * Create a LootItem using a block
	 * 
	 * @param item
	 * @param min
	 * @param max
	 */
//	public LootItem(Block item, int min, int max) {
//		this.item = Item.getItemFromBlock(item);
//		if(min > max) min = max;
//		this.min = min;
//		this.max = max;
//		// For these items, don't care about level
//		this.level = 6;
//	}

	private void fixLevel() {
		// Only set level on non-stacked items like tools / weapons
		if (max == 1) {
			ItemPrototype p = new ItemPrototype(item, meta);
			if (prototypes.containsKey(p)) {
				int l = prototypes.get(p);
				if (l < level) {
					level = l;
				}
				if (l > level) {
					prototypes.put(p, level);
				}
			} else {
				prototypes.put(p, level);
			}
		}
	}

	/**
	 * Parses its input string to set the values for item and meta (damage value).
	 * 
	 * @param in
	 */
	private void metaParse(String in) {
//		Tokenizer tokens = new Tokenizer(in, "({[]})");
//		String name  = tokens.nextToken();
		in = in.toUpperCase();
		in = in.replace("MINECRAFT:", "");
		item = Material.valueOf(in);
		if (item == null) {
			Logging.logError(
					"[DLDUNGEONS] ERROR! Item read as \"" + in + "\" was was not in registry (returned null).");
		}
//		if(tokens.hasMoreTokens()) {
//			meta = Integer.parseInt(tokens.nextToken());
//		}
	}

	private static Material getItem(String in) {
		try {
			return Material.valueOf("minecraft:" + in);
		} catch (Exception ex) {
			return Material.AIR;
		}
	}

	/**
	 * This will parse an NBT tag from the chest.cfg and add it to the LootItem as
	 * an NbtTag object for later use in adding the tag to actual item stacks in
	 * chests.
	 * 
	 * @param in
	 */
	public void addNbt(String in) {
//		if(nbtData == null) {
//			nbtData = new ArrayList<ITag>();
//		}
//		nbtData.add(NBTHelper.getTagFromLabel(in));
	}

	/**
	 * Make the items NBT data a small as possible.
	 */
	public void trimNbt() {
//		if(nbtData != null) {
//			nbtData.trimToSize();
//		}
	}

	/**
	 * Returns a randomly sized ItemStack of the Item.
	 * 
	 * @param random
	 * @return
	 */
	public ItemStack getStack(Random random) {
		ItemStack out;
		if (max <= min) {
			if (item.isItem())
				out = new ItemStack(item, max);
			else
				out = new ItemStack(item, max);
		} else {
			if (item.isItem())
				out = new ItemStack(item, random.nextInt(max - min) + min + 1);
			else
				out = new ItemStack(item, random.nextInt(max - min) + min + 1);
		}
//		if(out.getType() == null) {
//			return null;
//		}
//		if(out.getHasSubtypes() && meta >= 0) {
//			out.setItemDamage(meta);
//		} else {
//			out.setItemDamage(0);
//		}
//		if(nbtData != null && !nbtData.isEmpty()) {
//			for(ITag tag: nbtData) {
//                            ItemStack tmp[] = {out};
//				NBTHelper.setNbtTag(tmp, tag);
//                                out = tmp[0];
//			}
//		}
		return out;
	}

	/*----------------------------------*/
	/* Default Loots Below */
	/*----------------------------------*/
	// Is this really necessary anymore?

	public static LootItem stoneSword = new LootItem(Material.STONE_SWORD, 1, 1);
	public static LootItem ironSword = new LootItem(Material.IRON_SWORD, 1, 1);
	public static LootItem diamondSword = new LootItem(Material.DIAMOND_SWORD, 1, 1);
	public static LootItem bow = new LootItem(Material.BOW, 1, 1);
	public static LootItem fewArrows = new LootItem(Material.ARROW, 4, 12);
	public static LootItem someArrows = new LootItem(Material.ARROW, 8, 16);
	public static LootItem manyArrows = new LootItem(Material.ARROW, 16, 48);

	public static LootItem fewTorches = new LootItem(Material.TORCH, 4, 12);
	public static LootItem someToreches = new LootItem(Material.TORCH, 12, 16);
	public static LootItem manyTorches = new LootItem(Material.TORCH, 16, 24);

	public static LootItem leatherHat = new LootItem(getItem("leather_helmet"), 1, 1);
	public static LootItem goldHat = new LootItem(getItem("golden_helmet"), 1, 1);
	public static LootItem ironHat = new LootItem(getItem("iron_helmet"), 1, 1);
	public static LootItem diamondHat = new LootItem(getItem("diamond_helmet"), 1, 1);
	public static LootItem leatherBoots = new LootItem(getItem("leather_boots"), 1, 1);
	public static LootItem goldBoots = new LootItem(getItem("golden_boots"), 1, 1);
	public static LootItem ironBoots = new LootItem(getItem("iron_boots"), 1, 1);
	public static LootItem diamondBoots = new LootItem(getItem("diamond_boots"), 1, 1);
	public static LootItem leatherPants = new LootItem(getItem("leather_leggings"), 1, 1);
	public static LootItem goldPants = new LootItem(getItem("golden_leggings"), 1, 1);
	public static LootItem ironPants = new LootItem(getItem("iron_leggings"), 1, 1);
	public static LootItem diamondPants = new LootItem(getItem("diamond_leggings"), 1, 1);
	public static LootItem leatherChest = new LootItem(getItem("leather_chestplate"), 1, 1);
	public static LootItem goldChest = new LootItem(getItem("golden_chestplate"), 1, 1);
	public static LootItem ironChest = new LootItem(getItem("iron_chestplate"), 1, 1);
	public static LootItem diamondChest = new LootItem(getItem("diamond_chestplate"), 1, 1);

	public static LootItem someBread = new LootItem(getItem("bread"), 2, 4);
	public static LootItem moreBread = new LootItem(getItem("bread"), 4, 8);
	public static LootItem someSteak = new LootItem(getItem("cooked_beef"), 2, 4);
	public static LootItem moreSteak = new LootItem(getItem("cooked_beef"), 4, 8);
	public static LootItem someChicken = new LootItem(getItem("cooked_chicken"), 2, 4);
	public static LootItem moreChicken = new LootItem(getItem("cooked_chicken"), 4, 8);
	public static LootItem someApples = new LootItem(getItem("apple"), 1, 3);
	public static LootItem moreApples = new LootItem(getItem("apple"), 2, 7);
	public static LootItem somePie = new LootItem(getItem("pumpkin_pie"), 1, 3);
	public static LootItem morePie = new LootItem(getItem("pumpkin_pie"), 2, 7);
	public static LootItem goldApple = new LootItem(getItem("golden_apple"), 1, 1);
	public static LootItem goldApples = new LootItem(getItem("golden_apple"), 1, 3);

	public static LootItem oneGold = new LootItem(getItem("gold_ingot"), 1, 1);
	public static LootItem someGold = new LootItem(getItem("gold_ingot"), 2, 5);
	public static LootItem moreGold = new LootItem(getItem("gold_ingot"), 3, 8);
	public static LootItem someIron = new LootItem(getItem("iron_ingot"), 1, 8);
	public static LootItem moreIron = new LootItem(getItem("iron_ingot"), 3, 12);
	public static LootItem oneDiamond = new LootItem(getItem("iron_ingot"), 1, 1);
	public static LootItem diamonds = new LootItem(getItem("diamond"), 1, 4);
	public static LootItem manyDiamonds = new LootItem(getItem("diamond"), 3, 9);
	public static LootItem oneEmerald = new LootItem(getItem("emerald"), 1, 1);
	public static LootItem emeralds = new LootItem(getItem("emerald"), 1, 4);
	public static LootItem manyEmerald = new LootItem(getItem("emerald"), 3, 7);

	public static LootItem saddle = new LootItem(getItem("saddle"), 1, 1);
	public static LootItem ironBarding = new LootItem(getItem("iron_horse_armor"), 1, 1);
	public static LootItem goldBarding = new LootItem(getItem("golden_horse_armor"), 1, 1);
	public static LootItem diamondBard = new LootItem(getItem("diamond_horse_armor"), 1, 1);

	public static LootItem book = new LootItem(getItem("book"), 1, 1);
	public static LootItem someBooks = new LootItem(Material.BOOK, 2, 5);
	public static LootItem moreBooks = new LootItem(Material.BOOK, 3, 8);
	public static LootItem nameTag = new LootItem(Material.NAME_TAG, 1, 1);
	public static LootItem enderpearl = new LootItem(getItem("ender_pearl"), 1, 2);
	public static LootItem enderpearls = new LootItem(getItem("ender_pearl"), 1, 9);
	public static LootItem eyeOfEnder = new LootItem(getItem("ender_eye"), 1, 2);
	public static LootItem blazeRod = new LootItem(Material.BLAZE_ROD, 1, 2);
	public static LootItem netherstar = new LootItem(Material.NETHER_STAR, 1, 1);

	public static LootItem disc13 = new LootItem(Material.MUSIC_DISC_13, 1, 1);
	public static LootItem discCat = new LootItem(Material.MUSIC_DISC_CAT, 1, 1);
	public static LootItem discBlocks = new LootItem(Material.MUSIC_DISC_BLOCKS, 1, 1);
	public static LootItem discChirp = new LootItem(Material.MUSIC_DISC_CHIRP, 1, 1);
	public static LootItem discFar = new LootItem(Material.MUSIC_DISC_FAR, 1, 1);
	public static LootItem discMall = new LootItem(Material.MUSIC_DISC_MALL, 1, 1);
	public static LootItem discMellohi = new LootItem(Material.MUSIC_DISC_MELLOHI, 1, 1);
	public static LootItem discStrad = new LootItem(Material.MUSIC_DISC_STRAD, 1, 1);
	public static LootItem discWard = new LootItem(Material.MUSIC_DISC_WARD, 1, 1);
	public static LootItem disc11 = new LootItem(Material.MUSIC_DISC_11, 1, 1);
	public static LootItem discWait = new LootItem(Material.MUSIC_DISC_WAIT, 1, 1);
}
