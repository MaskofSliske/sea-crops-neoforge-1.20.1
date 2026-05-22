package com.example.maskseacrops.entity;

import com.example.maskseacrops.entity.custom.CactusUrchinEggEntity;
import com.example.maskseacrops.entity.custom.CactusUrchinEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CactusUrchinEggItem extends Item {

    public CactusUrchinEggItem(Properties properties){
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isUnderWater()) {
            if (!level.isClientSide) {
                CactusUrchinEggEntity egg = new CactusUrchinEggEntity (level, player);
                egg.setItem(itemstack);
                egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
                level.addFreshEntity(egg);
            }
            player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, net.minecraft.world.entity.item.ItemEntity entity) {
        if (!entity.level().isClientSide && entity.isInWater()) {
            int age = entity.getAge();
            // Only check at 8 specific points during the item's lifespan of just 4 minutes
            if (age == 600 || age == 1200 || age== 1800 || age == 2400 || age == 3000 || age == 3600 || age == 4200 || age == 4800) {
                if (entity.level().getRandom().nextFloat() < 0.8f) {
                    CactusUrchinEntity urchin = ModEntities.CACTUS_URCHIN.get().create(entity.level());
                    if (urchin != null) {
                        urchin.setBaby(true);
                        urchin.moveTo(entity.getX(), entity.getY(), entity.getZ());
                        entity.level().addFreshEntity(urchin);
                    }
                    entity.discard();
                    return true;
                }
            }
        }
        return false;
    }
}