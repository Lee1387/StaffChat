package me.lee1387.staffchat.Listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.lee1387.staffchat.StaffChat;
import me.lee1387.staffchat.objects.Placeholder;
import me.lee1387.staffchat.enums.VelocityConfig;

import static me.lee1387.staffchat.enums.VelocityConfig.*;

public class JoinListener {

    public final StaffChat PLUGIN;

    public JoinListener(StaffChat plugin) {
        this.PLUGIN = plugin;
    }

    @Subscribe
    public void handle(LoginEvent event){
        if (!(StaffChat.getInstance().getServer().getAllPlayers().size() < 1)) {
            Player player = event.getPlayer();
            if (STAFF_JOIN_MESSAGE.get(Boolean.class)) {
                if (player.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))) {
                    for(Player all : StaffChat.getInstance().getServer().getAllPlayers()) {
                        if (all.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))) {
                            StaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                            (players -> players.hasPermission
                                                    (VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class)))
                                    .forEach(players -> STAFF_JOIN_MESSAGE_FORMAT.send(players,
                                            new Placeholder("user", player.getUsername()),
                                            new Placeholder("prefix", PREFIX.color())));
                        }
                    }
                }
            }
        }
    }

    @Subscribe
    public void handle(DisconnectEvent event){
        if (StaffChat.getInstance().getServer().getAllPlayers().size() >= 1) {
            Player player = event.getPlayer();
            if (STAFF_QUIT_MESSAGE.get(Boolean.class)) {
                if (player.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))) {
                    for(Player all : StaffChat.getInstance().getServer().getAllPlayers()) {
                        if (all.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))) {
                            StaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                            (players -> players.hasPermission
                                                    (VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class)))
                                    .forEach(players -> STAFF_QUIT_MESSAGE_FORMAT.send(players,
                                            new Placeholder("user", player.getUsername()),
                                            new Placeholder("prefix", PREFIX.color())));
                        }
                    }
                }
            }
        }
    }
}
