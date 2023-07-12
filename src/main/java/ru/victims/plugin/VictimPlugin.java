package ru.victims.plugin;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VictimPlugin extends JavaPlugin {

    private BukkitAudiences adventure;

    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("error no adventure :(");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        getCommand("victimstart").setExecutor(new VictimStartCommand(this));
        this.adventure = BukkitAudiences.create(this);
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }
}

