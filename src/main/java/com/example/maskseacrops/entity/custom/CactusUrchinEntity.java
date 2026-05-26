package com.example.maskseacrops.entity.custom;

import com.example.maskseacrops.MaskSeaCrops;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.swing.text.html.parser.Entity;

public class CactusUrchinEntity extends Animal {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();


    private int eggLayTimer = 12000 + this.random.nextInt(2000);

    public CactusUrchinEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3D)
                .add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override
    public net.minecraft.world.entity.MobType getMobType() {
        return net.minecraft.world.entity.MobType.WATER;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    public ItemStack getBreedingItem() {
        return ItemStack.EMPTY;
    }

    public CactusUrchinEntity getBreedOffspring(net.minecraft.server.level.ServerLevel level, Animal mate) {
        return null;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    //Current egg laying logic
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isUnderWater()) {
            if (eggLayTimer <= 0) {
                this.playSound(net.minecraft.sounds.SoundEvents.CHICKEN_EGG, 1.0f, 1.0f);
                this.spawnAtLocation(new ItemStack((ItemLike) MaskSeaCrops.CACTUS_URCHIN_EGG.get()));
                eggLayTimer = 12000 + this.random.nextInt(2000);
            } else {
                eggLayTimer--;
            }        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.level().isClientSide) {
            this.idleAnimationState.start(this.tickCount);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.walkAnimationState.animateWhen(this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6, this.tickCount);
            if (this.attackAnimationState.isStarted() && this.tickCount - this.attackAnimationState.getAccumulatedTime() > 25) {
                this.attackAnimationState.stop();
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        this.spawnAtLocation(new ItemStack(MaskSeaCrops.CACTUS_URCHIN_SHELL.get(), 1 + this.random.nextInt(2)));
        int uniCount = this.random.nextInt(3);
        if (uniCount > 0) {
            this.spawnAtLocation(new ItemStack(MaskSeaCrops.CACTUS_URCHIN_UNI.get(), uniCount));
        }
    }

    @Override
    public void playerTouch(Player player) {
        float distance = (float) this.distanceTo(player);
        if (distance <= 1.0f) {
            player.hurt(this.damageSources().cactus(), 1.0f);
            if (this.level().isClientSide) {
                this.attackAnimationState.startIfStopped(this.tickCount);
            }
        }
    }

    public boolean isMoving() {
        boolean moving = this.getDeltaMovement().horizontalDistanceSqr() > 0.0001D;
        return moving;
    }
}