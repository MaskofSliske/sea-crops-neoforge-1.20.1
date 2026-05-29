package com.example.maskseacrops;

import com.example.maskseacrops.entity.CactusUrchinEggItem;
import com.example.maskseacrops.entity.ModEntities;
import com.example.maskseacrops.entity.client.CactusUrchinModel;
import com.example.maskseacrops.entity.client.CactusUrchinRenderer;
import com.example.maskseacrops.entity.custom.CactusUrchinEggEntity;
import com.example.maskseacrops.entity.custom.CactusUrchinEntity;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import javax.swing.text.html.parser.Entity;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MaskSeaCrops.MODID)
public class MaskSeaCrops
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "maskseacrops";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    //Making a Deferred Register for an entity
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    // Silt entry, this is the primary block to grow stuff underwater in this mod!
    public static final RegistryObject<Block> SILT = BLOCKS.register("silt", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).strength(0.5f).sound(SoundType.SAND)));
    public static final RegistryObject<Item> SILT_ITEM = ITEMS.register("silt", () -> new BlockItem(SILT.get(), new Item.Properties()));

    //Sea Wheat entry, this is the underwater equivalent to wheat. It will drop wheat and sea wheat seeds.
    public static final RegistryObject<Block> SEA_WHEAT = BLOCKS.register("sea_wheat", () -> new SeaWheatBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.WET_GRASS).randomTicks().lightLevel(state -> 15).noCollission().replaceable()));
    public static final RegistryObject<Item> SEA_WHEAT_SEEDS = ITEMS.register("sea_wheat_seeds", () -> new ItemNameBlockItem(SEA_WHEAT.get(), new Item.Properties()));

    //Sugar Kelp entry. This will end up dropping sugar cane.
    public static final RegistryObject<Block> SUGAR_KELP = BLOCKS.register("sugar_kelp", () -> new SugarKelpBlock(BlockBehaviour.Properties.copy(Blocks.KELP)));
    public static final RegistryObject<Block> SUGAR_KELP_PLANT = BLOCKS.register("sugar_kelp_plant", () -> new SugarKelpPlantBlock(BlockBehaviour.Properties.copy(Blocks.KELP_PLANT)));
    public static final RegistryObject<Item> SUGAR_KELP_ITEM = ITEMS.register("sugar_kelp", () -> new ItemNameBlockItem(SUGAR_KELP.get(), new Item.Properties()));

    //Sea Witch's Blessing entry. An item that converts most of this mod's crops and certain items into their vanilla variants. Also helps you craft the modded items.
    //It will return itself after crafting in this mod. DO NOT ADD "remainItem" to any recipes, doing so will make it duplicate! Just add like a normal item!
    public static final RegistryObject<Item> SEA_WITCH_BLESSING = ITEMS.register("sea_witch_blessing", () -> new SeaWitchBlessingItem(new Item.Properties().rarity(Rarity.RARE)));

    //Nemo's Green Melon entry. This is the sea plant that grows regular melons!
    public static final RegistryObject<Block> NEMO_GREEN_MELON = BLOCKS.register("nemo_green_melon", () -> new NemoGreenMelonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.WET_GRASS).randomTicks().lightLevel(state -> 15)));
    public static final RegistryObject<Item> NEMO_GREEN_MELON_SEEDS = ITEMS.register("nemo_green_melon_seeds", () -> new ItemNameBlockItem(NEMO_GREEN_MELON.get(), new Item.Properties()));

    //Nemo's Orange Melon entry. This one grows pumpkins lol
    public static final RegistryObject<Block> NEMO_ORANGE_MELON = BLOCKS.register("nemo_orange_melon", () -> new NemoOrangeMelonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.WET_GRASS).randomTicks().lightLevel(state -> 15)));
    public static final RegistryObject<Item> NEMO_ORANGE_MELON_SEEDS = ITEMS.register("nemo_orange_melon_seeds", () -> new ItemNameBlockItem(NEMO_ORANGE_MELON.get(), new Item.Properties()));

    //Nemo's Carrot entry
    public static final RegistryObject<Block> NEMO_CARROT = BLOCKS.register("nemo_carrot", () -> new NemoCarrotBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.WET_GRASS).randomTicks().lightLevel(state -> 15)));
    public static final RegistryObject<Item> NEMO_CARROT_SPORE = ITEMS.register("nemo_carrot_spore", () -> new ItemNameBlockItem(NEMO_CARROT.get(), new Item.Properties()));

    //Nemo's Potato entry
    public static final RegistryObject<Block> NEMO_POTATO = BLOCKS.register("nemo_potato", () -> new NemoPotatoBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.WET_GRASS).randomTicks().lightLevel(state -> 15)));
    public static final RegistryObject<Item> NEMO_POTATO_SPORE = ITEMS.register("nemo_potato_spore", () -> new ItemNameBlockItem(NEMO_POTATO.get(), new Item.Properties()));

    //Nemo's Beetroot entry
    public static final RegistryObject<Block> NEMO_BEETROOT = BLOCKS.register("nemo_beetroot", () -> new NemoBeetrootBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.WET_GRASS).randomTicks().lightLevel(state -> 15)));
    public static final RegistryObject<Item> NEMO_BEETROOT_SPORE = ITEMS.register("nemo_beetroot_spore", () -> new ItemNameBlockItem(NEMO_BEETROOT.get(), new Item.Properties()));

    //Cactus Urchin Egg (item), the entity registries are in ModEntities.java
    public static final RegistryObject<Item> CACTUS_URCHIN_EGG = ITEMS.register("cactus_urchin_egg", () -> new CactusUrchinEggItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> CACTUS_URCHIN_UNI = ITEMS.register("cactus_urchin_uni", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.6f).build())));
    public static final RegistryObject<Item> COOKED_CACTUS_URCHIN_UNI = ITEMS.register("cooked_cactus_urchin_uni", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build())));
    public static final RegistryObject<Item> CACTUS_URCHIN_SHELL = ITEMS.register("cactus_urchin_shell", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CACTUS_URCHIN_SPAWN_EGG = ITEMS.register("cactus_urchin_spawn_egg", () -> new net.minecraftforge.common.ForgeSpawnEggItem(ModEntities.CACTUS_URCHIN, 0xFFE1C2, 0xE0822B, new Item.Properties()));

    //These are the "optional" Create compatibility processing items for the rest of the colors, should one have a fully underwater playthrough.
    public static final RegistryObject<Block> FINE_SILT = BLOCKS.register("fine_silt", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).strength(0.5f).sound(SoundType.SAND)));
    public static final RegistryObject<Item> FINE_SILT_ITEM = ITEMS.register("fine_silt", () -> new BlockItem(FINE_SILT.get(), new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_OCHRE = ITEMS.register("yellow_ochre", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Block> YELLOW_OCHRE_BLOCK = BLOCKS.register("yellow_ochre_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_YELLOW).strength(0.5f).sound(SoundType.SAND)));
    public static final RegistryObject<Item> YELLOW_OCHRE_BLOCK_ITEM = ITEMS.register("yellow_ochre_block", () -> new BlockItem(YELLOW_OCHRE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BROWN_OCHRE = ITEMS.register("brown_ochre", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Block> BROWN_OCHRE_BLOCK = BLOCKS.register("brown_ochre_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).strength(0.5f).sound(SoundType.SAND)));
    public static final RegistryObject<Item> BROWN_OCHRE_BLOCK_ITEM = ITEMS.register("brown_ochre_block", () -> new BlockItem(BROWN_OCHRE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> RED_OCHRE = ITEMS.register("red_ochre", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Block> RED_OCHRE_BLOCK = BLOCKS.register("red_ochre_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_RED).strength(0.5f).sound(SoundType.SAND)));
    public static final RegistryObject<Item> RED_OCHRE_BLOCK_ITEM = ITEMS.register("red_ochre_block", () -> new BlockItem(RED_OCHRE_BLOCK.get(), new Item.Properties()));

    //Mask's Sea Crops creative tab!
    public static final RegistryObject<CreativeModeTab> SEA_CROPS_TAB = CREATIVE_MODE_TABS.register("sea_crops_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("itemGroup.maskseacrops.sea_crops_tab"))
            .icon(() -> SILT_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(SILT_ITEM.get());
                output.accept(FINE_SILT_ITEM.get());
                output.accept(SEA_WHEAT_SEEDS.get());
                output.accept(SUGAR_KELP_ITEM.get());
                output.accept(SEA_WITCH_BLESSING.get());
                output.accept(NEMO_GREEN_MELON_SEEDS.get());
                output.accept(NEMO_ORANGE_MELON_SEEDS.get());
                output.accept(NEMO_CARROT_SPORE.get());
                output.accept(NEMO_POTATO_SPORE.get());
                output.accept(NEMO_BEETROOT_SPORE.get());
                output.accept(CACTUS_URCHIN_EGG.get());
                output.accept(CACTUS_URCHIN_UNI.get());
                output.accept(COOKED_CACTUS_URCHIN_UNI.get());
                output.accept(CACTUS_URCHIN_SHELL.get());
                output.accept(BROWN_OCHRE.get());
                output.accept(BROWN_OCHRE_BLOCK_ITEM.get());
                output.accept(RED_OCHRE.get());
                output.accept(RED_OCHRE_BLOCK_ITEM.get());
                output.accept(YELLOW_OCHRE.get());
                output.accept(YELLOW_OCHRE_BLOCK_ITEM.get());
                output.accept(CACTUS_URCHIN_SPAWN_EGG.get());
            }).build());

    //Setting up modded loot drops in global loot modifier
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

    //Registering Sugar Kelp to spawn organically in the world alongside the normal kelp
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SUGAR_KELP_FEATURE = FEATURES.register("sugar_kelp", () -> new SugarKelpFeature(NoneFeatureConfiguration.CODEC));

    //Making silt spawn organically
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SILT_FEATURE = FEATURES.register("silt", () -> new SiltFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SEAGRASS_DROPS_SEEDS =
            LOOT_MODIFIERS.register("seagrass_drops_seeds", SeagrassDropsSeeds.CODEC);

    public MaskSeaCrops()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        //Registering features so we can get the custom kelp
        FEATURES.register(modEventBus);

        //Registering entities
        ENTITIES.register(modEventBus);

        //Registering the Cactus Urchin entity
        ModEntities.ENTITIES.register(modEventBus);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Registering loot modifiers
        LOOT_MODIFIERS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

        //Making my modded items compostable
        event.enqueueWork(() -> {
            ComposterBlock.COMPOSTABLES.put(SEA_WHEAT_SEEDS.get(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(CACTUS_URCHIN_SHELL.get(), 0.5f);
            ComposterBlock.COMPOSTABLES.put(CACTUS_URCHIN_EGG.get(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(CACTUS_URCHIN_UNI.get(), 0.5f);
            ComposterBlock.COMPOSTABLES.put(NEMO_BEETROOT_SPORE.get(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(NEMO_CARROT_SPORE.get(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(NEMO_POTATO_SPORE.get(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(NEMO_GREEN_MELON_SEEDS.get(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(NEMO_ORANGE_MELON_SEEDS.get(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(SUGAR_KELP_ITEM.get(), 0.3f);
        });
    }

    // Add the example block item to the building blocks tab (Mask note: had to change this to silt because it kept throwing errors if I tried to delete any examples lol)
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(SILT_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        LOGGER.info("Cactus Urchin registry name: {}", ForgeRegistries.ENTITY_TYPES.getKey(ModEntities.CACTUS_URCHIN.get()));
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemBlockRenderTypes.setRenderLayer(MaskSeaCrops.SEA_WHEAT.get(), RenderType.cutout());

            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        //Cactus Urchin!
        @SubscribeEvent
        public static void onRegisterRenderers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.CACTUS_URCHIN.get(), CactusUrchinRenderer::new);
            event.registerEntityRenderer(ModEntities.CACTUS_URCHIN_EGG_ENTITY.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterLayers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(CactusUrchinModel.LAYER_LOCATION, CactusUrchinModel::createBodyLayer);
        }

    }
    //Seacrops bonemealing events
    @SubscribeEvent
    public void onBonemeal(net.minecraftforge.event.entity.player.BonemealEvent event) {
        if (event.getBlock().getBlock() == SEA_WHEAT.get()) {
            BlockState state = event.getBlock();
            int age = state.getValue(SeaWheatBlock.AGE);
            if (age < SeaWheatBlock.MAX_AGE) {
                int growth = event.getLevel().getRandom().nextInt(4) + 1;
                int newAge = Math.min(age + growth, SeaWheatBlock.MAX_AGE);
                event.getLevel().setBlock(event.getPos(), state.setValue(SeaWheatBlock.AGE, newAge), 2);
                event.getLevel().levelEvent(2005, event.getPos(), 0);
                event.getEntity().swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            }
        } else if (event.getBlock().getBlock() == SUGAR_KELP.get()) {
            if (event.getLevel().isClientSide()) return;
            BlockPos pos = event.getPos();
            BlockPos above = pos.above();
            BlockPos blockAboveWater = above.above();
            FluidState aboveFluid = event.getLevel().getFluidState(above);
            FluidState blockAboveWaterFluid = event.getLevel().getFluidState(blockAboveWater);
            if (aboveFluid.is(Fluids.WATER)){
                ServerLevel level = (ServerLevel) event.getLevel();
                level.setBlock(pos, SUGAR_KELP_PLANT.get().defaultBlockState(), 3);
                level.setBlock(above, SUGAR_KELP.get().defaultBlockState(), 3);
                event.getLevel().levelEvent(2005, pos, 0);
                event.getEntity().swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            } else {
                event.setCanceled(true);
            }
        } else if (event.getBlock().getBlock() == NEMO_GREEN_MELON.get() || event.getBlock().getBlock() == NEMO_ORANGE_MELON.get()) {
            BlockState state = event.getBlock();
            int age = state.getValue(NemoVineBlock.AGE);
            if (age < NemoVineBlock.MAX_AGE) {
                int growth = event.getLevel().getRandom().nextInt(4) + 1;
                int newAge = Math.min(age + growth, NemoVineBlock.MAX_AGE);
                event.getLevel().setBlock(event.getPos(), state.setValue(NemoVineBlock.AGE, newAge), 2);
                event.getLevel().levelEvent(2005, event.getPos(), 0);
                event.getEntity().swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            }
        } else if (event.getBlock().getBlock() == NEMO_BEETROOT.get()) {
            BlockState state = event.getBlock();
            int age = state.getValue(NemoBeetrootBlock.AGE);
            if (age < NemoBeetrootBlock.MAX_AGE) {
                int growth = event.getLevel().getRandom().nextInt(4) +1;
                int newAge = Math.min(age + growth, NemoBeetrootBlock.MAX_AGE);
                event.getLevel().setBlock(event.getPos(), state.setValue(NemoBeetrootBlock.AGE, newAge), 2);
                event.getLevel().levelEvent(2005, event.getPos(), 0);
                event.getEntity().swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            }
        } else if (event.getBlock().getBlock() == NEMO_CARROT.get()) {
            BlockState state = event.getBlock();
            int age = state.getValue(NemoCarrotBlock.AGE);
            if (age < NemoCarrotBlock.MAX_AGE) {
                int growth = event.getLevel().getRandom().nextInt(4) +1;
                int newAge = Math.min(age + growth, NemoCarrotBlock.MAX_AGE);
                event.getLevel().setBlock(event.getPos(), state.setValue(NemoCarrotBlock.AGE, newAge), 2);
                event.getLevel().levelEvent(2005, event.getPos(), 0);
                event.getEntity().swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            }
        } else if (event.getBlock().getBlock() == NEMO_POTATO.get()) {
            BlockState state = event.getBlock();
            int age = state.getValue(NemoPotatoBlock.AGE);
            if (age < NemoPotatoBlock.MAX_AGE) {
                int growth = event.getLevel().getRandom().nextInt(4) + 1;
                int newAge = Math.min(age + growth, NemoPotatoBlock.MAX_AGE);
                event.getLevel().setBlock(event.getPos(), state.setValue(NemoPotatoBlock.AGE, newAge), 2);
                event.getLevel().levelEvent(2005, event.getPos(), 0);
                event.getEntity().swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            }
        }
    }

    //Fixing smth with the Cactus Urchin attributes because it is causing some warnings in the log
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onEntityAttributeCreation(net.minecraftforge.event.entity.EntityAttributeCreationEvent event) {
            event.put(ModEntities.CACTUS_URCHIN.get(), CactusUrchinEntity.createAttributes().build());
        }
    }
}