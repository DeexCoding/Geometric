package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.Geometric.API.ClassExtensions.Vec3iExtension;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
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
