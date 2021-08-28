package io.github.haykam821.permeate;

import io.github.haykam821.permeate.command.SelectionCommand;
import io.github.haykam821.permeate.command.SetCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			SelectionCommand.register(dispatcher);
			SetCommand.register(dispatcher);
		});
	}
}
