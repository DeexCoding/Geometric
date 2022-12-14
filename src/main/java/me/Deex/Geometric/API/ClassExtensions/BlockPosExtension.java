package me.Deex.Geometric.API.ClassExtensions;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface BlockPosExtension 
{
    public void CopyPosition(BlockPos blockPos);

    public BlockPos UpThis();
    public BlockPos DownThis();
    public BlockPos EastThis();
    public BlockPos WestThis();
    public BlockPos SouthThis();
    public BlockPos NorthThis();
    public BlockPos OffsetThis(Direction direction);
    public BlockPos OffsetThis(Direction direction, int count);
    public BlockPos OffsetThisInOppositeDirection(Direction opposieTo);
}
