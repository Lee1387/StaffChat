package me.lee1387.staffchat.Listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import me.lee1387.staffchat.StaffChat;
import me.lee1387.staffchat.enums.VelocityConfig;
import me.lee1387.staffchat.objects.Placeholder;
import me.lee1387.staffchat.objects.PlayerCache;

import static me.lee1387.staffchat.enums.VelocityConfig.*;

public class ChatListener {

    public final StaffChat PLUGIN;

    public ChatListener(StaffChat plugin) {
        this.PLUGIN = plugin;
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onChat(PlayerChatEvent event) {

        String message = event.getMessage();
        String sender = event.getPlayer().getUsername();

        if (PlayerCache.getToggled_2().contains(event.getPlayer().getUniqueId())) {
            if (event.getPlayer().hasPermission(STAFFCHAT_USE_PERMISSION.get(String.class))) {
                event.setResult(PlayerChatEvent.ChatResult.denied());
                StaffChat.getInstance().getServer().getAllPlayers().stream().filter
                        (players -> players.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                        .forEach(players -> STAFFCHAT_FORMAT.send(players,
                                new Placeholder("user", sender),
                                new Placeholder("message", message),
                                new Placeholder("prefix", PREFIX.color())));

            }
        }
    }
}