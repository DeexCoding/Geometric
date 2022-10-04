package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.Geometric.API.GeometricAPI;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin 
{
    @Overwrite
    public void playStepSound(BlockPos pos, Block block) 
    {
        Block.Sound sound = block.sound;
        if (this.world.getBlockState(GeometricAPI.ExtendBlockPos(pos).UpThis()).getBlock() == Blocks.SNOW_LAYER) 
        {
            sound = Blocks.SNOW_LAYER.sound;
            this.playSound(sound.getStepSound(), sound.getVolume() * 0.15f, sound.getPitch());
        } else if (!block.getMaterial().isFluid()) 
        {
            this.playSound(sound.getStepSound(), sound.getVolume() * 0.15f, sound.getPitch());
        }
    }
    
    @Shadow
    public abstract void playSound(String id, float volume, float pitch);
    
    @Shadow
    public World world;
}
