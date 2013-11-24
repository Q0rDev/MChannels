package ca.q0r.mchannels.commands;

import ca.q0r.mchannels.channels.Channel;
import ca.q0r.mchannels.channels.ChannelManager;
import ca.q0r.mchannels.channels.Occupant;
import ca.q0r.mchannels.channels.types.*;
import ca.q0r.mchannels.types.ChannelCommandType;
import ca.q0r.mchannels.types.ChannelEditType;
import ca.q0r.mchannels.types.ChannelType;
import ca.q0r.mchannels.yml.locale.LocaleType;
import ca.q0r.mchat.api.Parser;
import ca.q0r.mchat.util.CommandUtil;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MChannelsCommand implements CommandExecutor {
    public MChannelsCommand() { }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName();

        if (!cmd.equalsIgnoreCase("mchannel")) {
            return true;
        }


        if (args.length < 1) {
            sendDefaultMessage(sender, cmd);
            return true;
        }

        ChannelCommandType cType = ChannelCommandType.fromName(args[0].toLowerCase());

        if (cType == null) {
            sendDefaultMessage(sender, cmd);
            return true;
        }

        if (args.length < cType.getLength()) {
            if (cType.getHelp(cmd) == null) {
                sendDefaultMessage(sender, cmd);
                return true;
            }

            MessageUtil.sendMessage(sender, cType.getHelp(cmd));
            return true;
        }

        switch(cType) {
            case RELOAD:
                if (!CommandUtil.hasCommandPerm(sender, cType.getPermission())) {
                    return true;
                }

                ChannelManager.reloadChannels();
                MessageUtil.sendMessage(sender, "Channels Reloaded.");

                return true;
            case TYPES:
                if (!CommandUtil.hasCommandPerm(sender, cType.getPermission())) {
                    return true;
                }

                String types = "";

                for (ChannelType type : ChannelType.values()) {
                    types += " " + type.getName();
                }

                types = types.trim();

                MessageUtil.sendMessage(sender, "All valid ChannelTypes: '" + types + "'.");

                return true;
            case EDIT_TYPES:
                if (!CommandUtil.hasCommandPerm(sender, cType.getPermission())) {
                    return true;
                }

                String editTypes = "";

                for (ChannelEditType type : ChannelEditType.values()) {
                    editTypes += " " + type.getName();
                }

                editTypes = editTypes.trim();

                MessageUtil.sendMessage(sender, "All valid ChannelEditTypes: '" + editTypes + "'.");

                return true;
        }

        String cName = args[1].toLowerCase();
        Channel channel = ChannelManager.getChannel(cName);

        if (!CommandUtil.hasCommandPerm(sender, cType.getPermission(cName))) {
            return true;
        }

        switch (cType) {
            case CREATE:
                if (channel != null) {
                    MessageUtil.sendMessage(sender, "'" + cName + "' has already been created.");
                    return true;
                }

                ChannelType type = ChannelType.fromName(args[2]);

                if (type == null) {
                    MessageUtil.sendMessage(sender, "'" + args[2] + "' is not a valid ChannelType. Use '/" + cmd + " types' for more information.");
                    return true;
                }

                String prefix = "[";
                String suffix = "]";

                switch(type) {
                    case GLOBAL:
                        channel = new Global(cName, prefix, suffix);
                        break;
                    case CHUNK:
                        channel = new Chunk(cName, prefix, suffix);
                        break;
                    case PRIVATE:
                        channel = new Chunk(cName, prefix, suffix);
                        break;
                    case WORLD:
                        channel = new World(cName, prefix, suffix);
                        break;
                }

                if (channel == null && args.length < 4) {
                    MessageUtil.sendMessage(sender, "'" + type.getName() + "' ChannelType cannot be created with only 3 arguments. Use '/" + cmd + " create' for more information.");
                    return true;
                }

                switch (type) {
                    case PASSWORD:
                        channel = new Password(cName, prefix, suffix, args[3]);
                        break;
                    case LOCAL:
                        channel = new Local(cName, prefix, suffix, Integer.parseInt(args[3]));
                        break;
                }

                ChannelManager.addChannel(channel);
                MessageUtil.sendMessage(sender, "You have successfully created Channel '" + cName + "' of type '" + type.getName() + "'.");

                return true;
            case REMOVE:
                if (channel == null) {
                    MessageUtil.sendMessage(sender, "'" + cName + "' is not a valid channel.");
                    return true;
                }

                ChannelManager.removeChannel(channel);
                MessageUtil.sendMessage(sender, "You have successfully removed Channel '" + cName + "'.");

                return true;
            case EDIT:
                if (channel == null) {
                    MessageUtil.sendMessage(sender, "'" + cName + "' is not a valid channel.");
                    return true;
                }

                if (ChannelEditType.fromName(args[2]) == null) {
                    MessageUtil.sendMessage(sender, "'" + args[2] + "' is not a valid EditType.");
                    return true;
                }

                ChannelEditType edit = ChannelEditType.fromName(args[2]);
                Object option = args[3];

                try {
                    switch(edit) {
                        case DEFAULT:
                            ChannelManager.setDefaultChannel(channel);
                            MessageUtil.sendMessage(sender, "You have successfully edited '" + cName + "'.");

                            return true;
                    }
                } catch (Exception ignored) {
                    MessageUtil.sendMessage(sender, "Error when converting '" + args[3] + "' to an Object of type '" + edit.getOptionClass().getSimpleName() + "'.");
                    return true;
                }

                if (option == null) {
                    MessageUtil.sendMessage(sender, "The option '" + args[3] + "' seems to not be resolving properly.");
                    return true;
                }

                ChannelManager.editChannel(channel, edit, option);
                MessageUtil.sendMessage(sender, "You have successfully edited '" + cName + "'.");

                return true;
        }

        if (!(sender instanceof Player)) {
            MessageUtil.sendMessage(sender, "Console's can't interact with channels.");
            return true;
        }

        Player player = (Player) sender;

        if (channel == null) {
            MessageUtil.sendMessage(sender, "No Channel by the name of '" + cName + "' could be found.");
            return true;
        }

        Occupant occupant = channel.getOccupant(sender.getName());

        switch(cType) {
            case JOIN:
                if (occupant != null) {
                    MessageUtil.sendMessage(sender, "You are already in channel '" + cName + "'.");
                    return true;
                }

                if (channel.getType() == ChannelType.PASSWORD) {

                    if (args.length < 3) {
                        MessageUtil.sendMessage(sender, "'" + cName + "' is a Passworded channel. Please use '/" + cmd + " join [ChannelName] [Password]' to enter.");
                        return true;
                    } else if (!args[2].equalsIgnoreCase(((Password) channel).getPassword())) {
                        MessageUtil.sendMessage(sender, "Password entered for channel '" + cName + "' is invalid.");
                        return true;
                    }
                }

                channel.broadcastMessage(Parser.parseMessage(player.getName(), player.getWorld().getName(), "", LocaleType.FORMAT_JOIN.getRaw()));
                channel.add(new Occupant(sender.getName()));

                MessageUtil.sendMessage(sender, "You have successfully joined '" + cName + "'.");

                return true;
            case LEAVE:
                if (occupant == null) {
                    MessageUtil.sendMessage(sender, "You are not in channel '" + cName + "'.");
                    return true;
                }

                channel.remove(occupant);
                channel.broadcastMessage(Parser.parseMessage(player.getName(), player.getWorld().getName(), "", LocaleType.FORMAT_LEAVE.getRaw()));

                MessageUtil.sendMessage(sender, "You have successfully left '" + cName + "'.");

                return true;
            case AWAY:
                setAvailability(player, channel, false);
                return true;
            case BACK:
                setAvailability(player, channel, true);
                return true;
        }

        return true;
    }

    private void sendDefaultMessage(CommandSender sender, String cmd) {
        String[] message = new String[] {
                MessageUtil.format("'/" + cmd + " reload' to reload."),
                MessageUtil.format("'/" + cmd + " types' for more information."),
                MessageUtil.format("'/" + cmd + " editTypes' for more information."),
                MessageUtil.format("'/" + cmd + " create' for more information."),
                MessageUtil.format("'/" + cmd + " remove' for more information."),
                MessageUtil.format("'/" + cmd + " edit' for more information."),
                MessageUtil.format("'/" + cmd + " join' for more information."),
                MessageUtil.format("'/" + cmd + " leave' for more information."),
                MessageUtil.format("'/" + cmd + " away' for more information."),
                MessageUtil.format("'/" + cmd + " back' for more information.")
        };

        sender.sendMessage(message);
    }

    private void setAvailability(Player player, Channel channel, Boolean state) {
        if (channel.getType() == ChannelType.GLOBAL) {
            MessageUtil.sendMessage(player, "You cannot change availability in channel '" + channel.getName() + "'.");
            MessageUtil.sendMessage(player, "This is because it is a Global channel!");
            return;
        }

        Occupant occupant = channel.getOccupant(player.getName());

        if (occupant == null) {
            MessageUtil.sendMessage(player, "You are not in channel '" + channel.getName() + "'.");
            return;
        }

        if (occupant.getState() == state) {
            MessageUtil.sendMessage(player, "You are already " + (state ? "available" : "away") + " in channel '" + channel.getName() + "'.");
        } else {
            occupant.setState(state);
            MessageUtil.sendMessage(player, "You are now marked as " + (state ? "available" : "away") + " in channel '" + channel.getName() + "'.");
            channel.broadcastMessage(Parser.parsePlayerName(player.getName(), player.getWorld().getName()) + " is now " + (state ? "available" : "away") + "!");
        }
    }
}
