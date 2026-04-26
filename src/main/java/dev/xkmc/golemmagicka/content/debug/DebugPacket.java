package dev.xkmc.golemmagicka.content.debug;

import dev.xkmc.golemmagicka.content.entity.GolemWizardGoal;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public record DebugPacket(int golem, ArrayList<String> list) implements SerialPacketBase<DebugPacket> {

	public static void fill(SweepGolemEntity<?, ?> golem, List<String> list) {
		list.add("DEBUG INFO START");
		var target = golem.getTarget();
		list.add("HasTarget: " + (target != null && target.isAlive()));
		list.add("Running Goals: ");
		boolean meleeFilled = false;
		boolean magicFilled = false;
		for (var wrapped : golem.goalSelector.getAvailableGoals()) {
			if (wrapped.isRunning()) {
				var goal = wrapped.getGoal();
				String clz = goal.getClass().getSimpleName();
				if (goal instanceof GolemMeleeGoal melee) {
					meleeFilled = true;
				} else if (goal instanceof GolemWizardGoal<?> magic) {
					magicFilled = true;
				}
				list.add("- " + clz);
			}
		}
		list.add("Pending Goals: ");
		for (var wrapped : golem.goalSelector.getAvailableGoals()) {
			var goal = wrapped.getGoal();
			String clz = goal.getClass().getSimpleName();
			if (goal instanceof GolemMeleeGoal melee) {
				if (!meleeFilled) {
					list.add("- " + clz);
					list.add("| - CanUse = " + melee.canUse());
				}
			} else if (goal instanceof GolemWizardGoal<?> magic) {
				if (!magicFilled) {
					list.add("- " + clz);
					list.add("| - CanUse = " + magic.canUse());
				}
			}
		}
		list.add("DEBUG INFO END");
	}

	@Override
	public void handle(Player player) {
		ClientDebugInfo.handle(golem, list);
	}

}
