package ca.bungo.screens.api.registry;

import ca.bungo.screens.Screens;
import ca.bungo.screens.api.ScreenComponent;
import ca.bungo.screens.api.animation.Animatable;
import ca.bungo.screens.api.impl.components.Screen;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ScreenManager {

    private final Map<String, Screen> screensById = new HashMap<>();
    private final Map<World, List<Screen>> screensByWorld = new HashMap<>();

    private BukkitTask task;

    public ScreenManager() {
        handleAnimations();
    }

    public void register(Screen screen) {
        if (screensById.containsKey(screen.id())) {
            throw new IllegalArgumentException("Screen id already registered: " + screen.id());
        }
        screensById.put(screen.id(), screen);
        screensByWorld.computeIfAbsent(screen.origin().getWorld(), w -> new ArrayList<>()).add(screen);
        screen.spawn();
    }

    public void unregister(String id) {
        Screen screen = screensById.remove(id);
        if (screen == null) return;
        screensByWorld.getOrDefault(screen.origin().getWorld(), List.of()).remove(screen);
        screen.despawn();
    }

    public Screen get(String id) {
        return screensById.get(id);
    }

    public List<Screen> getScreensInWorld(World world) {
        return Collections.unmodifiableList(screensByWorld.getOrDefault(world, List.of()));
    }

    private void handleAnimations() {
        task = Screens.getInstance().getServer().getScheduler().runTaskTimer(Screens.getInstance(), () -> {
            for(Screen screen : screensById.values()){
                for(ScreenComponent component : screen.getScreenComponents()){
                    if(!(component instanceof Animatable animatable)) continue;
                    animatable.tickAnimations(1f/20f);
                }
            }
        },1, 1);
    }

    public void onDisable() {
        for (Screen screen : screensById.values()) {
            screen.despawn();
        }
        screensById.clear();
        screensByWorld.clear();
        if(task != null) task.cancel();
    }
}
