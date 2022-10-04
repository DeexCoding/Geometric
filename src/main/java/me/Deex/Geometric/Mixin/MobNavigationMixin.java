package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.Geometric.API.ClassExtensions.Vec3iExtension;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(MobNavigation.class)
public abstract class MobNavigationMixin extends EntityNavigation
{
    public MobNavigationMixin(MobEntity mob, World world) {
        super(mob, world);
        //TODO Auto-generated constructor stub
    }

    @Overwrite
    private int getPathfindingY() 
    {
        if (!this.mob.isTouchingWater() || !this.canSwim()) 
        {
            return (int)(this.mob.getBoundingBox().minY + 0.5);
        }

        int i = (int)this.mob.getBoundingBox().minY;
        Block block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.mob.x), i, MathHelper.floor(this.mob.z))).getBlock();
        int j = 0;

        BlockPos blockPos = new BlockPos(0, 0, 0);

        while (block == Blocks.FLOWING_WATER || block == Blocks.WATER) 
        {
            ((Vec3iExtension)(Object)blockPos).set(MathHelper.floor(this.mob.x), ++i, MathHelper.floor(this.mob.z));
            block = this.world.getBlockState(blockPos).getBlock();
            if (++j <= 16) continue;
            return (int)this.mob.getBoundingBox().minY;
        }

        return i;
    }

    @Overwrite
    public void adjustPath() 
    {
        super.adjustPath();
        if (this.avoidSunlight) 
        {
            if (this.world.hasDirectSunlight(new BlockPos(MathHelper.floor(this.mob.x), (int)(this.mob.getBoundingBox().minY + 0.5), MathHelper.floor(this.mob.z)))) 
            {
                return;
            }

            BlockPos blockPos = new BlockPos(0, 0, 0);
            PathNode pathNode;

            for (int i = 0; i < this.currentPath.getNodeCount(); ++i) 
            {
                pathNode = this.currentPath.getNode(i);
                ((Vec3iExtension)(Object)blockPos).set(pathNode.posX, pathNode.posY, pathNode.posZ);
                if (this.world.hasDirectSunlight(blockPos))
                {
                    this.currentPath.setNodeCount(i - 1);
                    return;
                }
            }
        }
    }

    @Shadow
    private boolean allVisibleAreSafe(int centerX, int centerY, int centerZ, int sizeX, int sizeY, int sizeZ, Vec3d entityPos, double lookVecX, double lookVecZ) 
    {
        int i = centerX - sizeX / 2;
        int j = centerZ - sizeZ / 2;

        if (!this.allVisibleArePassable(i, centerY, j, sizeX, sizeY, sizeZ, entityPos, lookVecX, lookVecZ))
        {
            return false;
        }

        BlockPos blockPos = new BlockPos(0, 0, 0);

        for (int k = i; k < i + sizeX; ++k) 
        {
            for (int l = j; l < j + sizeZ; ++l) 
            {
                double d = (double)k + 0.5 - entityPos.x;
                double e = (double)l + 0.5 - entityPos.z;

                if (d * lookVecX + e * lookVecZ < 0.0) continue;

                ((Vec3iExtension)(Object)blockPos).set(k, centerY - 1, l);
                Block block = this.world.getBlockState(blockPos).getBlock();

                Material material = block.getMaterial();

                if (material == Material.AIR) 
                {
                    return false;
                }

                if (material == Material.WATER && !this.mob.isTouchingWater()) 
                {
                    return false;
                }

                if (material != Material.LAVA) continue;

                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether all blocks in the box which are visible (in front of) the mob can be pathed through
     */
    @Overwrite
    private boolean allVisibleArePassable(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d entityPos, double lookVecX, double lookVecZ)
    {
        for (BlockPos blockPos : BlockPos.iterate(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1)))
        {
            double d = (double)blockPos.getX() + 0.5 - entityPos.x;
            if (d * lookVecX + ((double)blockPos.getZ() + 0.5 - entityPos.z) * lookVecZ < 0.0 || (this.world.getBlockState(blockPos).getBlock()).blocksMovement(this.world, blockPos)) continue;
            return false;
        }

        return true;
    }

    @Shadow
    private boolean avoidSunlight;

    @Shadow
    public abstract boolean canSwim();

    @Override
    @Shadow
    protected PathNodeNavigator createNavigator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Shadow
    protected Vec3d getPos() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Shadow
    protected boolean isAtValidPosition() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @Shadow
    protected boolean canPathDirectlyThrough(Vec3d var1, Vec3d var2, int var3, int var4, int var5) {
        // TODO Auto-generated method stub
        return false;
    }
}
