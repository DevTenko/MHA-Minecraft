package devtenko.mhaminecraft;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.util.Objects;

public class discord_handler {
    JDA jda_object;
    Long text_channel_id;
    Guild guild;
    Long guild_id;
    public discord_handler(String token, Long guild_id, Long text_channel_id) throws LoginException, InterruptedException {
        if(token == null || token == "") return;
        this.guild_id = guild_id;
        this.text_channel_id = text_channel_id;
        this.jda_object = new JDABuilder(AccountType.BOT).setToken(token).build();
        this.jda_object.getPresence().setStatus(OnlineStatus.ONLINE);
        this.guild = jda_object.getGuildById(guild_id);
        this.jda_object.awaitReady();
        }

    public void send_message(String text) {
        jda_object.getTextChannelById(text_channel_id).sendMessage(text).queue();
    }
}
