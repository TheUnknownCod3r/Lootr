package noobanidus.mods.lootr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.data.ModelProperty;
import noobanidus.mods.lootr.block.entities.LootrBarrelBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.util.ChestUtil;

import javax.annotation.Nullable;
import java.util.Random;

public class LootrBarrelBlock extends BarrelBlock {
  public static final ModelProperty<Boolean> OPENED = new ModelProperty<>();

  public LootrBarrelBlock(Properties p_49046_) {
    super(p_49046_);
  }

  @Override
  public float getExplosionResistance() {
    if (ConfigManager.BLAST_RESISTANT.get()) {
      return 16.0f;
    } else {
      return super.getExplosionResistance();
    }
  }

  @Override
  public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
    if (!pState.is(pNewState.getBlock())) {
      BlockEntity blockentity = pLevel.getBlockEntity(pPos);
      if (blockentity instanceof Container) {
        pLevel.updateNeighbourForOutputSignal(pPos, this);
      }

      if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity())) {
        pLevel.removeBlockEntity(pPos);
      }
    }
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
    if (player.isShiftKeyDown()) {
      ChestUtil.handleLootSneak(this, world, pos, player);
    } else {
      ChestUtil.handleLootChest(this, world, pos, player);
    }
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new LootrBarrelBlockEntity(pos, state);
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
    super.triggerEvent(state, world, pos, id, param);
    BlockEntity tile = world.getBlockEntity(pos);
    return tile != null && tile.triggerEvent(id, param);
  }

  @Override
  public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    BlockEntity blockentity = pLevel.getBlockEntity(pPos);
    if (blockentity instanceof LootrBarrelBlockEntity) {
      ((LootrBarrelBlockEntity) blockentity).recheckOpen();
    }
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState pState) {
    return false;
  }

  @Override
  public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
    if (ConfigManager.ZERO_COMPARATOR.get()) {
      return 0;
    } else {
      return 1;
    }
  }
}