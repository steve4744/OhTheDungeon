package otd.lib;

import static otd.lib.BiomeDictionary.Type.BEACH;
import static otd.lib.BiomeDictionary.Type.CAVE;
import static otd.lib.BiomeDictionary.Type.COLD;
import static otd.lib.BiomeDictionary.Type.CONIFEROUS;
import static otd.lib.BiomeDictionary.Type.DENSE;
import static otd.lib.BiomeDictionary.Type.DRY;
import static otd.lib.BiomeDictionary.Type.END;
import static otd.lib.BiomeDictionary.Type.FOREST;
import static otd.lib.BiomeDictionary.Type.HILLS;
import static otd.lib.BiomeDictionary.Type.HOT;
import static otd.lib.BiomeDictionary.Type.JUNGLE;
import static otd.lib.BiomeDictionary.Type.LUSH;
import static otd.lib.BiomeDictionary.Type.MESA;
import static otd.lib.BiomeDictionary.Type.MOUNTAIN;
import static otd.lib.BiomeDictionary.Type.MUSHROOM;
import static otd.lib.BiomeDictionary.Type.NETHER;
import static otd.lib.BiomeDictionary.Type.OCEAN;
import static otd.lib.BiomeDictionary.Type.OVERWORLD;
import static otd.lib.BiomeDictionary.Type.PLAINS;
import static otd.lib.BiomeDictionary.Type.PLATEAU;
import static otd.lib.BiomeDictionary.Type.RARE;
import static otd.lib.BiomeDictionary.Type.RIVER;
import static otd.lib.BiomeDictionary.Type.SANDY;
import static otd.lib.BiomeDictionary.Type.SAVANNA;
import static otd.lib.BiomeDictionary.Type.SNOWY;
import static otd.lib.BiomeDictionary.Type.SPARSE;
import static otd.lib.BiomeDictionary.Type.SPOOKY;
import static otd.lib.BiomeDictionary.Type.SWAMP;
import static otd.lib.BiomeDictionary.Type.VOID;
import static otd.lib.BiomeDictionary.Type.WASTELAND;
import static otd.lib.BiomeDictionary.Type.WET;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import java.util.stream.Collectors;
//import javax.annotation.Nonnull;
import org.bukkit.block.Biome;

/*
 * Minecraft Forge
 * Copyright (c) 2016-2018.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

//import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class BiomeDictionary {
	private static final Set<Type> dict = new HashSet<>();

	public static final class Type {

		private static final Map<String, Type> byName = new HashMap<>();
		private static Collection<Type> allTypes = Collections.unmodifiableCollection(byName.values());

		/* Temperature-based tags. Specifying neither implies a biome is temperate */
		public static final Type HOT = new Type("HOT");
		public static final Type COLD = new Type("COLD");

		/*
		 * Tags specifying the amount of vegetation a biome has. Specifying neither
		 * implies a biome to have moderate amounts
		 */
		public static final Type SPARSE = new Type("SPARSE");
		public static final Type DENSE = new Type("DENSE");

		/*
		 * Tags specifying how moist a biome is. Specifying neither implies the biome as
		 * having moderate humidity
		 */
		public static final Type WET = new Type("WET");
		public static final Type DRY = new Type("DRY");

		/*
		 * Tree-based tags, SAVANNA refers to dry, desert-like trees (Such as Acacia),
		 * CONIFEROUS refers to snowy trees (Such as Spruce) and JUNGLE refers to jungle
		 * trees. Specifying no tag implies a biome has temperate trees (Such as Oak)
		 */
		public static final Type SAVANNA = new Type("SAVANNA");
		public static final Type CONIFEROUS = new Type("CONIFEROUS");
		public static final Type JUNGLE = new Type("JUNGLE");

		/* Tags specifying the nature of a biome */
		public static final Type SPOOKY = new Type("SPOOKY");
		public static final Type DEAD = new Type("DEAD");
		public static final Type LUSH = new Type("LUSH");
		public static final Type NETHER = new Type("NETHER");
		public static final Type END = new Type("END");
		public static final Type MUSHROOM = new Type("MUSHROOM");
		public static final Type MAGICAL = new Type("MAGICAL");
		public static final Type RARE = new Type("RARE");

		public static final Type OCEAN = new Type("OCEAN");
		public static final Type RIVER = new Type("RIVER");
		/**
		 * A general tag for all water-based biomes. Shown as present if OCEAN or RIVER
		 * are.
		 **/
		public static final Type WATER = new Type("WATER", OCEAN, RIVER);

		/* Generic types which a biome can be */
		public static final Type CAVE = new Type("CAVE");
		public static final Type MESA = new Type("MESA");
		public static final Type FOREST = new Type("FOREST");
		public static final Type PLAINS = new Type("PLAINS");
		public static final Type MOUNTAIN = new Type("MOUNTAIN");
		public static final Type HILLS = new Type("HILLS");
		public static final Type SWAMP = new Type("SWAMP");
		public static final Type SANDY = new Type("SANDY");
		public static final Type SNOWY = new Type("SNOWY");
		public static final Type WASTELAND = new Type("WASTELAND");
		public static final Type BEACH = new Type("BEACH");
		public static final Type VOID = new Type("VOID");
		public static final Type PLATEAU = new Type("PLATEAU");

		public static final Type OVERWORLD = new Type("OVERWORLD");

		private final String name;
		private final List<Type> subTypes;
		private final Set<Biome> biomes = new HashSet<>();
		private final Set<Biome> biomesUn = Collections.unmodifiableSet(biomes);

		private Type(String name, Type... subTypes) {
			this.name = name;
			this.subTypes = ImmutableList.copyOf(subTypes);

			byName.put(name, this);
		}

		@SuppressWarnings("unused")
		private boolean hasSubTypes() {
			return !subTypes.isEmpty();
		}

		/**
		 * Gets the name for this type.
		 */
		public String getName() {
			return name;
		}

		public String toString() {
			return name;
		}

		/**
		 * Retrieves a Type instance by name, if one does not exist already it creates
		 * one. This can be used as intermediate measure for modders to add their own
		 * Biome types.
		 * <p>
		 * There are <i>no</i> naming conventions besides:
		 * <ul>
		 * <li><b>Must</b> be all upper case (enforced by name.toUpper())</li>
		 * <li><b>No</b> Special characters. {Unenforced, just don't be a pain, if it
		 * becomes a issue I WILL make this RTE with no worry about backwards
		 * compatibility}</li>
		 * </ul>
		 * <p>
		 * Note: For performance sake, the return value of this function SHOULD be
		 * cached. Two calls with the same name SHOULD return the same value.
		 *
		 * @param name The name of this Type
		 * @return An instance of Type for this name.
		 */
		public static Type getType(String name, Type... subTypes) {
			name = name.toUpperCase();
			Type t = byName.get(name);
			if (t == null) {
				t = new Type(name, subTypes);
			}
			return t;
		}

		/**
		 * @return An unmodifiable collection of all current biome types.
		 */
		public static Collection<Type> getAll() {
			return allTypes;
		}
	}

//    private static final Map<ResourceLocation, BiomeInfo> biomeInfoMap = new HashMap<ResourceLocation, BiomeInfo>();

	public static boolean hasType(Biome biome, Type type) {
		return type.biomes.contains(biome);
	}

	@SuppressWarnings("unused")
	private static class BiomeInfo {

		private final Set<Type> types = new HashSet<>();
		private final Set<Type> typesUn = Collections.unmodifiableSet(this.types);

	}

	static {
		registerVanillaBiomes_v2();
	}

	/**
	 * Adds the given types to the biome.
	 *
	 */
	public static void addTypes(Biome biome, Type... types) {
		for (Type type : types) {
			type.biomes.add(biome);
			dict.add(type);
		}

	}

	public static Set<Biome> getBiomes(Type type) {
		return type.biomesUn;
	}

	public static Set<Type> getTypes(Biome biome) {
		Set<Type> res = new HashSet<>();
		for (Type type : dict) {
			if (type.biomes.contains(biome))
				res.add(type);
		}
		return res;
	}

	private static void registerVanillaBiomes_v2() {
		addTypes(Biome.BADLANDS, MESA, SANDY, DRY, OVERWORLD);
		addTypes(Biome.BAMBOO_JUNGLE, HOT, WET, RARE, JUNGLE, OVERWORLD);
		addTypes(Biome.BASALT_DELTAS, HOT, DRY, NETHER);
		addTypes(Biome.BEACH, BEACH, WET, SANDY, OVERWORLD);
		addTypes(Biome.BIRCH_FOREST, FOREST, OVERWORLD);
		addTypes(Biome.CHERRY_GROVE, FOREST, HILLS, OVERWORLD);
		addTypes(Biome.COLD_OCEAN, OCEAN, COLD, OVERWORLD);
		addTypes(Biome.CRIMSON_FOREST, HOT, DRY, NETHER, FOREST);
		addTypes(Biome.DARK_FOREST, SPOOKY, DENSE, FOREST, OVERWORLD);
		addTypes(Biome.DEEP_COLD_OCEAN, OCEAN, COLD, OVERWORLD);
		addTypes(Biome.DEEP_DARK, CAVE, OVERWORLD);
		addTypes(Biome.DEEP_FROZEN_OCEAN, OCEAN, COLD, OVERWORLD);
		addTypes(Biome.DEEP_LUKEWARM_OCEAN, OCEAN, OVERWORLD);
		addTypes(Biome.DEEP_OCEAN, OCEAN, OVERWORLD);
		addTypes(Biome.DESERT, HOT, DRY, SANDY, OVERWORLD);
		addTypes(Biome.DRIPSTONE_CAVES, CAVE, OVERWORLD);
		addTypes(Biome.END_BARRENS, END);
		addTypes(Biome.END_HIGHLANDS, END);
		addTypes(Biome.END_MIDLANDS, END);
		addTypes(Biome.ERODED_BADLANDS, HOT, DRY, SPARSE, MOUNTAIN, RARE, OVERWORLD);
		addTypes(Biome.FLOWER_FOREST, FOREST, HILLS, RARE, OVERWORLD);
		addTypes(Biome.FOREST, FOREST, OVERWORLD);
		addTypes(Biome.FROZEN_OCEAN, COLD, OCEAN, SNOWY, OVERWORLD);
		addTypes(Biome.FROZEN_PEAKS, COLD, SNOWY, MOUNTAIN, HILLS, OVERWORLD);
		addTypes(Biome.FROZEN_RIVER, COLD, RIVER, SNOWY, OVERWORLD);
		addTypes(Biome.GROVE, COLD, SNOWY, MOUNTAIN, HILLS, OVERWORLD);
		addTypes(Biome.ICE_SPIKES, COLD, SNOWY, HILLS, RARE, OVERWORLD);
		addTypes(Biome.JAGGED_PEAKS, COLD, SNOWY, MOUNTAIN, HILLS, OVERWORLD);
		addTypes(Biome.JUNGLE, HOT, WET, DENSE, JUNGLE, OVERWORLD);
		addTypes(Biome.LUKEWARM_OCEAN, OCEAN, OVERWORLD);
		addTypes(Biome.LUSH_CAVES, CAVE, LUSH, OVERWORLD);
		addTypes(Biome.MANGROVE_SWAMP, SWAMP, WET, OVERWORLD);
		addTypes(Biome.MEADOW, PLAINS, OVERWORLD);
		addTypes(Biome.MUSHROOM_FIELDS, MUSHROOM, RARE, OVERWORLD);
		addTypes(Biome.NETHER_WASTES, HOT, DRY, NETHER);
		addTypes(Biome.OCEAN, OCEAN, OVERWORLD);
		addTypes(Biome.OLD_GROWTH_BIRCH_FOREST, FOREST, DENSE, HILLS, RARE, OVERWORLD);
		addTypes(Biome.OLD_GROWTH_PINE_TAIGA, COLD, CONIFEROUS, FOREST, OVERWORLD);
		addTypes(Biome.OLD_GROWTH_SPRUCE_TAIGA, DENSE, FOREST, RARE, OVERWORLD);
		addTypes(Biome.PALE_GARDEN, SPOOKY, DENSE, FOREST, RARE, OVERWORLD);
		addTypes(Biome.PLAINS, PLAINS, OVERWORLD);
		addTypes(Biome.RIVER, RIVER, OVERWORLD);
		addTypes(Biome.SAVANNA, HOT, SAVANNA, PLAINS, SPARSE, OVERWORLD);
		addTypes(Biome.SAVANNA_PLATEAU, HOT, SAVANNA, PLAINS, SPARSE, RARE, OVERWORLD, PLATEAU);
		addTypes(Biome.SMALL_END_ISLANDS, END);
		addTypes(Biome.SNOWY_BEACH, COLD, BEACH, SNOWY, OVERWORLD);
		addTypes(Biome.SNOWY_PLAINS, COLD, SNOWY, WASTELAND, OVERWORLD);
		addTypes(Biome.SNOWY_SLOPES, COLD, SNOWY, MOUNTAIN, HILLS, OVERWORLD);
		addTypes(Biome.SNOWY_TAIGA, COLD, CONIFEROUS, FOREST, SNOWY, OVERWORLD);
		addTypes(Biome.SOUL_SAND_VALLEY, HOT, DRY, NETHER);
		addTypes(Biome.SPARSE_JUNGLE, HOT, WET, JUNGLE, FOREST, RARE, OVERWORLD);
		addTypes(Biome.STONY_PEAKS, COLD, SNOWY, MOUNTAIN, HILLS, OVERWORLD);
		addTypes(Biome.STONY_SHORE, BEACH, SPARSE, WET, OVERWORLD);
		addTypes(Biome.SUNFLOWER_PLAINS, PLAINS, RARE, OVERWORLD);
		addTypes(Biome.SWAMP, WET, SWAMP, OVERWORLD);
		addTypes(Biome.TAIGA, COLD, CONIFEROUS, FOREST, OVERWORLD);
		addTypes(Biome.THE_END, COLD, DRY, END);
		addTypes(Biome.THE_VOID, VOID);
		addTypes(Biome.WARM_OCEAN, OCEAN, HOT, OVERWORLD);
		addTypes(Biome.WARPED_FOREST, HOT, DRY, NETHER, FOREST);
		addTypes(Biome.WINDSWEPT_FOREST, MOUNTAIN, FOREST, SPARSE, OVERWORLD);
		addTypes(Biome.WINDSWEPT_GRAVELLY_HILLS, MOUNTAIN, SPARSE, RARE, OVERWORLD);
		addTypes(Biome.WINDSWEPT_HILLS, MOUNTAIN, HILLS, OVERWORLD);
		addTypes(Biome.WINDSWEPT_SAVANNA, HOT, DRY, SPARSE, SAVANNA, MOUNTAIN, RARE, OVERWORLD);
		addTypes(Biome.WOODED_BADLANDS, MESA, SANDY, DRY, SPARSE, OVERWORLD, PLATEAU);

	}

}