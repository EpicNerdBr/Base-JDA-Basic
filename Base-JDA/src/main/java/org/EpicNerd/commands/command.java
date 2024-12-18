package org.EpicNerd.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface command {
    String getName();
    String[] getAliases();
    void execute(MessageReceivedEvent event);
}
