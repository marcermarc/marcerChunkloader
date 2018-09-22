package de.marcermarc.chunkloader.listener;

import de.marcermarc.chunkloader.Messages;
import de.marcermarc.chunkloader.Util;
import de.marcermarc.chunkloader.controller.PluginController;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Command implements CommandExecutor, TabCompleter, Listener {
    private static final String CONFIG = "config";
    private static final String CHUNK = "chunk";

    private static final String BASE_BLOCKS = "base_blocks";
    private static final String ITEMS = "activation_items";
    private static final String MAX_TIER = "max_tier";
    private static final String DEBUG = "debug";
    private static final String SAVE = "save";
    private static final String LOAD = "load";

    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private static final List<String> ARG1_NO_OP = Arrays.asList(CONFIG, CHUNK);
    private static final List<String> ARG1_OP = Arrays.asList(CONFIG, CHUNK);
    private static final List<String> ARG1_CONSOLE = Collections.singletonList(CONFIG);
    private static final List<String> ARG2_NO_OP_CONFIG = Arrays.asList(BASE_BLOCKS, ITEMS, MAX_TIER);
    private static final List<String> ARG2_OP_CONFIG = Arrays.asList(BASE_BLOCKS, ITEMS, MAX_TIER, DEBUG);
    private static final List<String> ARG3_CONFIG_ADDREMOVE = Arrays.asList(ADD, REMOVE);
    private static final List<String> ARG3_CONFIG_TRUEFALSE = Arrays.asList(TRUE, FALSE);

    private PluginController controller;

    public Command(PluginController controller) {
        this.controller = controller;
    }

    //region CommandExecutor
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(Messages.getInfo());
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case CONFIG:
                    return commandConfig(commandSender, args);
                case CHUNK:
                    if (commandSender instanceof Player) {
                        Chunk chunk = ((Player) commandSender).getLocation().getChunk();
                        if (controller.getWorld(chunk.getWorld()).getLoadedChunks().getLoadedChunks().contains(chunk)) {
                            commandSender.sendMessage(Messages.getChunkLoaded());
                        } else {
                            commandSender.sendMessage(Messages.getChunkNotLoaded());
                        }
                    }
            }
        }

        return false;
    }

    private boolean commandConfig(CommandSender commandSender, String[] args) {
        if (args != null && args.length > 1) {
            switch (args[1].toLowerCase()) {
                case BASE_BLOCKS:
                    return commandConfigBaseBlocks(commandSender, args);
                case ITEMS:
                    return commandConfigActivationItems(commandSender, args);
                case MAX_TIER:
                    return commandConfigMaxTier(commandSender, args);
                case DEBUG:
                    return commandConfigDebug(commandSender, args);
                case SAVE:
                    if (!commandSender.isOp()) {
                        commandSender.sendMessage(Messages.getNotAllowedConfig());
                    } else if (controller.getConfig().saveConfig()) {
                        commandSender.sendMessage(Messages.getSaveOk());
                    } else {
                        commandSender.sendMessage(Messages.getSaveFailed());
                    }
                    return true;
                case LOAD:
                    if (!commandSender.isOp()) {
                        commandSender.sendMessage(Messages.getNotAllowedConfig());
                    } else if (controller.getConfig().loadConfig()) {
                        commandSender.sendMessage(Messages.getLoadOk());
                    } else {
                        commandSender.sendMessage(Messages.getLoadFailed());
                    }
                    return true;
            }
        }
        return false;
    }

    private boolean commandConfigBaseBlocks(CommandSender commandSender, String[] args) {
        if (args != null && args.length == 2) {
            commandSender.sendMessage(Messages.getBaseBlocksList(controller.getConfig().getAllowedBaseBlocks().toArray(new Material[]{})));
        } else if (!commandSender.isOp()) {
            commandSender.sendMessage(Messages.getNotAllowedConfig());
        } else if (args != null && args.length == 4) {
            Material material = Util.stringToMaterial(args[3]);
            if (material == null || !material.isBlock()) {
                commandSender.sendMessage(Messages.getNotABlock());
                return true;
            }

            switch (args[2].toLowerCase()) {
                case ADD:
                    if (controller.getConfig().getAllowedBaseBlocks().contains(material)) {
                        commandSender.sendMessage(Messages.getBaseBlocksAddDuplicate(material));
                    } else {
                        controller.getConfig().getAllowedBaseBlocks().add(material);
                        commandSender.sendMessage(Messages.getBaseBlocksAddOk(material));
                    }
                    return true;

                case REMOVE:
                    if (controller.getConfig().getAllowedBaseBlocks().contains(material)) {
                        controller.getConfig().getAllowedBaseBlocks().remove(material);
                        commandSender.sendMessage(Messages.getBaseBlocksRemoveOk(material));
                    } else {
                        commandSender.sendMessage(Messages.getBaseBlocksRemoveDuplicate(material));
                    }
                    return true;

                default:
                    return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean commandConfigActivationItems(CommandSender commandSender, String[] args) {
        if (args != null && args.length == 2) {
            commandSender.sendMessage(Messages.getActivationItemsList(controller.getConfig().getAllowedActivationItems().toArray(new Material[]{})));
        } else if (!commandSender.isOp()) {
            commandSender.sendMessage(Messages.getNotAllowedConfig());
        } else if (args != null && args.length == 4) {
            Material material = Util.stringToMaterial(args[3]);
            if (material == null || !material.isItem()) {
                commandSender.sendMessage(Messages.getNotAItem());
                return true;
            }

            switch (args[2].toLowerCase()) {
                case ADD:
                    if (controller.getConfig().getAllowedActivationItems().contains(material)) {
                        commandSender.sendMessage(Messages.getActivationItemsAddDuplicate(material));
                    } else {
                        controller.getConfig().getAllowedActivationItems().add(material);
                        commandSender.sendMessage(Messages.getActivationItemsAddOk(material));
                    }
                    return true;

                case REMOVE:
                    if (controller.getConfig().getAllowedActivationItems().contains(material)) {
                        controller.getConfig().getAllowedActivationItems().remove(material);
                        commandSender.sendMessage(Messages.getActivationItemsRemoveOk(material));
                    } else {
                        commandSender.sendMessage(Messages.getActivationItemsRemoveDuplicate(material));
                    }
                    return true;

                default:
                    return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean commandConfigMaxTier(CommandSender commandSender, String[] args) {
        if (args != null && args.length == 2) {
            commandSender.sendMessage(Messages.getMaxTier(controller.getConfig().getMaxTier()));
        } else if (!commandSender.isOp()) {
            commandSender.sendMessage(Messages.getNotAllowedConfig());
        } else if (args != null && args.length == 3) {
            if (StringUtils.isNumeric(args[2])) {
                int newTier = Integer.parseInt(args[2]);
                controller.getConfig().setMaxTier(newTier);
                commandSender.sendMessage(Messages.getMaxTierOk(newTier));
            } else {
                commandSender.sendMessage(Messages.getNotNumber());
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean commandConfigDebug(CommandSender commandSender, String[] args) {
        if (args != null && args.length == 2) {
            if (controller.getConfig().isDebugMode()) {
                commandSender.sendMessage(Messages.getDebugTrue());
            } else {
                commandSender.sendMessage(Messages.getDebugFalse());
            }
        } else if (!commandSender.isOp()) {
            commandSender.sendMessage(Messages.getNotAllowedConfig());
        } else if (args != null && args.length == 3) {
            switch (args[2].toLowerCase()) {
                case TRUE:
                    controller.getConfig().setDebugMode(true);
                    commandSender.sendMessage(Messages.getDebugNowTrue());
                    return true;
                case FALSE:
                    controller.getConfig().setDebugMode(false);
                    commandSender.sendMessage(Messages.getDebugNowFalse());
                    return true;

                default:
                    commandSender.sendMessage(Messages.getNotBoolean());
                    return true;
            }
        } else {
            return false;
        }
        return true;
    }

    //endregion

    //region TabComplete
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                return onTabCompleteArg1(sender, args);
            case 2:
                return onTabCompleteArg2(sender, args);
            case 3:
                return onTabCompleteArg3(sender, args);
            case 4:
                return onTabCompleteArg4(sender, args);
            default:
                return null;
        }
    }

    private List<String> onTabCompleteArg1(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return Util.tabCompleteFilter(ARG1_CONSOLE, args[0]);
        } else if (sender.isOp()) {
            return Util.tabCompleteFilter(ARG1_OP, args[0]);
        } else {
            return Util.tabCompleteFilter(ARG1_NO_OP, args[0]);
        }
    }

    private List<String> onTabCompleteArg2(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(CONFIG)) {
            if (sender.isOp()) {
                return Util.tabCompleteFilter(ARG2_OP_CONFIG, args[1]);
            } else {
                return Util.tabCompleteFilter(ARG2_NO_OP_CONFIG, args[1]);
            }
        }
        return null;
    }

    private List<String> onTabCompleteArg3(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(CONFIG))
            if (args[1].equalsIgnoreCase(BASE_BLOCKS) || args[1].equalsIgnoreCase(ITEMS)) {
                return Util.tabCompleteFilter(ARG3_CONFIG_ADDREMOVE, args[2]);
            } else if (args[1].equalsIgnoreCase(MAX_TIER)) {
                return Collections.singletonList("[<tier>]");
            } else if (args[1].equalsIgnoreCase(DEBUG)) {
                return Util.tabCompleteFilter(ARG3_CONFIG_TRUEFALSE, args[2]);
            }
        return null;
    }

    private List<String> onTabCompleteArg4(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(CONFIG) && sender.isOp()) {
            if (args[1].equalsIgnoreCase(BASE_BLOCKS)) {
                if (args[2].equalsIgnoreCase(ADD)) {
                    return Util.tabCompleteFilter(
                            Stream.of(Material.values())
                                    .filter(Material::isBlock)
                                    .filter(material -> !controller.getConfig().getAllowedBaseBlocks().contains(material))
                                    .map(Util::materialToString)
                                    .collect(Collectors.toList()),
                            args[3]);

                } else if (args[2].equals(REMOVE)) {
                    return Util.tabCompleteFilter(
                            Stream.of(Material.values())
                                    .filter(Material::isBlock)
                                    .filter(material -> controller.getConfig().getAllowedBaseBlocks().contains(material))
                                    .map(Util::materialToString)
                                    .collect(Collectors.toList()),
                            args[3]);
                }
            } else if (args[1].equalsIgnoreCase(ITEMS)) {
                if (args[2].equalsIgnoreCase(ADD)) {
                    return Util.tabCompleteFilter(
                            Stream.of(Material.values())
                                    .filter(Material::isItem)
                                    .filter(material -> !controller.getConfig().getAllowedActivationItems().contains(material))
                                    .map(Util::materialToString)
                                    .collect(Collectors.toList()),
                            args[3]);

                } else if (args[2].equals(REMOVE)) {
                    return Util.tabCompleteFilter(
                            Stream.of(Material.values())
                                    .filter(Material::isItem)
                                    .filter(material -> controller.getConfig().getAllowedActivationItems().contains(material))
                                    .map(Util::materialToString)
                                    .collect(Collectors.toList()),
                            args[3]);
                }
            }
        }
        return null;
    }


    //endregion
}