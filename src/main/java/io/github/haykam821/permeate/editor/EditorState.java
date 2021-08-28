package io.github.haykam821.permeate.editor;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.haykam821.permeate.editor.selection.Selection;
import io.github.haykam821.permeate.editor.selection.SelectionUpdater;
import net.minecraft.text.TranslatableText;

public class EditorState {
	protected static final SimpleCommandExceptionType NOT_EDITOR_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("text.permeate.editor_state.not_editor"));
	private static final SimpleCommandExceptionType NO_SELECTION_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("text.permeate.editor_state.no_selection"));

	private Selection selection;

	public Selection getSelection() throws CommandSyntaxException {
		if (this.selection == null) {
			throw NO_SELECTION_EXCEPTION.create();
		}
		return this.selection;
	}

	public Selection getSelectionNullable() {
		return this.selection;
	}

	public Selection updateSelection(SelectionUpdater updater) throws CommandSyntaxException {
		return this.selection = updater.update(this);
	}

	public void copyFrom(EditorState editorState) {
		this.selection = editorState.selection;
	}

	@Override
	public String toString() {
		return "EditorState{selection=" + this.selection + "}";
	}
}
