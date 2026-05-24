package com.example.maskseacrops.entity.client;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.example.maskseacrops.entity.custom.CactusUrchinEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CactusUrchinModel<T extends CactusUrchinEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("maskseacrops", "cactusurchin"), "main");
	private final ModelPart main;
	private final ModelPart spines;
	private final ModelPart detailsides;

	@Override
	public ModelPart root() {
		return this.main;
	}

	public CactusUrchinModel(ModelPart root) {
		this.main = root.getChild("main");
		this.spines = this.main.getChild("spines");
		this.detailsides = this.main.getChild("detailsides");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition spines = main.addOrReplaceChild("spines", CubeListBuilder.create().texOffs(0, 23).addBox(-5.0F, -4.5F, 0.0F, 10.0F, 9.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(0.0F, -4.5F, -5.0F, 0.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 0.0F));

		PartDefinition spines_b_r1 = spines.addOrReplaceChild("spines_b_r1", CubeListBuilder.create().texOffs(0, 13).addBox(0.0F, -4.5F, -5.0F, 0.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition spines_a_r1 = spines.addOrReplaceChild("spines_a_r1", CubeListBuilder.create().texOffs(0, 13).addBox(0.0F, -4.5F, -5.0F, 0.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition detailsides = main.addOrReplaceChild("detailsides", CubeListBuilder.create().texOffs(12, 10).addBox(-1.0F, -1.7F, -3.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(-1.0F, -1.7F, 2.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(2.0F, -1.7F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 10).addBox(-3.0F, -1.7F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 6).addBox(-1.0F, -3.7F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.3F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T t, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//This is for the idle, it is the gentle pulsing of the spines to simulate it being a living creature.
		float idlePulse = (float) Math.sin(ageInTicks * 0.05f) * 0.01f;
		this.main.xScale = 1.0f + idlePulse;
		this.main.yScale = 1.0f + idlePulse;
		this.main.zScale = 1.0f + idlePulse;

		this.spines.yRot = (float) Math.sin(ageInTicks * 0.1f) * 0.2f * (t.isMoving() ? 1.0f : 0.0f);
	}



	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}