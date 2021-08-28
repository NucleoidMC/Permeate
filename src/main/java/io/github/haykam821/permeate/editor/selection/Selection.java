package io.github.haykam821.permeate.editor.selection;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface Selection {
	public Iterable<BlockPos> iterate();

	/**
	 * {@return number of blocks in this selection}
	 */
	public int getVolume();

	public Selection offset(Direction direction, int distance);
}
