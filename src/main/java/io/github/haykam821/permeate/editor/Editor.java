package io.github.haykam821.permeate.editor;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;

public interface Editor {
	public EditorState getEditorState();

	public static Editor fromCommandSource(ServerCommandSource source) throws CommandSyntaxException {
		if (source.getPlayer() instanceof Editor) {
			return (Editor) source.getPlayer();
		}
		throw EditorState.NOT_EDITOR_EXCEPTION.create();
	}
}
