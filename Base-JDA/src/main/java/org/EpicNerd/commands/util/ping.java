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
        return new String[] {"pong", "pinga"};
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Pong! kkk").queue();
    }
}
