/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2.generator;

import forge_sandbox.BlockPos;
import java.util.Map;
import java.util.Random;

import forge_sandbox.com.someguyssoftware.dungeons2.generator.blockprovider.IDungeonsBlockProvider;
import forge_sandbox.com.someguyssoftware.dungeons2.generator.strategy.IRoomGenerationStrategy;
import forge_sandbox.com.someguyssoftware.dungeons2.model.LevelConfig;
import forge_sandbox.com.someguyssoftware.dungeons2.style.DesignElement;
import forge_sandbox.com.someguyssoftware.dungeons2.style.Layout;
import forge_sandbox.com.someguyssoftware.dungeons2.style.Style;
import forge_sandbox.com.someguyssoftware.dungeons2.style.StyleSheet;
import forge_sandbox.com.someguyssoftware.dungeons2.style.Theme;
import forge_sandbox.com.someguyssoftware.dungeonsengine.config.ILevelConfig;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import otd.lib.async.AsyncWorldEditor;

/**
 * @author Mark Gottschling on Aug 28, 2016
 *
 */
public abstract class AbstractRoomGenerationStrategy implements IRoomGenerationStrategy {
	private IDungeonsBlockProvider blockProvider;

	/**
	 * 
	 * @param provider
	 */
	public AbstractRoomGenerationStrategy(IDungeonsBlockProvider provider) {
		this.blockProvider = provider;
	}

	/**
	 * 
	 * @param arrangement
	 * @param coords
	 * @param postProcess
	 * @return
	 */
	protected boolean isPostProcessed(Arrangement arrangement, ICoords coords, Map<ICoords, Arrangement> postProcess) {
		if (arrangement.getElement() == DesignElement.LADDER) {
			// add to post-process list/map
			postProcess.put(coords, arrangement);
			return true;
		}
		return false;
	}

	protected void postProcess(AsyncWorldEditor world, Random random, Map<ICoords, Arrangement> post, Layout layout,
			Theme theme, StyleSheet styleSheet, ILevelConfig config) {
		BlockData blockState;

		for (Map.Entry<ICoords, Arrangement> e : post.entrySet()) {
			ICoords keyCoords = e.getKey();
			Arrangement a = e.getValue();
			DesignElement elem = a.getElement();

			/*
			 * check to see if a block is adjacent to the post processed block. this is
			 * checking for a "backing" block for the post processed block because the pp
			 * block can not exist without a block for support
			 */
			BlockPos pos = keyCoords.toPos();
			Material state1 = world.getBlockState(pos.east());
			Material state2 = world.getBlockState(pos.west());
			Material state3 = world.getBlockState(pos.north());
			Material state4 = world.getBlockState(pos.south());
			boolean flag = false;
			// this is wrong. should set a flag if any is normal cube
			if ((state1 != null && state1.isBlock()) || (state2 != null && state2.isBlock())
					|| (state3 != null && state3.isBlock()) || (state4 != null && state4.isBlock())) {
				flag = true;
			}
			if (!flag)
				return;

			// get the style for the element
			Style style = getBlockProvider().getStyle(elem, layout, theme, styleSheet);
			int decayIndex = getBlockProvider().getDecayIndex(random, config.getDecayMultiplier(), style);
			// get calculated blockstate
			blockState = getBlockProvider().getBlockState(a, style, decayIndex);
			// update the world with the blockState
			if (blockState != null && blockState != IDungeonsBlockProvider.NULL_BLOCK) {
				world.setBlockState(keyCoords.toPos(), blockState, 3);
			}
		}
	}

	/**
	 * @param world
	 * @param random
	 * @param post
	 * @param theme
	 * @param layout
	 * @param styleSheet
	 * @param config
	 */
	@Deprecated
	protected void postProcess(AsyncWorldEditor world, Random random, Map<ICoords, Arrangement> post, Layout layout,
			Theme theme, StyleSheet styleSheet, LevelConfig config) {
//        IBlockState blockState = null;
//        
//        for (Entry<ICoords, Arrangement> e : post.entrySet()) {
//            ICoords keyCoords = e.getKey();
//            Arrangement a = e.getValue();
//            DesignElement elem = a.getElement();
//            
//            /* 
//             * check to see if a block is adjacent to the post processed block.
//             * this is checking for a "backing" block for the post processed block
//             * because the pp block  can not exist without a block for support
//             */
//            BlockPos pos = keyCoords.toPos();
//            IBlockState state1 = world.getBlockState(pos.east());
//            IBlockState state2 = world.getBlockState(pos.west());
//            IBlockState state3 = world.getBlockState(pos.north());
//            IBlockState state4 = world.getBlockState(pos.south());
//            boolean flag = false;
//            // this is wrong. should set a flag if any is normal cube
//            if ((state1 != null && state1.getBlock().isNormalCube(state1, world, null)) ||
//                    (state2 != null && state2.getBlock().isNormalCube(state2, world, null)) ||
//                    (state3 != null && state3.getBlock().isNormalCube(state3, world, null)) ||
//                    (state4 != null && state4.getBlock().isNormalCube(state4, world, null))) {
//                flag = true;
//            }
//            if (!flag) return;
//            
//            // get the style for the element
//            Style style = getBlockProvider().getStyle(elem, layout, theme, styleSheet);
//            int decayIndex = getBlockProvider().getDecayIndex(random, config.getDecayMultiplier(), style);
//            // get calculated blockstate
//            blockState = getBlockProvider().getBlockState(a, style, decayIndex);            
//            // update the world with the blockState
//            if (blockState != null && blockState != IDungeonsBlockProvider.NULL_BLOCK) {
//                world.setBlockState(keyCoords.toPos(), blockState, 3);
//            }
//        }        
	}

	/**
	 * @return the blockProvider
	 */
	@Override
	public IDungeonsBlockProvider getBlockProvider() {
		return blockProvider;
	}

	/**
	 * @param blockProvider the blockProvider to set
	 */
//    @Override
//    public void setBlockProvider(IDungeonsBlockProvider blockProvider) {
//        this.blockProvider = blockProvider;
//    }
}
