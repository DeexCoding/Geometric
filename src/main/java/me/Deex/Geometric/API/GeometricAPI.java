package me.Deex.Geometric.API;

import me.Deex.Geometric.API.ClassExtensions.BlockPosExtension;
import me.Deex.Geometric.API.ClassExtensions.Vec3iExtension;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class GeometricAPI 
{
    public Vec3iExtension BlockPosToVec3i(BlockPos blockPos)
    {
        return (Vec3iExtension)(Object)blockPos;
    }

    public BlockPosExtension ExtendBlockPos(BlockPos blockPos)
    {
        return (BlockPosExtension)(Object)blockPos;
    }

    public Vec3iExtension ExtendVec3i(Vec3i vec3i)
    {
        return (Vec3iExtension)(Object)vec3i;
    }
}
