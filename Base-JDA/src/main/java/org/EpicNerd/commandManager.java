package org.EpicNerd;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.EpicNerd.commands.command;
import org.EpicNerd.commands.util.ping;

import java.util.HashMap;
import java.util.Map;

public class commandManager {
    private final Map<String, command> commands = new HashMap<>();
    private final String prefix;

    public commandManager() {
        Dotenv dotenv = Dotenv.load();
        this.prefix = dotenv.get("PREFIX");

        if (this.prefix == null) {
            throw new IllegalStateException("PREFIX não definido no arquivo .env");
        }

        addCommand(new ping());
        // Adicione outros comandos aqui
    }

    private void addCommand(command command) {
        commands.put(command.getName(), command);
        for (String alias : command.getAliases()) {
            commands.put(alias, command);
        }
    }

    public void handleCommand(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split("\\s+");
        if (!split[0].startsWith(prefix)) return;  // Verifica o prefixo

        String commandName = split[0].substring(prefix.length()).toLowerCase(); // Remove o prefixo
        command command = commands.get(commandName);

        if (command != null) {
            command.execute(event);
        }
    }
}
