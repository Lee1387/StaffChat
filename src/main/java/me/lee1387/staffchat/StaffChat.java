package me.lee1387.staffchat;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.lee1387.staffchat.Commands.StaffChatCommand;
import me.lee1387.staffchat.Listeners.ChatListener;
import me.lee1387.staffchat.Listeners.JoinListener;
import me.lee1387.staffchat.objects.PlayerCache;
import me.lee1387.staffchat.objects.TextFile;
import lombok.Getter;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.logging.Logger;

@Getter
@Plugin(
        id = "staffchat",
        name = "StaffChat",
        version = "1.0.0",
        url = "github.com/lee1387",
        authors = "lee1387"
)
public class StaffChat {

    private final ProxyServer server;
    private final Logger logger;
    private final Path path;
    private TextFile configTextFile;
    private static StaffChat instance;

    public static StaffChat getInstance() {
        return instance;
    }
    @Inject
    public StaffChat(ProxyServer server, Logger logger, @DataDirectory Path path) {
        this.server = server;
        this.logger = logger;
        this.path = path;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;
        logger.info("§7Registering commands...");
        server.getCommandManager().register(server.getCommandManager()
                .metaBuilder("sc")
                .aliases("staffchat")
                .build(), new StaffChatCommand(this));
        server.getEventManager().register(this, new JoinListener(this));
        server.getEventManager().register(this, new ChatListener(this));
        logger.info("§7Commands registered §asuccessfully§7...");
        logger.info("§7Loading configurations...");
        configTextFile = new TextFile(path, "config.yml");
        logger.info("§7Configurations loaded §asuccessfully§7!");
        logger.info("§7Plugin successfully §aenabled§7, enjoy!");
    }
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("§7Clearing lists...");
        PlayerCache.getToggled_2().clear();
        PlayerCache.getToggled().clear();
        PlayerCache.getMuted().clear();
        logger.info("§7Successfully §cdisabled§7.");
    }
}
