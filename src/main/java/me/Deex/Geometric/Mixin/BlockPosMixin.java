package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Mixin;

import me.Deex.Geometric.BlockPosExtension;
import me.Deex.Geometric.Vec3iExtension;
import net.minecraft.util.math.BlockPos;
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
}
