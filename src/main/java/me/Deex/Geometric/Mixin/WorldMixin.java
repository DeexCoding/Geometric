package me.Deex.Geometric.Mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.Geometric.API.ClassExtensions.BlockPosExtension;
import me.Deex.Geometric.API.ClassExtensions.Vec3iExtension;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelProperties;

@Mixin(World.class)
public abstract class WorldMixin 
{
    @Overwrite
    public Block getBlockAt(BlockPos pos) 
    {
        BlockPos blockPos = new BlockPos(pos.getX(), this.getSeaLevel(), pos.getZ());

        while (!this.isAir(((BlockPosExtension)(Object)blockPos).UpThis())) 
        {
        }

        ((Vec3iExtension)(Object)(Vec3i)blockPos).setY(blockPos.getY() - 1);
        return this.getBlockState(blockPos).getBlock();
    }

    @Overwrite
    public BlockPos getTopPosition(BlockPos pos) 
    {
        Material material;
        Chunk chunk = this.getChunk(pos);
        BlockPos blockPos = new BlockPos(pos.getX(), chunk.getHighestNonEmptySectionYOffset() + 16, pos.getZ());
        while (true) 
        {
            if (blockPos.getY() < 0)
            {
                break;
            }

            material = chunk.getBlockAtPos(((BlockPosExtension)(Object)blockPos).DownThis()).getMaterial();

            if (material.blocksMovement())
            {
                if (material != Material.FOILAGE)
                {
                    ((BlockPosExtension)(Object)blockPos).UpThis();
                    break;
                }
            }
        }

        return blockPos;
    }

    @Overwrite
    public boolean recievesSunlight(BlockPos pos) 
    {
        //TODO: Technically we don't need to do this, since this function never gets called in a way where it reads pos without changing y after the call
        int startY = pos.getY();

        if (pos.getY() >= this.getSeaLevel()) 
        {
            return this.hasDirectSunlight(pos);
        }

        ((Vec3iExtension)(Object)(Vec3i)pos).setY(this.getSeaLevel());

        if (!this.hasDirectSunlight(pos)) 
        {
            ((Vec3iExtension)(Object)(Vec3i)pos).setY(startY);
            return false;
        }

        ((BlockPosExtension)(Object)pos).DownThis();

        while (pos.getY() > startY) 
        {
            Block block = this.getBlockState(pos).getBlock();

            if (block.getOpacity() > 0 && !block.getMaterial().isFluid()) 
            {
                ((Vec3iExtension)(Object)(Vec3i)pos).setY(startY);
                return false;
            }

            ((BlockPosExtension)(Object)pos).DownThis();
        }

        ((Vec3iExtension)(Object)(Vec3i)pos).setY(startY);
        return true;
    }

    @Overwrite
    public int method_8557(BlockPos osarg) 
    {
        if (osarg.getY() < 0) 
        {
            return 0;
        }
        
        if (osarg.getY() >= 256) 
        {
            ((Vec3iExtension)(Object)(Vec3i)osarg).setY(255);
        }

        return this.getChunk(osarg).getLightLevel(osarg, 0);
    }

    @Overwrite
    public int method_8543(BlockPos pos, boolean bl) 
    {
        if (pos.getX() < -30000000 || pos.getZ() < -30000000 || pos.getX() >= 30000000 || pos.getZ() >= 30000000) 
        {
            return 15;
        }

        if (bl && this.getBlockState(pos).getBlock().usesNeighbourLight()) 
        {
            //int i = this.method_8543(pos.up(), false);
            //int j = this.method_8543(pos.east(), false);
            //int k = this.method_8543(pos.west(), false);
            //int l = this.method_8543(pos.south(), false);
            //int m = this.method_8543(pos.north(), false);

            //TODO: Figure out a better order
            ((BlockPosExtension)(Object)pos).UpThis();
            int i = this.method_8543(pos.up(), false);
            ((BlockPosExtension)(Object)pos).DownThis();
            ((BlockPosExtension)(Object)pos).EastThis();
            int j = this.method_8543(pos.east(), false);
            ((BlockPosExtension)(Object)pos).WestThis();
            ((BlockPosExtension)(Object)pos).WestThis();
            int k = this.method_8543(pos.west(), false);
            ((BlockPosExtension)(Object)pos).EastThis();
            ((BlockPosExtension)(Object)pos).SouthThis();
            int l = this.method_8543(pos.south(), false);
            ((BlockPosExtension)(Object)pos).NorthThis();
            ((BlockPosExtension)(Object)pos).NorthThis();
            int m = this.method_8543(pos.north(), false);
            ((BlockPosExtension)(Object)pos).SouthThis();

            if (j > i) 
            {
                i = j;
            }
            if (k > i) 
            {
                i = k;
            }
            if (l > i) 
            {
                i = l;
            }
            if (m > i) 
            {
                i = m;
            }
            return i;
        }

        if (pos.getY() < 0) 
        {
            return 0;
        }

        if (pos.getY() >= 256) 
        {
            ((Vec3iExtension)(Object)(Vec3i)pos).setY(255);
        }

        Chunk chunk = this.getChunk(pos);
        return chunk.getLightLevel(pos, this.ambientDarkness);
    }

    @Overwrite
    public int getLuminance(LightType type, BlockPos pos) 
    {
        if (this.dimension.isNether() && type == LightType.SKY) 
        {
            return 0;
        }

        if (pos.getY() < 0) 
        {
            ((Vec3iExtension)(Object)(Vec3i)pos).setY(0);
        }

        if (!this.isValidPos(pos)) 
        {
            return type.defaultValue;
        }

        if (!this.blockExists(pos)) 
        {
            return type.defaultValue;
        }

        if (this.getBlockState(pos).getBlock().usesNeighbourLight()) 
        {
            //int i = this.getLightAtPos(type, pos.up());
            //int j = this.getLightAtPos(type, pos.east());
            //int k = this.getLightAtPos(type, pos.west());
            //int l = this.getLightAtPos(type, pos.south());
            //int m = this.getLightAtPos(type, pos.north());
            
            //TODO: Figure out a better order
            ((BlockPosExtension)(Object)pos).UpThis();
            int i = this.getLightAtPos(type, pos);
            ((BlockPosExtension)(Object)pos).DownThis();
            ((BlockPosExtension)(Object)pos).EastThis();
            int j = this.getLightAtPos(type, pos);
            ((BlockPosExtension)(Object)pos).WestThis();
            ((BlockPosExtension)(Object)pos).WestThis();
            int k = this.getLightAtPos(type, pos);
            ((BlockPosExtension)(Object)pos).EastThis();
            ((BlockPosExtension)(Object)pos).SouthThis();
            int l = this.getLightAtPos(type, pos);
            ((BlockPosExtension)(Object)pos).NorthThis();
            ((BlockPosExtension)(Object)pos).NorthThis();
            int m = this.getLightAtPos(type, pos);
            ((BlockPosExtension)(Object)pos).SouthThis();

            if (j > i) 
            {
                i = j;
            }

            if (k > i) 
            {
                i = k;
            }

            if (l > i) 
            {
                i = l;
            }

            if (m > i) 
            {
                i = m;
            }
            return i;
        }

        Chunk chunk = this.getChunk(pos);
        return chunk.getLightAtPos(type, pos);
    }

    @Overwrite
    public int getLightAtPos(LightType type, BlockPos pos) 
    {
        if (pos.getY() < 0) 
        {
            ((Vec3iExtension)(Object)(Vec3i)pos).setY(0);
        }

        if (!this.isValidPos(pos)) 
        {
            return type.defaultValue;
        }

        if (!this.blockExists(pos)) 
        {
            return type.defaultValue;
        }

        Chunk chunk = this.getChunk(pos);
        return chunk.getLightAtPos(type, pos);
    }

    @Overwrite
    public BlockHitResult rayTrace(Vec3d start, Vec3d end, boolean bl, boolean bl2, boolean bl3) 
    {
        if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
            return null;
        }
        if (Double.isNaN(end.x) || Double.isNaN(end.y) || Double.isNaN(end.z)) {
            return null;
        }
        BlockHitResult blockHitResult;
        int l = MathHelper.floor(start.x);
        int m = MathHelper.floor(start.y);
        int n = MathHelper.floor(start.z);
        BlockPos blockPos = new BlockPos(l, m, n);
        BlockState blockState = this.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if ((!bl2 || block.getCollisionBox((World)(Object)this, blockPos, blockState) != null) && block.canCollide(blockState, bl) && (blockHitResult = block.method_414((World)(Object)this, blockPos, start, end)) != null) {
            return blockHitResult;
        }
        int i = MathHelper.floor(end.x);
        int j = MathHelper.floor(end.y);
        int k = MathHelper.floor(end.z);
        blockHitResult = null;
        int o = 200;
        while (o-- >= 0) {
            if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                return null;
            }
            if (l == i && m == j && n == k) {
                return bl3 ? blockHitResult : null;
            }
            boolean bl4 = true;
            boolean bl5 = true;
            boolean bl6 = true;
            double d = 999.0;
            double e = 999.0;
            double f = 999.0;
            if (i > l) {
                d = (double)l + 1.0;
            } else if (i < l) {
                d = (double)l + 0.0;
            } else {
                bl4 = false;
            }
            if (j > m) {
                e = (double)m + 1.0;
            } else if (j < m) {
                e = (double)m + 0.0;
            } else {
                bl5 = false;
            }
            if (k > n) {
                f = (double)n + 1.0;
            } else if (k < n) {
                f = (double)n + 0.0;
            } else {
                bl6 = false;
            }
            double g = 999.0;
            double h = 999.0;
            double p = 999.0;
            double q = end.x - start.x;
            double r = end.y - start.y;
            double s = end.z - start.z;
            if (bl4) {
                g = (d - start.x) / q;
            }
            if (bl5) {
                h = (e - start.y) / r;
            }
            if (bl6) {
                p = (f - start.z) / s;
            }
            if (g == -0.0) {
                g = -1.0E-4;
            }
            if (h == -0.0) {
                h = -1.0E-4;
            }
            if (p == -0.0) {
                p = -1.0E-4;
            }
            Direction direction;
            if (g < h && g < p) {
                direction = i > l ? Direction.WEST : Direction.EAST;
                start = new Vec3d(d, start.y + r * g, start.z + s * g);
            } else if (h < p) {
                direction = j > m ? Direction.DOWN : Direction.UP;
                start = new Vec3d(start.x + q * h, e, start.z + s * h);
            } else {
                direction = k > n ? Direction.NORTH : Direction.SOUTH;
                start = new Vec3d(start.x + q * p, start.y + r * p, f);
            }
            l = MathHelper.floor(start.x) - (direction == Direction.EAST ? 1 : 0);
            m = MathHelper.floor(start.y) - (direction == Direction.UP ? 1 : 0);
            n = MathHelper.floor(start.z) - (direction == Direction.SOUTH ? 1 : 0);
            
            ((Vec3iExtension)(Object)blockPos).setX(l);
            ((Vec3iExtension)(Object)blockPos).setY(m);
            ((Vec3iExtension)(Object)blockPos).setZ(n);

            blockState = this.getBlockState(blockPos);
            Block block2 = blockState.getBlock();
            if (bl2 && block2.getCollisionBox((World)(Object)this, blockPos, blockState) == null) continue;
            if (block2.canCollide(blockState, bl)) {
                BlockHitResult blockHitResult3 = block2.method_414((World)(Object)this, blockPos, start, end);
                if (blockHitResult3 == null) continue;
                return blockHitResult3;
            }
            blockHitResult = new BlockHitResult(BlockHitResult.Type.MISS, start, direction, blockPos);
        }
        return bl3 ? blockHitResult : null;
    }
    
    @Overwrite
    public void updateHorizontalAdjacent(BlockPos pos, Block block) 
    {
        BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

        for (Direction direction : Direction.DirectionType.HORIZONTAL)
        {
            ((Vec3iExtension)(Object)blockPos).setX(pos.getX());
            ((Vec3iExtension)(Object)blockPos).setY(pos.getY());
            ((Vec3iExtension)(Object)blockPos).setZ(pos.getZ());
            ((BlockPosExtension)(Object)blockPos).OffsetThis(direction);

            if (!this.blockExists(blockPos)) continue;

            BlockState blockState = this.getBlockState(blockPos);

            if (Blocks.UNPOWERED_COMPARATOR.isComparator(blockState.getBlock())) 
            {
                blockState.getBlock().neighborUpdate((World)(Object)this, blockPos, blockState, block);
                continue;
            }

            if (!blockState.getBlock().isFullCube() || !Blocks.UNPOWERED_COMPARATOR.isComparator((blockState = this.getBlockState(((BlockPosExtension)(Object)blockPos).OffsetThis(direction))).getBlock())) continue;
            blockState.getBlock().neighborUpdate((World)(Object)this, blockPos, blockState, block);
        }
    }
    
    public BlockPos getSpawnPos() 
    {
        BlockPos blockPos = new BlockPos(this.levelProperties.getSpawnX(), this.levelProperties.getSpawnY(), this.levelProperties.getSpawnZ());
        
        if (!border.contains(blockPos)) 
        {
            blockPos = this.method_8559(new BlockPos(this.border.getCenterX(), 0.0, this.border.getCenterZ()));
        }

        return blockPos;
    }

    @Shadow
    public abstract BlockPos method_8559(BlockPos pos);

    @Shadow
    @Final
    private WorldBorder border;

    @Shadow
    protected LevelProperties levelProperties;

    @Shadow
    public abstract void neighbourUpdate(BlockPos pos, final Block block);

    @Shadow
    public abstract boolean setAir(BlockPos pos);

    @Shadow
    public abstract void syncWorldEvent(PlayerEntity player, int eventId, BlockPos pos, int data);

    @Shadow
    public abstract boolean blockExists(BlockPos pos);

    //TODO: Figure out a way to auctally shadow this function
    private boolean isValidPos(BlockPos pos) 
    {
        return pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000 && pos.getY() >= 0 && pos.getY() < 256;
    }

    @Shadow
    @Final
    public Dimension dimension;

    @Shadow
    private int ambientDarkness;

    @Shadow
    public abstract boolean hasDirectSunlight(BlockPos pos);

    @Shadow
    public abstract Chunk getChunk(BlockPos pos);

    @Shadow
    public abstract int getSeaLevel();

    @Shadow
    public abstract boolean isAir(BlockPos pos);
    
    @Shadow
    public abstract BlockState getBlockState(BlockPos pos);
}
