package me.Deex.Geometric.Mixin.API;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.Geometric.API.ClassExtensions.Vec3iExtension;
import net.minecraft.util.math.Vec3i;

@Mixin(Vec3i.class)
public class Vec3iMixin implements Vec3iExtension
{
    @Shadow
    @Final
    @Mutable
    private int x;
    
    @Shadow
    @Final
    @Mutable
    private int y;
    
    @Shadow
    @Final
    @Mutable
    private int z;

    
    @Override
    public void set(int x, int y, int z) 
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void setX(int newX) 
    {
        x = newX;
    }

    @Override
    public void setY(int newY) 
    {
        y = newY;
    }

    @Override
    public void setZ(int newZ) 
    {
        z = newZ;
    }

}
