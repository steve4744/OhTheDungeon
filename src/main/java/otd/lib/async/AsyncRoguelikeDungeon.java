/* 
 * Copyright (C) 2021 shadow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package otd.lib.async;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import forge_sandbox.greymerk.roguelike.dungeon.Dungeon;
import forge_sandbox.greymerk.roguelike.dungeon.DungeonLevel;
import forge_sandbox.greymerk.roguelike.dungeon.IDungeonLevel;
import forge_sandbox.greymerk.roguelike.dungeon.settings.DungeonSettings;
import forge_sandbox.greymerk.roguelike.dungeon.settings.ISettings;
import forge_sandbox.greymerk.roguelike.dungeon.settings.LevelSettings;
import forge_sandbox.greymerk.roguelike.dungeon.settings.SettingsContainer;
import forge_sandbox.greymerk.roguelike.dungeon.settings.SettingsResolver;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskEncase;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskFilters;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskLayout;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskLinks;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskLoot;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskRooms;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskSegments;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskTower;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.DungeonTaskTunnels;
import forge_sandbox.greymerk.roguelike.dungeon.tasks.IDungeonTask;
import forge_sandbox.greymerk.roguelike.treasure.ITreasureChest;
import forge_sandbox.greymerk.roguelike.worldgen.Coord;
import io.papermc.lib.PaperLib;
import otd.Main;
import otd.config.SimpleWorldConfig;
import otd.config.WorldConfig;
import otd.lib.ZoneWorld;
import otd.lib.async.later.roguelike.Later;
import otd.util.FileUtils;
import otd.util.ProtectBlock;

/**
 *
 * @author
 */
public class AsyncRoguelikeDungeon {
	private static final String SETTINGS_DIRECTORY = Main.instance.getDataFolder().toString() + File.separator
			+ "forge_sandbox" + File.separator + "roguelike" + File.separator + "settings";

	static {
		try {
			initResolver();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			Bukkit.getLogger().log(Level.SEVERE, sw.toString());
		}
	}

	public static SettingsResolver settingsResolver;

	public static void initResolver() throws Exception {
		Bukkit.getLogger().log(Level.INFO, "AsyncRoguelikeDungeon initResolver");

		File settingsDir = new File(SETTINGS_DIRECTORY);

		if (settingsDir.exists() && !settingsDir.isDirectory()) {
			throw new Exception("Settings directory is a file");
		}

		if (!settingsDir.exists()) {
			settingsDir.mkdir();
		}

		File[] settingsFiles = settingsDir.listFiles();
		Arrays.sort(settingsFiles);

		SettingsContainer settings = new SettingsContainer();
		settingsResolver = new SettingsResolver(settings);

		Map<String, String> files = new HashMap<>();

		for (File file : Arrays.asList(settingsFiles)) {

			if (!FileUtils.getExtension(file).equals("json"))
				continue;

			try {
				@SuppressWarnings("deprecation")
				String content = Files.toString(file, Charsets.UTF_8);
				files.put(file.getName(), content);
			} catch (IOException e) {
				throw new Exception("Error reading file : " + file.getName());
			}
		}

		settings.parseCustomSettings(files);
	}

	private final static IDungeonTask[] TASKS = { new DungeonTaskLayout(), new DungeonTaskEncase(),
			new DungeonTaskTunnels(), new DungeonTaskRooms(), new DungeonTaskSegments(), new DungeonTaskLinks(),
			new DungeonTaskTower(), new DungeonTaskFilters(), };

	private final static Map<UUID, Integer> POOL = new HashMap<>();

	public static boolean generateAsync(Random rand, AsyncWorldEditor editor, int x, int z) {
		int offset = 0;
		if (WorldConfig.wc.dict.containsKey(editor.getWorldName())) {
			SimpleWorldConfig swc = WorldConfig.wc.dict.get(editor.getWorldName());
			int sealevel = swc.worldParameter.sealevel;

			offset = sealevel - AsyncWorldEditor.DEFAULT_SEALEVEL;
		}

		if (offset != 0) {
			editor.setVirtualGroundHeight(editor.getVirtualGroundHeight() - offset);
		}

		return generateAsync(rand, editor, x, z, null, null, offset);
	}

	public static boolean generateAsync(Random rand, AsyncWorldEditor editor, int x, int z, Set<String> chunk_set,
			DungeonSettings theme, int sealevel_offset) {
		if (Dungeon.settingsResolver == null)
			return false;

		UUID id;
		do {
			id = UUID.randomUUID();
		} while (POOL.containsKey(id));
		UUID uuid = id;

		final int offset_y = sealevel_offset; // sum_y;

		Coord location = new Coord(x, 0, z);
		ISettings setting;
		try {
			if (theme == null)
				setting = Dungeon.settingsResolver.getSettings(editor, rand, location);
			else
				setting = Dungeon.settingsResolver.generateSettings(theme, editor, rand, location);

			if (setting == null)
				return false;
			if (WorldConfig.wc.dict.containsKey(editor.getWorldName())) {
				SimpleWorldConfig swc = WorldConfig.wc.dict.get(editor.getWorldName());
				if (!swc.roguelike.builtinLoot) {
					((DungeonSettings) setting).clearLootRule();
				}
			}
		} catch (Exception e) {
			return false;
		}

		Coord start = new Coord(location.getX(), Dungeon.TOPLEVEL, location.getZ());
		Dungeon dungeon = new Dungeon(editor);
		dungeon.origin = start;

		Bukkit.getScheduler().runTaskAsynchronously(otd.Main.instance, () -> {
			List<IDungeonLevel> levels = dungeon.getLevels();
			int numLevels = setting.getNumLevels();
			// create level objects
			for (int i = 0; i < numLevels; ++i) {
				LevelSettings levelSettings = setting.getLevelSettings(i);
				DungeonLevel level = new DungeonLevel(editor, rand, levelSettings, new Coord(start));
				levels.add(level);
			}

			for (IDungeonTask task : TASKS) {
				int sub_index = 0;
				while (!task.execute(editor, rand, dungeon, setting, sub_index))
					sub_index++;
			}

			Set<int[]> chunks0 = editor.getAsyncWorld().getCriticalChunks(chunk_set);
			synchronized (POOL) {
				POOL.put(uuid, chunks0.size());
			}

			int delay = 0;

			for (int[] chunk : chunks0) {
				int chunkX = chunk[0];
				int chunkZ = chunk[1];
				if (chunk_set != null && !chunk_set.contains(chunkX + "," + chunkZ))
					continue;

				List<ZoneWorld.CriticalNode> cn = editor.getAsyncWorld().getCriticalBlock(chunkX, chunkZ);
				List<Later> later = editor.getAsyncWorld().getCriticalLater(chunkX, chunkZ);

//				if (!PaperLib.isPaper())
				delay += 1;

				Bukkit.getScheduler().runTaskLater(otd.Main.instance, () -> {
					boolean flag = false;
					synchronized (POOL) {
						if (!POOL.containsKey(uuid))
							flag = true;
					}
					if (flag)
						return;

					PaperLib.getChunkAtAsync(editor.getWorld(), chunkX, chunkZ, true).thenAccept((Chunk c) -> {
						// Bukkit.getLogger().log(Level.INFO, chunkX + "," + chunkZ);
						List<int[]> delay_pos = new ArrayList<>();
						List<Material> delay_materials = new ArrayList<>();

						for (ZoneWorld.CriticalNode node : cn) {
							int[] pos = node.pos;
							Material material;
							if (node.bd != null)
								material = node.bd.getMaterial();
							else
								material = node.material;
							boolean patch = false;
							if (material == Material.IRON_BARS || material == Material.REDSTONE_WIRE
									|| material == Material.WATER || material == Material.LAVA
									|| material == Material.OAK_FENCE || material == Material.SPRUCE_FENCE
									|| material == Material.JUNGLE_FENCE || material == Material.BIRCH_FENCE
									|| material == Material.DARK_OAK_FENCE || material == Material.ACACIA_FENCE
									|| material == Material.NETHER_BRICK_FENCE) {
								patch = true;
							}
							if (patch) {
								delay_materials.add(material);
								delay_pos.add(pos);
							} else {
								if (node.bd != null) {
									Block block = c.getBlock(pos[0], pos[1] + offset_y, pos[2]);
									Material type = block.getType();
									if (!ProtectBlock.isProtectedType(type))
										block.setBlockData(node.bd, false);
								} else {
									Block block = c.getBlock(pos[0], pos[1] + offset_y, pos[2]);
									Material type = block.getType();
									if (!ProtectBlock.isProtectedType(type))
										block.setType(node.material, false);
								}
							}
						}

						int len = delay_pos.size();
						for (int i = 0; i < len; i++) {
							int[] pos = delay_pos.get(i);
							Block block = c.getBlock(pos[0], pos[1] + offset_y, pos[2]);
							if (!ProtectBlock.isProtectedType(block.getType()))
								block.setType(delay_materials.get(i), true);
						}

						if (later != null) {
							for (Later l : later) {
								l.setOffset(0, offset_y, 0);
								l.doSomethingInChunk(c);
							}
						}

						boolean isFinish = false;
						synchronized (POOL) {
							if (POOL.containsKey(uuid)) {
								int count = POOL.get(uuid);
								count--;
								// Bukkit.getLogger().log(Level.SEVERE, ":"+count);
								POOL.put(uuid, count);

								if (count == 0) {
									isFinish = true;
									POOL.remove(uuid);
								}
							}
						}

						if (isFinish) {
//                                    if(theme != null) {
//                                        Bukkit.getLogger().log(Level.INFO, "add loot");
//                                    }
							AsyncRoguelikeDungeon.addLoot(editor, rand, dungeon, setting);
						}
					});

				}, delay);
			}

		});

		return true;
	}

	public static final String errfile = Main.instance.getDataFolder().toString() + File.separator + "err_rpt.txt";

	public static final boolean init() {
		return errfile != null;
	}

	public static void addLoot(AsyncWorldEditor editor, Random rand, Dungeon dungeon, ISettings setting) {
		int sub_index = 0;
		DungeonTaskLoot dtl = new DungeonTaskLoot();
		while (!dtl.execute(editor, rand, dungeon, setting, sub_index))
			sub_index++;
		for (ITreasureChest chest : editor.getTreasure().chests) {
			chest.addBufferedItems(editor);
		}
	}
}
