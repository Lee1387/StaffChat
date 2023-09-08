package me.lee1387.staffchat.Commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.lee1387.staffchat.StaffChat;
import me.lee1387.staffchat.enums.VelocityConfig;
import me.lee1387.staffchat.objects.Placeholder;
import me.lee1387.staffchat.objects.PlayerCache;
import me.lee1387.staffchat.objects.TextFile;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

import static me.lee1387.staffchat.enums.VelocityConfig.*;

public class StaffChatCommand implements SimpleCommand {

    public final StaffChat PLUGIN;

    public StaffChatCommand(StaffChat plugin) {
        this.PLUGIN = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {

            if (!(commandSource instanceof Player)) {
                ARGUMENTS.send(commandSource, new Placeholder("prefix", PREFIX.color()));
                return;
            }

            Player player = (Player) commandSource;

            if (commandSource.hasPermission(STAFFCHAT_USE_PERMISSION.get(String.class))) {
                if (!PlayerCache.getToggled_2().contains(player.getUniqueId())) {
                    PlayerCache.getToggled_2().add(player.getUniqueId());
                    player.sendMessage(Component.text("You have entered the Staff Chat."));
                    return;
                } else if (PlayerCache.getToggled_2().contains(player.getUniqueId())) {
                    PlayerCache.getToggled_2().remove(player.getUniqueId());
                    player.sendMessage(Component.text("You have left the Staff Chat."));
                    return;
                }
            } else {
                NO_PERMISSION.send(commandSource,
                        new Placeholder("prefix", PREFIX.color()));
                return;
            }
        }

        if (args[0].equalsIgnoreCase("help")) {
            ARGUMENTS.send(commandSource,
                    new Placeholder("prefix", PREFIX.color()));
            return;
        }

        if (DEBUG.get(Boolean.class)) {
            PLUGIN.getLogger().info("[DEBUG] First IF passed");
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (commandSource.hasPermission(VelocityConfig.STAFFCHAT_RELOAD_PERMISSION.get(String.class))) {
                TextFile.reloadAll();
                RELOADED.send(commandSource,
                        new Placeholder("prefix", PREFIX.color()));
                return;
            } else {
                NO_PERMISSION.send(commandSource,
                        new Placeholder("prefix", PREFIX.color()));
            }
        }

        if (DEBUG.get(Boolean.class)) {
            PLUGIN.getLogger().info("[DEBUG] Second IF passed");
        }


        if (args[0].equalsIgnoreCase("toggle")) {

            if (!(commandSource instanceof Player)) {
                PLAYER_ONLY.send(commandSource, new Placeholder("prefix", PREFIX.color()));
                return;
            }

            Player player = (Player) commandSource;

            if (player.hasPermission(VelocityConfig.STAFFCHAT_TOGGLE_PERMISSION.get(String.class))) {
                if (!PlayerCache.getToggled().contains(player.getUniqueId())) {
                    PlayerCache.getToggled().add(player.getUniqueId());
                    player.sendMessage(Component.text("Added"));
                    return;
                }
            }

            PlayerCache.getToggled().remove(player.getUniqueId());

            player.sendMessage(Component.text("Removed"));

            return;
        }

        if (DEBUG.get(Boolean.class)) {
            PLUGIN.getLogger().info("[DEBUG] Third IF passed");
        }

        if (args[0].equalsIgnoreCase("mute")) {

            if (commandSource.hasPermission(VelocityConfig.STAFFCHAT_MUTE_PERMISSION.get(String.class))) {
                if (!PlayerCache.getMuted().contains("true")) {
                    PlayerCache.getMuted().add("true");
                    commandSource.sendMessage(Component.text("Blocked"));
                } else {
                    PlayerCache.getMuted().remove("true");
                    commandSource.sendMessage(Component.text("Unblocked"));
                }
                return;
            }
        }

        if (DEBUG.get(Boolean.class)) {
            PLUGIN.getLogger().info("[DEBUG] Fourth IF passed");
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        if (DEBUG.get(Boolean.class)) {
            PLUGIN.getLogger().info("Message: " + message);
        }

        String sender = !(commandSource instanceof Player) ? CONSOLE_PREFIX.get(String.class) :
                ((Player) commandSource).getUsername();

        if (DEBUG.get(Boolean.class)) {
            PLUGIN.getLogger().info("Sender: " + sender);
        }

        if (commandSource.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))) {
            if (!PlayerCache.getMuted().contains("true")) {
                if (DEBUG.get(Boolean.class)) {
                    PLUGIN.getLogger().info("[DEBUG] Deciding whether the commandSender is a player or the console...");
                }
                if (commandSource instanceof Player) {
                    if (DEBUG.get(Boolean.class)) {
                        PLUGIN.getLogger().info("[DEBUG] Player detected");
                    }
                    StaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                    (players -> players.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                            && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                            .forEach(players -> STAFFCHAT_FORMAT.send(players,
                                    new Placeholder("user", sender),
                                    new Placeholder("message", message),
                                    new Placeholder("prefix", PREFIX.color())));
                    if (DEBUG.get(Boolean.class)) {
                        PLUGIN.getLogger().info("Message was sent to " + StaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                (players -> players.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                        && !(PlayerCache.getToggled().contains(players.getUniqueId()))));
                    }
                } else if (CONSOLE_CAN_TALK.get(Boolean.class)) {
                    if (DEBUG.get(Boolean.class)) {
                        PLUGIN.getLogger().info("[DEBUG] Console detected");
                    }
                    StaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                    (players -> players.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                            && !(PlayerCache.getToggled().contains(players.getUniqueId())))
                            .forEach(players -> STAFFCHAT_FORMAT.send(players,
                                    new Placeholder("user", sender),
                                    new Placeholder("message", message),
                                    new Placeholder("prefix", PREFIX.color())));
                    if (DEBUG.get(Boolean.class)) {
                        PLUGIN.getLogger().info("Message was sent to " + StaffChat.getInstance().getServer().getAllPlayers().stream().filter
                                (players -> players.hasPermission(VelocityConfig.STAFFCHAT_USE_PERMISSION.get(String.class))
                                        && !(PlayerCache.getToggled().contains(players.getUniqueId()))));
                    }
                    STAFFCHAT_FORMAT.send(commandSource,
                            new Placeholder("user", sender),
                            new Placeholder("message", message),
                            new Placeholder("prefix", PREFIX.color()));
                    if (DEBUG.get(Boolean.class)) {
                        PLUGIN.getLogger().info("Message was sent to " + commandSource);
                    }
                }
            } else {
                STAFFCHAT_MUTED.send(commandSource,
                        new Placeholder("prefix", PREFIX.color()));
            }
        } else {
            NO_PERMISSION.send(commandSource,
                    new Placeholder("prefix", PREFIX.color()));
        }
    }
}
