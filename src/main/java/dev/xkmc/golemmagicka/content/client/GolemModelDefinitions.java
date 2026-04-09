package dev.xkmc.golemmagicka.content.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GolemModelDefinitions {

	public static MeshDefinition buildGolemBaseLayers(CubeDeformation def) {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition partdefinition = mesh.getRoot();
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, def), PartPose.offset(0.0F, -7.0F, -2.0F));
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F, def).texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -7.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 14.0F, 6.0F, def), PartPose.offset(0.0F, -7.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(60, 58).mirror().addBox(9.0F, -2.5F, -3.0F, 4.0F, 14.0F, 6.0F, def), PartPose.offset(0.0F, -7.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, def), PartPose.offset(-4.0F, 11.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, def), PartPose.offset(5.0F, 11.0F, 0.0F));
		PartDefinition root1 = mesh.getRoot().getChild("right_arm");
		root1.addOrReplaceChild("right_forearm", CubeListBuilder.create().texOffs(60, 35).addBox(-12.99F, 0.0F, -6.0F, 4.0F, 16.0F, 6.0F, def), PartPose.offsetAndRotation(0.0F, 11.5F, 3.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition root2 = mesh.getRoot().getChild("left_arm");
		root2.addOrReplaceChild("left_forearm", CubeListBuilder.create().texOffs(60, 72).mirror().addBox(8.99F, 0.0F, -6.0F, 4.0F, 16.0F, 6.0F, def), PartPose.offsetAndRotation(0.0F, 11.5F, 3.0F, 0.0F, 0.0F, 0.0F));
		return mesh;
	}

	public static LayerDefinition createGolemLayer(CubeDeformation def) {
		MeshDefinition mesh = buildGolemBaseLayers(def);
		return LayerDefinition.create(mesh, 64, 64);
	}

	public static MeshDefinition createPlayerMesh(CubeDeformation def, boolean p_170827_) {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(def, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, def), PartPose.ZERO);
		partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, def, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));
		float f = 0.25F;
		if (p_170827_) {
			partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, def), PartPose.offset(5.0F, 2.5F, 0.0F));
			partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, def), PartPose.offset(-5.0F, 2.5F, 0.0F));
			partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, def.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
			partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, def.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));
		} else {
			partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, def), PartPose.offset(5.0F, 2.0F, 0.0F));
			partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, def.extend(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
			partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, def.extend(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		}

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, def), PartPose.offset(1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, def.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, def.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, def.extend(0.25F)), PartPose.ZERO);
		return meshdefinition;
	}

	public static LayerDefinition createHumanoidLayer(CubeDeformation def) {
		return LayerDefinition.create(createPlayerMesh(def, false), 64, 64);
	}

	public static LayerDefinition createDogLayer(CubeDeformation def) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		float f = 13.5F;
		PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.0F, 13.5F, -7.0F));
		partdefinition1.addOrReplaceChild("real_head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, def).texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, def).texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, def).texOffs(0, 10).addBox(-0.5F, -0.001F, -5.0F, 3.0F, 3.0F, 4.0F, def), PartPose.ZERO);
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, def), PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, def), PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
		CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, def);
		partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-2.5F, 16.0F, 7.0F));
		partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder.mirror(), PartPose.offset(0.5F, 16.0F, 7.0F));
		partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder, PartPose.offset(-2.5F, 16.0F, -4.0F));
		partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder.mirror(), PartPose.offset(0.5F, 16.0F, -4.0F));
		PartDefinition partdefinition2 = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, ((float)Math.PI / 5F), 0.0F, 0.0F));
		partdefinition2.addOrReplaceChild("real_tail", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, def), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

}
