package io.github.haykam821.permeate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.haykam821.permeate.editor.Editor;
import io.github.haykam821.permeate.editor.selection.Selection;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public final class SetCommand {
	public SetCommand() {
		return;
	}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("set")
			.then(CommandManager.argument("state", BlockStateArgumentType.blockState())
				.executes(SetCommand::execute)));
	}

	private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();

		Editor editor = Editor.fromCommandSource(source);
		Selection selection = editor.getEditorState().getSelection();

		BlockStateArgument argument = BlockStateArgumentType.getBlockState(context, "state");
		BlockState state = argument.getBlockState();

		int changes = 0;
		for (BlockPos pos : selection.iterate()) {
			source.getWorld().setBlockState(pos, state);
			changes += 1;
		}

		Text feedback;
		if (changes == 1) {
			feedback = new TranslatableText("text.permeate.set.success.singular");
		} else {
			feedback = new TranslatableText("text.permeate.set.success", changes);
		}

		source.sendFeedback(feedback, false);
		return changes;
	}
}
