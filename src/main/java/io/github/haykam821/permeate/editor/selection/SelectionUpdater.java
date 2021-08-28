package io.github.haykam821.permeate.editor.selection;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.haykam821.permeate.editor.EditorState;

@FunctionalInterface
public interface SelectionUpdater {
	public Selection update(EditorState editorState) throws CommandSyntaxException;
}
