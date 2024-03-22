import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.event.EventHandler;
import org.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Disablesomeitem extends JavaPlugin implements Listener {

    private static final String PERMISSION_ADMIN = "nonaktifkanitem.admin";
    private static final Map<Material, Boolean> statusItem = new HashMap<>();

    static {
        statusItem.put(Material.ELYTRA, false);
        statusItem.put(Material.END_CRYSTAL, false);
        statusItem.put(Material.RESPAWN_ANCHOR, false);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Material itemMaterial = event.getClickedBlock().getType();
      
        if (statusItem.containsKey(itemMaterial) && !statusItem.get(itemMaterial)) {
            event.setCancelled(true); // Batalkan interaksi dengan item
            player.sendMessage(ChatColor.RED + "**Item " + itemMaterial.name() + " dinonaktifkan!**");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("enable") || cmd.getName().equalsIgnoreCase("disable")) {
            if (!sender.hasPermission(PERMISSION_ADMIN)) {
                sender.sendMessage(ChatColor.RED + "Anda tidak memiliki izin untuk menggunakan perintah ini.");
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Penggunaan: /" + cmd.getName() + " (nama item)");
                return true;
            }

            Material itemMaterial = Material.getMaterial(args[0].toUpperCase());
            if (!statusItem.containsKey(itemMaterial)) {
                sender.sendMessage(ChatColor.RED + "Item " + args[0] + " tidak valid.");
                return true;
            }

            boolean newState = !statusItem.get(itemMaterial);
            statusItem.put(itemMaterial, newState);

            sender.sendMessage(ChatColor.GOLD + "Item " + args[0] + " " + (newState ? "diaktifkan" : "dinonaktifkan") + ".");

            return true;
        }

        return false;
    }
}
