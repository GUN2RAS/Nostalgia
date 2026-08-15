package net.nostalgia.client.model;

import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class AlphaCowModel extends CowModel {

  public AlphaCowModel(ModelPart root) {
    super(root);
  }

  public static ModelPart bakeModelPart() {
    return createBodyLayer().bakeRoot();
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();

    root.addOrReplaceChild(
      "head",
      CubeListBuilder.create()
        .texOffs(0, 0)
        .addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
        .texOffs(22, 0)
        .addBox(-5.2F, -6.0F, -4.0F, 1.0F, 3.0F, 1.0F)
        .texOffs(22, 0)
        .addBox(4.2F, -6.0F, -4.0F, 1.0F, 3.0F, 1.0F),
      PartPose.offset(0.0F, 4.0F, -8.0F)
    );

    root.addOrReplaceChild(
      "body",
      CubeListBuilder.create()
        .texOffs(18, 4)
        .addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F),
      PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F)
    );

    root.addOrReplaceChild(
      "udder",
      CubeListBuilder.create()
        .texOffs(52, 0)
        .addBox(-2.0F, -3.0F, 0.0F, 4.0F, 6.0F, 2.0F),
      PartPose.offsetAndRotation(0.0F, 14.0F, 6.0F, 1.5707964F, 0.0F, 0.0F)
    );

    CubeListBuilder leftLeg = CubeListBuilder.create().mirror().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
    CubeListBuilder rightLeg = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
    root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-4.0F, 12.0F, 7.0F));
    root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(4.0F, 12.0F, 7.0F));
    root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-4.0F, 12.0F, -6.0F));
    root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(4.0F, 12.0F, -6.0F));

    return LayerDefinition.create(mesh, 64, 32);
  }
}
