package io.github.javtor.javtormod.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class DowsingRodItem extends Item {

    public DowsingRodItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(!context.getWorld().isClient()) {
            BlockPos clickedBlockPos = context.getBlockPos();
            PlayerEntity player = context.getPlayer();
            boolean foundValuableBlock = false;
            for(int i = 0; i <= clickedBlockPos.getY() + 64; i++) {
                Block blockBelow = context.getWorld().getBlockState(clickedBlockPos.down(i)).getBlock();

                if(isValuableBlock(blockBelow)) {
                    outputValuableCoordinates(clickedBlockPos.down(i), player, blockBelow);
                    foundValuableBlock = true;
                    break;
                }
            }

            if(!foundValuableBlock) {
                player.sendMessage(new LiteralText("No valuable block found"), false);
            }

            context.getStack().damage(1, context.getPlayer(),
                    (playerEntity) -> playerEntity.sendToolBreakStatus(playerEntity.getActiveHand()));
        }
        return super.useOnBlock(context);
    }

    private void outputValuableCoordinates(BlockPos blockPos, PlayerEntity player, Block blockBelow) {
        player.sendMessage(new LiteralText("Found " + blockBelow.asItem().getName().getString() + " at " +
                "(" + blockPos.getX() + ", " + blockPos.getY() + "," + blockPos.getZ() + ")"), false);
    }

    private boolean isValuableBlock(Block block) {
        return block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE
                || block == Blocks.DIAMOND_ORE || block == Blocks.IRON_ORE;
    }
}
