package org.EpicNerd;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import org.EpicNerd.events.BotJoinListener; // Events
import org.EpicNerd.events.MyEventListener; // Events
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("TOKEN");

        JDABuilder builder = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .disableCache(CacheFlag.ACTIVITY)
                .setActivity(Activity.playing("estou aqui!"));

        commandManager commandManager = new commandManager(); // Adicionando CommandManager

        builder.addEventListeners(new BotJoinListener(), new MyEventListener(), new ListenerAdapter() {
            @Override
            public void onMessageReceived(MessageReceivedEvent event) {
                if (event.getAuthor().isBot()) return;

                String message = event.getMessage().getContentRaw();
                commandManager.handleCommand(event);  // Delegar para o commandManager
            }
        });

        builder.build();
    }
}
