package com.example.maskseacrops.entity;

import com.example.maskseacrops.MaskSeaCrops;
import com.example.maskseacrops.entity.custom.CactusUrchinEggEntity;
import com.example.maskseacrops.entity.custom.CactusUrchinEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MaskSeaCrops.MODID);

    public static final RegistryObject<EntityType<CactusUrchinEntity>> CACTUS_URCHIN =
            ENTITIES.register("cactus_urchin", () -> EntityType.Builder
                    .<CactusUrchinEntity>of(CactusUrchinEntity::new, MobCategory.CREATURE)
                    .sized(0.8F, 0.4F)
                    .build("cactus_urchin"));

    public static final RegistryObject<EntityType<CactusUrchinEggEntity>> CACTUS_URCHIN_EGG_ENTITY =
            ENTITIES.register("cactus_urchin_egg", () -> EntityType.Builder
                    .<CactusUrchinEggEntity>of(CactusUrchinEggEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .build("cactus_urchin_egg"));
}