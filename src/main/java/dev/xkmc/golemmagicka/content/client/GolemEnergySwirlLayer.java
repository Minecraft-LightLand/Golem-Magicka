package dev.xkmc.golemmagicka.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.golemmagicka.init.GolemMagicka;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Predicate;

public class GolemEnergySwirlLayer<
		T extends AbstractGolemEntity<T, P>,
		P extends IGolemPart<P>,
		M extends EntityModel<T> & IGolemModel<T, P, M>
		> extends RenderLayer<T, M> {

	public static final ModelLayerLocation LARGE = new ModelLayerLocation(GolemMagicka.loc("energy_layer"), "large");
	public static final ModelLayerLocation HUMANOID = new ModelLayerLocation(GolemMagicka.loc("energy_layer"), "humanoid");
	public static final ModelLayerLocation DOG = new ModelLayerLocation(GolemMagicka.loc("energy_layer"), "dog");

	private static final int COLOR = RenderHelper.colorf(0.8F, 0.8F, 0.8F);

	private static RenderType getRenderType(ResourceLocation texture, float f) {
		return RenderType.energySwirl(texture, f * 0.02F % 1.0F, f * 0.01F % 1.0F);
	}

	private final M model;
	private final ResourceLocation TEXTURE;
	private final Predicate<T> shouldRender;

	public GolemEnergySwirlLayer(RenderLayerParent<T, M> pRenderer, M model, ResourceLocation texture, Predicate<T> shouldRender) {
		super(pRenderer);
		this.model = model;
		this.TEXTURE = texture;
		this.shouldRender = shouldRender;
	}

	public GolemEnergySwirlLayer(RenderLayerParent<T, M> pRenderer, M model, ResourceLocation texture, Holder<MobEffect> shouldRenderFlag) {
		this(pRenderer, model, texture, (living) -> living.hasEffect(shouldRenderFlag));
	}

	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if (shouldRender.test(pLivingEntity)) {
			float f = (float) pLivingEntity.tickCount + pPartialTicks;
			M m = this.model();
			this.getParentModel().copyPropertiesTo(m);
			m.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks);
			m.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
			VertexConsumer vertexconsumer = pBuffer.getBuffer(getRenderType(this.TEXTURE, f));
			m.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, COLOR);
		}

	}

	protected M model() {
		return this.model;
	}

}