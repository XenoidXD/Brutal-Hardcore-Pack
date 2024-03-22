import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Disablemessage extends JavaPlugin implements Listener {

    private static final String PERMISSION_ADMIN = "nonaktifkanpesan.admin";
    private boolean modeNonaktif = false;
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (modeNonaktif) {
            Player player = event.getPlayer();
            event.setCancelled(true);

            // Kirim pesan balasan ke pemain
            player.sendMessage(ChatColor.RED + "**Anda tidak dapat mengirim pesan di server ini!**");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("nomessage")) {
            if (!sender.hasPermission(PERMISSION_ADMIN)) {
                sender.sendMessage(ChatColor.RED + "Anda tidak memiliki izin untuk menggunakan perintah ini.");
                return true;
            }

            modeNonaktif = !modeNonaktif; // status plugin

            if (modeNonaktif) {
                Bukkit.broadcastMessage(ChatColor.GOLD + "**Mode Nonaktifkan Pesan diaktifkan!**");
            } else {
                Bukkit.broadcastMessage(ChatColor.GOLD + "**Mode Nonaktifkan Pesan dinonaktifkan!**");
            }

            return true;
        }
        return false;
    }
}
