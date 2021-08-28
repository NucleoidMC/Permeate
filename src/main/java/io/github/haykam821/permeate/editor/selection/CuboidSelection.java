package io.github.haykam821.permeate.editor.selection;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CuboidSelection implements Selection {
	private final BlockPos start;
	private final BlockPos end;

	public CuboidSelection(BlockPos start, BlockPos end) {
		this.start = CuboidSelection.min(start, end);
		this.end = CuboidSelection.max(start, end);
	}

	public BlockPos getStart() {
		return this.start;
	}

	public BlockPos getEnd() {
		return this.end;
	}

	@Override
	public Iterable<BlockPos> iterate() {
		return BlockPos.iterate(this.start, this.end);
	}

	@Override
	public int getVolume() {
		int x = this.end.getX() - this.start.getX() + 1;
		int y = this.end.getY() - this.start.getY() + 1;
		int z = this.end.getZ() - this.start.getZ() + 1;

		return x * y * z;
	}

	@Override
	public Selection offset(Direction direction, int distance) {
		return new CuboidSelection(this.start.offset(direction, distance), this.end.offset(direction, distance));
	}

	private static BlockPos min(BlockPos start, BlockPos end) {
		int x = Math.min(start.getX(), end.getX());
		int y = Math.min(start.getY(), end.getY());
		int z = Math.min(start.getZ(), end.getZ());

		return new BlockPos(x, y, z);
	}

	private static BlockPos max(BlockPos start, BlockPos end) {
		int x = Math.max(start.getX(), end.getX());
		int y = Math.max(start.getY(), end.getY());
		int z = Math.max(start.getZ(), end.getZ());

		return new BlockPos(x, y, z);
	}
}
