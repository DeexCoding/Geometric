package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.Geometric.API.GeometricAPI;
import me.Deex.Geometric.API.ClassExtensions.Vec3iExtension;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.EndChunkGenerator;

@Mixin(EndChunkGenerator.class)
public class EndChunkGeneratorMixin 
{
    @Shadow
    private World world;

    @Overwrite
    public void decorateChunk(ChunkProvider provider, int x, int z) 
    {
        FallingBlock.field_7453 = true;
        BlockPos blockPos = new BlockPos((x * 16) + 16, 0, (z * 16) + 16);
        Biome biome = this.world.getBiome(blockPos);
        Vec3iExtension blockPosAsV3i = GeometricAPI.BlockPosToVec3i(blockPos);
        
        blockPosAsV3i.setX(blockPos.getX() - 16);
        blockPosAsV3i.setZ(blockPos.getZ() - 16);

        biome.decorate(this.world, this.world.random, blockPos);
        FallingBlock.field_7453 = false;
    }
}
