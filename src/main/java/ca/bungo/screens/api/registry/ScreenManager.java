package ca.bungo.screens.api.registry;

import ca.bungo.screens.api.components.Screen;
import org.bukkit.World;

import java.util.*;

public class ScreenManager {

    private final Map<String, Screen> screensById = new HashMap<>();
    private final Map<World, List<Screen>> screensByWorld = new HashMap<>();

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

    public void onDisable() {
        for (Screen screen : screensById.values()) {
            screen.despawn();
        }
        screensById.clear();
        screensByWorld.clear();
    }
}
