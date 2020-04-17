package devtenko.mhaminecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class scoreboard_manager {
    public void erasure_scoreboard(Player p, int quirk_return){
        Scoreboard quirk_scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective return_quirk_score=quirk_scoreboard.registerNewObjective("test","dummy", ChatColor.RED +"Quirk Return Time");
        return_quirk_score.setDisplaySlot(DisplaySlot.SIDEBAR);
        String time_left = String.valueOf(quirk_return);
        Score score = return_quirk_score.getScore(ChatColor.RED + time_left);
        score.setScore(1);
        p.setScoreboard(quirk_scoreboard);
    }

    public void defaul_quirk(Player p, int time_till_next_quirk){
        String time_left = String.valueOf(time_till_next_quirk);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective =scoreboard.registerNewObjective("test", "dummy", ChatColor.RED + "Time Till Quirk");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore(ChatColor.RED + "Time Till Quirk: " + time_left);
        score.setScore(1);
        p.setScoreboard(scoreboard);
    }

}
