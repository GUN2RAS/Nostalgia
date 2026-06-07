package net.nostalgia.client.render.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.BoatRenderState;

public class AlphaBoatModel extends EntityModel<BoatRenderState> {
    private final ModelPart root;

    public AlphaBoatModel(ModelPart root) {
        super(root);
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        
        
        
        
        
        partdefinition.addOrReplaceChild("bottom", 
            CubeListBuilder.create().texOffs(0, 8).addBox(-12.0F, -8.0F, -3.0F, 24.0F, 16.0F, 4.0F), 
            PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("front", 
            CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -7.0F, -1.0F, 20.0F, 6.0F, 2.0F), 
            PartPose.offsetAndRotation(-11.0F, 4.0F, 0.0F, 0.0F, 4.712389F, 0.0F));

        partdefinition.addOrReplaceChild("back", 
            CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -7.0F, -1.0F, 20.0F, 6.0F, 2.0F), 
            PartPose.offsetAndRotation(11.0F, 4.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));

        partdefinition.addOrReplaceChild("left", 
            CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -7.0F, -1.0F, 20.0F, 6.0F, 2.0F), 
            PartPose.offsetAndRotation(0.0F, 4.0F, -9.0F, 0.0F, 3.1415927F, 0.0F));

        partdefinition.addOrReplaceChild("right", 
            CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -7.0F, -1.0F, 20.0F, 6.0F, 2.0F), 
            PartPose.offsetAndRotation(0.0F, 4.0F, 9.0F, 0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(BoatRenderState state) {
        super.setupAnim(state);
    }
}
