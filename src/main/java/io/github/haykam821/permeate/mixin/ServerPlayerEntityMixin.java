package io.github.haykam821.permeate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.haykam821.permeate.editor.Editor;
import io.github.haykam821.permeate.editor.EditorState;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements Editor {
	@Unique
	private final EditorState editorState = new EditorState();

	@Override
	public EditorState getEditorState() {
		return this.editorState;
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void copyEditorStateFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		Editor oldEditor = (Editor) oldPlayer;
		this.editorState.copyFrom(oldEditor.getEditorState());
	}
}
