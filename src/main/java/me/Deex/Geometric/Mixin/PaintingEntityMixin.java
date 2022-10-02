package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(PaintingEntity.class)
public abstract class PaintingEntityMixin extends AbstractDecorationEntity
{
    //TODO: Can I shadow fields from a subclass?

    public PaintingEntityMixin(World world) 
    {
        super(world);
        //Auto-generated constructor stub
    }

    @Overwrite
    public void refreshPositionAndAngles(double x, double y, double z, float yaw, float pitch) 
    {
        this.updatePosition(pos.getX() + x - this.x, pos.getY() + y - this.y, pos.getZ() + z - this.z);
    }

    @Overwrite
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        BlockPos blockPos = this.pos.add(x - this.x, y - this.y, z - this.z);
        this.updatePosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}
