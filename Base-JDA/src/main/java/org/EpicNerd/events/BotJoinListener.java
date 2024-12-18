package org.EpicNerd.events;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotJoinListener extends ListenerAdapter {
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        event.getGuild().getTextChannels().stream()
                .filter(channel -> channel.canTalk())
                .findFirst()
                .ifPresent(channel -> {
                    sendSticker(channel.getId(), "1318070862670205029", () -> {
                        RestAction<?> messageAction = channel.sendMessage("Cheguei! Toc, toc! Tem alguém aí? ʕ•́ᴥ•̀ʔっ Ah, quase me esquecendo, obrigado por me adicionar! ≧◠ᴥ◠≦");

                        messageAction.queue(message -> {
                            scheduler.schedule(() -> message.delete().queue(), 5, TimeUnit.MINUTES);
                        });
                    });
                });
    }

    private void sendSticker(String channelId, String stickerId, Runnable onSuccess) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("TOKEN");

        RequestBody body = new FormBody.Builder()
                .add("sticker_ids", stickerId)
                .build();

        Request request = new Request.Builder()
                .url("https://discord.com/api/v10/channels/" + channelId + "/messages")
                .post(body)
                .addHeader("Authorization", "Bot " + token)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    String messageId = getMessageIdFromResponse(responseBody);

                    if (messageId != null) {
                        deleteMessageAfterDelay(channelId, messageId);
                        onSuccess.run();
                    }
                } else {
                    System.err.println("Failed to send sticker: " + response.body().string());
                }
            }
        });
    }

    private void deleteMessageAfterDelay(String channelId, String messageId) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("TOKEN");

        // Programar para apagar a mensagem após 5 minutos
        Runnable task = () -> {
            Request deleteRequest = new Request.Builder()
                    .url("https://discord.com/api/v10/channels/" + channelId + "/messages/" + messageId)
                    .delete()
                    .addHeader("Authorization", "Bot " + token)
                    .build();

            httpClient.newCall(deleteRequest).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        System.err.println("Failed to delete message: " + response.body().string());
                    }
                }
            });
        };

        scheduler.schedule(task, 5, TimeUnit.MINUTES);
    }

    private String getMessageIdFromResponse(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse.getString("id");
    }
}
