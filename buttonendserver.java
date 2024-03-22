import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class Buttonendserver extends JavaPlugin implements Listener {

    private static final int COUNTDOWN_DURATION = 10; // Durasi hitung mundur dalam detik
    private static final String[] COUNTDOWN_MESSAGES = {
            "Simulasi berakhir dalam %d detik...",
            "Simulasi berakhir dalam %d detik...",
            "Simulasi berakhir dalam %d detik...",
            "Simulasi berakhir dalam %d detik...",
            "Simulasi berakhir dalam %d detik...",
            "Simulasi berakhir dalam %d detik...",
            "Simulasi berakhir dalam %d detik...",
            "Simulasi berakhir dalam %d detik...",
            "Simulasi berakhir dalam %d detik...",
            "Simulasi Berakhir!"
    };

    private BukkitScheduler scheduler;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        scheduler = Bukkit.getScheduler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("button")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Perintah ini hanya dapat digunakan oleh pemain!");
                return true;
            }

            Player player = (Player) sender;
            player.getInventory().addItem(createButton());
            player.sendMessage(ChatColor.GOLD + "**Button telah diberikan!**");

            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType() == Material.REDSTONE_TORCH) {
            event.setCancelled(true);

            startCountdown();
        }
    }

    private void startCountdown() {
        scheduler.scheduleSyncTaskRepeated(this, () -> {
            int remainingSeconds = COUNTDOWN_DURATION - countdownTaskIndex;

            if (remainingSeconds <= 0) {
                Bukkit.broadcastMessage(ChatColor.RED + COUNTDOWN_MESSAGES[countdownTaskIndex]);
                Bukkit.shutdown();
                return;
            }

            Bukkit.broadcastMessage(String.format(ChatColor.RED + COUNTDOWN_MESSAGES[countdownTaskIndex], remainingSeconds));
            countdownTaskIndex++;
        }, 0L, 20L);

        countdownTaskIndex = 0;
        Bukkit.broadcastMessage(ChatColor.RED + "Simulasi akan berakhir dalam 10 detik...");
    }

    private int countdownTaskIndex = 0;

    private ItemStack createButton() {
        ItemStack button = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta buttonMeta = button.getItemMeta();
        buttonMeta.setDisplayName(ChatColor.GOLD + "Button Countdown");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Klik kanan untuk mengakhiri simulasi.");
        buttonMeta.setLore(lore);
        button.setItemMeta(buttonMeta);

        return button;
    }
}
