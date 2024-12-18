package org.EpicNerd.commands.util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.EpicNerd.commands.command;

public class ping implements command {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"pong"};
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        long gatewayPing = event.getJDA().getGatewayPing();
        long apiPing = event.getJDA().getRestPing().complete();

        String message = ":ping_pong: **|** **Pong!**\n" +
                         ":stopwatch: **|** **Gateway Ping:** `" + gatewayPing + "ms`\n" +
                         ":zap: **|** **API Ping:** `" + apiPing + "ms`";

        event.getMessage().reply(message).queue();
    }
}
