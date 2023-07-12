package ru.victims.plugin;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class VictimStartCommand implements CommandExecutor {

    private final VictimPlugin plugin;

    public VictimStartCommand(VictimPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        Audience audience = this.plugin.adventure().all();
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(500), Duration.ofMillis(500));
        int rnd = ThreadLocalRandom.current().nextInt(0, list.toArray().length);
        final int[] count = {4};
        new BukkitRunnable() {
            @Override
            public void run() {
                count[0]--;
                System.out.println(count[0]);
                Title title = Title.title(Component.text(count[0] + ".."), Component.empty(), times);
                audience.showTitle(title);
                if(count[0] <= 1) {
                    System.out.println(list.get(rnd).getName());
                    Title titleName = Title.title(Component.text(list.get(rnd).getName()), Component.empty(), times);
                    audience.showTitle(titleName);
                    list.get(rnd).getPlayer().getInventory().addItem(new ItemStack(Material.NETHERITE_SWORD));
                    list.get(rnd).getPlayer().getInventory().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
                    list.get(rnd).getPlayer().getInventory().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
                    list.get(rnd).getPlayer().getInventory().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
                    list.get(rnd).getPlayer().getInventory().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
                    list.forEach(player -> {
                        if (player.getName() == list.get(rnd).getName()) {
                        } else {
                            player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                            player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                            player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                            player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                            player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                        }
                    });
                    BossBar countBar = BossBar.bossBar(Component.text("Таймер : 180")
                            .color(NamedTextColor.WHITE), 1.0f, BossBar.Color.RED, BossBar.Overlay.NOTCHED_20);
                    audience.showBossBar(countBar);
                    final int[] countdown = {180};
                    Player player = list.get(rnd).getPlayer();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            countdown[0]--;
                            if (countdown[0] <= 0 || countBar.progress() - 0.0055f <= 0.0f){
                                audience.hideBossBar(countBar);
                                Bukkit.getOnlinePlayers().forEach(player -> {player.sendMessage("Победа жертвы."); player.getInventory().clear();});
                                cancel();
                            }
                            if (player.getHealth() <= 0) {
                                audience.hideBossBar(countBar);
                                Bukkit.getOnlinePlayers().forEach(player -> {player.sendMessage("Победа охотников."); player.getInventory().clear();});
                                cancel();
                            }
                            countBar.name(Component.text("Таймер : " + countdown[0]));
                            countBar.progress(countBar.progress() - 0.0055f);
                        }
                    }.runTaskTimerAsynchronously(plugin, 0, 20);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 0, 20);
        return true;
    }
}
