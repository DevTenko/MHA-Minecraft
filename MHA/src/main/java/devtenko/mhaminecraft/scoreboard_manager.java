package devtenko.mhaminecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class scoreboard_manager {

    public void default_quirk(Player p, int time_till_next_quirk){
        String time_left = String.valueOf(time_till_next_quirk);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective =scoreboard.registerNewObjective("test", "dummy", ChatColor.RED + "Time Till Quirk");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore(ChatColor.RED + "Time Till Quirk: " + time_left);
        score.setScore(1);
        p.setScoreboard(scoreboard);
    }
}
