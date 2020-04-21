package devtenko.mhaminecraft;

import net.dv8tion.jda.api.*;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class discord_handler {
    JDA jda_object;
    Long text_channel_id;
    public discord_handler(String token, Long text_channel_id) throws LoginException, InterruptedException {
        if(token == null || token == "") return;
        this.text_channel_id = text_channel_id;
        this.jda_object = new JDABuilder(AccountType.BOT).setToken(token).build();
        this.jda_object.getPresence().setStatus(OnlineStatus.ONLINE);
        this.jda_object.awaitReady();
        }

    public void send_message(String text) {
        jda_object.getTextChannelById(text_channel_id).sendMessage(text).queue();
    }
}
