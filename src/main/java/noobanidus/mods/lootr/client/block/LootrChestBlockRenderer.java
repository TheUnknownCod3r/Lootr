package noobanidus.mods.lootr.client.block;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.block.entities.LootrChestBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.util.Getter;

import java.util.UUID;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class LootrChestBlockRenderer<T extends LootrChestBlockEntity & ILootBlockEntity> extends ChestRenderer<T> {
  private UUID playerId = null;
  public static final Material MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(LootrAPI.MODID, "chest"));
  public static final Material MATERIAL2 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(LootrAPI.MODID, "chest_opened"));
  public static final Material MATERIAL3 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(LootrAPI.MODID, "trapped_chest"));
  public static final Material MATERIAL4 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(LootrAPI.MODID, "trapped_chest_opened"));

  public LootrChestBlockRenderer(BlockEntityRendererProvider.Context p_173607_) {
    super(p_173607_);
  }

  @Override
  protected Material getMaterial(T tile, ChestType type) {
    boolean trapped = tile.getType() == ModBlockEntities.SPECIAL_TRAPPED_LOOT_CHEST;
    if (ConfigManager.isVanillaTextures()) {
      return trapped ? Sheets.CHEST_TRAP_LOCATION : Sheets.CHEST_LOCATION;
    }
    if (playerId == null) {
      Player player = Getter.getPlayer();
      if (player != null) {
        playerId = player.getUUID();
      } else {
        return trapped ? MATERIAL3 : MATERIAL;
      }
    }
    if (tile.isOpened()) {
      return trapped ? MATERIAL4 : MATERIAL2;
    }
    if (tile.getOpeners().contains(playerId)) {
      return trapped ? MATERIAL4 : MATERIAL2;
    } else {
      return trapped ? MATERIAL3 : MATERIAL;
    }
  }
}
