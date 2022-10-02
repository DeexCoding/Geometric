package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Mixin;

import me.Deex.Geometric.BlockPosExtension;
import me.Deex.Geometric.Vec3iExtension;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

@Mixin(BlockPos.class)
public class BlockPosMixin extends Vec3i implements BlockPosExtension
{
    public BlockPosMixin(int x, int y, int z) 
    {
        super(x, y, z);
    }

    @Override
    public BlockPos UpThis()
    {
        ((Vec3iExtension)(Object)(Vec3i)(BlockPos)(Object)this).setY(getY() + 1);
        return (BlockPos)(Object)this;
    }

    @Override
    public BlockPos DownThis() 
    {
        ((Vec3iExtension)(Object)(Vec3i)(BlockPos)(Object)this).setY(getY() - 1);
        return (BlockPos)(Object)this;
    }
    
    @Override
    public BlockPos EastThis()
    {
        ((Vec3iExtension)(Object)(Vec3i)(BlockPos)(Object)this).setX(getX() + 1);
        return (BlockPos)(Object)this;
    }

    @Override
    public BlockPos WestThis()
    {
        ((Vec3iExtension)(Object)(Vec3i)(BlockPos)(Object)this).setX(getX() - 1);
        return (BlockPos)(Object)this;
    }

    @Override
    public BlockPos SouthThis()
    {
        ((Vec3iExtension)(Object)(Vec3i)(BlockPos)(Object)this).setZ(getZ() + 1);
        return (BlockPos)(Object)this;
    }

    @Override
    public BlockPos NorthThis()
    {
        ((Vec3iExtension)(Object)(Vec3i)(BlockPos)(Object)this).setZ(getZ() - 1);
        return (BlockPos)(Object)this;
    }
    
    @Override
    public BlockPos OffsetThis(Direction direction)
    {
        ((Vec3iExtension)(Object)(Vec3i)this).setX(getX() + direction.getVector().getX());
        ((Vec3iExtension)(Object)(Vec3i)this).setY(getY() + direction.getVector().getY());
        ((Vec3iExtension)(Object)(Vec3i)this).setZ(getZ() + direction.getVector().getZ());

        return (BlockPos)(Object)this;
    }
    
    public BlockPos OffsetThisInOppositeDirection(Direction opposieTo)
    {
        ((Vec3iExtension)(Object)(Vec3i)this).setX(getX() - opposieTo.getVector().getX());
        ((Vec3iExtension)(Object)(Vec3i)this).setY(getY() - opposieTo.getVector().getY());
        ((Vec3iExtension)(Object)(Vec3i)this).setZ(getZ() - opposieTo.getVector().getZ());

        return (BlockPos)(Object)this;
    }
}
