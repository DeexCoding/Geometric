package me.Deex.Geometric;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface BlockPosExtension 
{
    public BlockPos UpThis();
    public BlockPos DownThis();
    public BlockPos EastThis();
    public BlockPos WestThis();
    public BlockPos SouthThis();
    public BlockPos NorthThis();
    public BlockPos OffsetThis(Direction direction);
    public BlockPos OffsetThisInOppositeDirection(Direction opposieTo);
}
