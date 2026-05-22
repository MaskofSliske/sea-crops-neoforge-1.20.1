package com.example.maskseacrops.entity.custom;

import com.example.maskseacrops.MaskSeaCrops;
import com.example.maskseacrops.entity.ModEntities;
import com.example.maskseacrops.entity.custom.CactusUrchinEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class CactusUrchinEggEntity extends ThrowableItemProjectile {

    public CactusUrchinEggEntity(Level level, LivingEntity thrower) {
        super(ModEntities.CACTUS_URCHIN_EGG_ENTITY.get(), thrower, level);
    }

    public CactusUrchinEggEntity(EntityType<? extends CactusUrchinEggEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return (Item) MaskSeaCrops.CACTUS_URCHIN_EGG.get();
    }

    //This is a 2-3 min hatch timer for the egg entity
    private int hatchTimer = 2400 + this.random.nextInt(1200);
    private boolean landed = false;

    @Override
    protected void onHitBlock(net.minecraft.world.phys.BlockHitResult result) {
        landed = true;
    }

    @Override
    protected void onHitEntity(net.minecraft.world.phys.EntityHitResult result) {
        landed = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (landed) {
            hatchTimer--;
            if (hatchTimer <=0) {
                if (!this.level().isClientSide) {
                    //Currently, this is at 60%, can change later for balance reasons ofc
                    if (this.random.nextFloat() < 0.8f) {
                        CactusUrchinEntity urchin = ModEntities.CACTUS_URCHIN.get().create(this.level());
                        if (urchin != null) {
                            urchin.setBaby(true);
                            urchin.moveTo(this.getX(), this.getY(), this.getZ());
                            this.level().addFreshEntity(urchin);
                        }
                    }
                    this.discard();
                }
            }
        }
    }
}
