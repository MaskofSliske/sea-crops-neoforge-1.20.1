package com.example.maskseacrops.entity.custom;

import com.example.maskseacrops.MaskSeaCrops;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.swing.text.html.parser.Entity;

public class CactusUrchinEntity extends Animal {

    private int eggLayTimer = 12000 + this.random.nextInt(2000);

    public CactusUrchinEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3D)
                .add(Attributes.MOVEMENT_SPEED, 0.4);
    }

    @Override
    public net.minecraft.world.entity.MobType getMobType() {
        return net.minecraft.world.entity.MobType.WATER;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 0.4D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
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
            }
        }
    }

    @Override
    public void playerTouch(Player player) {
        player.hurt(this.damageSources().cactus(), 1.0f);
    }

}