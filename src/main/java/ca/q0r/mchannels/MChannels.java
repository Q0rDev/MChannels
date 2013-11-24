package ca.q0r.mchannels;

import ca.q0r.mchannels.channels.ChannelManager;
import ca.q0r.mchannels.commands.MChannelsCommand;
import ca.q0r.mchannels.events.ChatListener;
import ca.q0r.mchannels.yml.YmlManager;
import ca.q0r.mchat.metrics.Metrics;
import ca.q0r.mchat.util.MessageUtil;
import ca.q0r.mchat.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MChannels extends JavaPlugin {
    // Default Plugin Data
    private PluginManager pm;
    private PluginDescriptionFile pdfFile;

    public void onEnable() {
        // Initialize Plugin Data
        pm = getServer().getPluginManager();
        pdfFile = getDescription();

        try {
            // Initialize and Start the Timer
            Timer timer = new Timer();

            // Initialize Metrics
            getServer().getScheduler().runTaskLater(this, new BukkitRunnable(){
                @Override
                public void run() {
                    try {
                        Metrics metrics = new Metrics(Bukkit.getPluginManager().getPlugin("MChannels"));
                        metrics.start();
                    } catch (Exception ignored) {}
                }
            }, 200);

            // Initialize Classes
            YmlManager.initialize();
            ChannelManager.initialize();

            // Register Events
            registerEvents();

            // Setup Command
            regCommands("mchannel", new MChannelsCommand());

            // Stop the Timer
            timer.stop();

            // Calculate Startup Timer
            long diff = timer.difference();

            MessageUtil.log("[" + pdfFile.getName() + "] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " is enabled! [" + diff + "ms]");
        } catch(NoClassDefFoundError ignored) {
            pm.disablePlugin(this);
        }
    }

    public void onDisable() {
        try {
            // Initialize and Start the Timer
            Timer timer = new Timer();

            getServer().getScheduler().cancelTasks(this);

            // Unload Yml
            YmlManager.unload();

            // Stop the Timer
            timer.stop();

            // Calculate Shutdown Timer
            long diff = timer.difference();

            MessageUtil.log("[" + pdfFile.getName() + "] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " is disabled! [" + diff + "ms]");
        } catch(NoClassDefFoundError ignored) {
            System.err.println("[" + pdfFile.getName() + "] MChat not found disabling!");
            System.out.println("[" + pdfFile.getName() + "] " + pdfFile.getName() + " v" + pdfFile.getVersion() + " is disabled!");
        }
    }

    void registerEvents() {
        pm.registerEvents(new ChatListener(), this);
    }

    void regCommands(String command, CommandExecutor executor) {
        if (getCommand(command) != null) {
            getCommand(command).setExecutor(executor);
        }
    }
}
