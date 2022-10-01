package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.Geometric.BlockPosExtension;
import me.Deex.Geometric.Vec3iExtension;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin 
{
    public Block getBlockAt(BlockPos pos) 
    {
        BlockPos blockPos = new BlockPos(pos.getX(), this.getSeaLevel(), pos.getZ());

        while (!this.isAir(((BlockPosExtension)(Object)blockPos).UpThis())) 
        {
        }

        ((Vec3iExtension)(Object)(Vec3i)blockPos).setY(blockPos.getY() - 1);
        return this.getBlockState(blockPos).getBlock();
    }
    
    @Shadow
    public abstract int getSeaLevel();

    @Shadow
    public abstract boolean isAir(BlockPos pos);
    
    @Shadow
    public abstract BlockState getBlockState(BlockPos pos);
}
