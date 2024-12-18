package org.EpicNerd.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MyEventListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentRaw();
        if (message.equalsIgnoreCase("!ping")) {
            long gatewayPing = event.getJDA().getGatewayPing();
            long time = System.currentTimeMillis();
            event.getChannel().sendMessage(
                    ":ping_pong: **|** **Pong!**\n" +
                            ":stopwatch: **|** **Gateway Ping:** `" + gatewayPing + "ms`\n" +
                            ":zap: **|** **API Ping:** `Calculando...`"
            ).queue(response -> {
                long apiPing = System.currentTimeMillis() - time;
                response.editMessage(
                        ":ping_pong: **|** **Pong!**\n" +
                                ":stopwatch: **|** **Gateway Ping:** `" + gatewayPing + "ms`\n" +
                                ":zap: **|** **API Ping:** `" + apiPing + "ms`"
                ).queue();
            });
        }
    }
}
