package com.example.maskseacrops.entity.client;

import com.example.maskseacrops.entity.custom.CactusUrchinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CactusUrchinRenderer extends MobRenderer<CactusUrchinEntity, CactusUrchinModel<CactusUrchinEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("maskseacrops", "textures/entity/cactus_urchin_texture.png");

    public CactusUrchinRenderer(EntityRendererProvider.Context context) {
        super(context, new CactusUrchinModel<>(context.bakeLayer(CactusUrchinModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(CactusUrchinEntity entity) {
        return TEXTURE;
    }

}