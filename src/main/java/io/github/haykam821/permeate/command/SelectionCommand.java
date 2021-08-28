package io.github.haykam821.permeate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.haykam821.permeate.editor.Editor;
import io.github.haykam821.permeate.editor.selection.CuboidSelection;
import io.github.haykam821.permeate.editor.selection.SelectionUpdater;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public final class SelectionCommand {
	public SelectionCommand() {
		return;
	}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("selection")
			.then(CommandManager.literal("cuboid")
				.then(CommandManager.argument("start", BlockPosArgumentType.blockPos())
					.then(CommandManager.argument("end", BlockPosArgumentType.blockPos())
						.executes(SelectionCommand::executeCuboid))))
			.then(CommandManager.literal("start")
				.executes(SelectionCommand::executeStartHere)
				.then(CommandManager.argument("start", BlockPosArgumentType.blockPos())
					.executes(SelectionCommand::executeStartAt)))
			.then(CommandManager.literal("end")
				.executes(SelectionCommand::executeEndHere)
				.then(CommandManager.argument("end", BlockPosArgumentType.blockPos())
					.executes(SelectionCommand::executeEndAt)))
			.then(CommandManager.literal("offset")
				.then(CommandManager.argument("direction", StringArgumentType.string())
					.then(CommandManager.argument("distance", IntegerArgumentType.integer())
							.executes(SelectionCommand::executeOffset))))
			.then(CommandManager.literal("clear")
				.executes(SelectionCommand::executeClear)));
	}

	private static int executeCuboid(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return SelectionCommand.setSelection(context, editorState -> {
			BlockPos start = BlockPosArgumentType.getBlockPos(context, "start");
			BlockPos end = BlockPosArgumentType.getBlockPos(context, "end");

			return new CuboidSelection(start, end);
		});
	}

	private static int executeStartHere(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos start = new BlockPos(context.getSource().getPosition());
		return SelectionCommand.setStart(context, start);
	}

	private static int executeStartAt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos start = BlockPosArgumentType.getBlockPos(context, "start");
		return SelectionCommand.setStart(context, start);
	}

	private static int executeEndHere(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos end = new BlockPos(context.getSource().getPosition());
		return SelectionCommand.setEnd(context, end);
	}

	private static int executeEndAt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos end = BlockPosArgumentType.getBlockPos(context, "end");
		return SelectionCommand.setEnd(context, end);
	}

	private static int executeOffset(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return SelectionCommand.setSelection(context, editorState -> {
			Direction direction = Direction.byName(StringArgumentType.getString(context, "direction"));

			int distance = IntegerArgumentType.getInteger(context, "distance");

			return editorState.getSelection().offset(direction, distance);
		});
	}

	private static int executeClear(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return SelectionCommand.setSelection(context, editorState -> {
			return null;
		});
	}

	private static int setStart(CommandContext<ServerCommandSource> context, BlockPos start) throws CommandSyntaxException {
		return SelectionCommand.setSelection(context, editorState -> {
			if (editorState.getSelectionNullable() instanceof CuboidSelection) {
				CuboidSelection cuboid = (CuboidSelection) editorState.getSelectionNullable();
				return new CuboidSelection(start, cuboid.getEnd());
			} else {
				return new CuboidSelection(start, start);
			}
		});
	}

	private static int setEnd(CommandContext<ServerCommandSource> context, BlockPos end) throws CommandSyntaxException {
		return SelectionCommand.setSelection(context, editorState -> {
			if (editorState.getSelectionNullable() instanceof CuboidSelection) {
				CuboidSelection cuboid = (CuboidSelection) editorState.getSelectionNullable();
				return new CuboidSelection(cuboid.getStart(), end);
			} else {
				return new CuboidSelection(end, end);
			}
		});
	}

	private static int setSelection(CommandContext<ServerCommandSource> context, SelectionUpdater updater) throws CommandSyntaxException {
		Editor editor = Editor.fromCommandSource(context.getSource());
		if (editor.getEditorState().updateSelection(updater) == null) {
			context.getSource().sendFeedback(new TranslatableText("text.permeate.selection.clear.success"), false);
			return 0;
		}

		int selected = editor.getEditorState().getSelectionNullable().getVolume();

		Text feedback;
		if (selected == 1) {
			feedback = new TranslatableText("text.permeate.selection.success.singular");
		} else {
			feedback = new TranslatableText("text.permeate.selection.success", selected);
		}

		context.getSource().sendFeedback(feedback, false);
		return selected;
	}
}
