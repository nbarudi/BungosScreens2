package ca.bungo.screens.api.components;

import ca.bungo.screens.Screens;
import ca.bungo.screens.api.RenderContext;
import ca.bungo.screens.api.ScreenComponent;
import ca.bungo.screens.api.components.generics.SimpleRectComponent;
import ca.bungo.screens.utility.TextDisplayMetrics;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Screen implements RenderContext {

    private static final double DEFAULT_MAX_REACH = 5.0; // adjust to whatever interact range makes sense
    private static final float PARALLEL_EPSILON = 1e-4f;

    private String id;

    private Location location;
    private Quaternionf orientation;

    private Vector3f right;
    private Vector3f down;
    private Vector3f normal;

    private int width;
    private int height;

    private double maxReach = DEFAULT_MAX_REACH;

    private Color backgroundColor = Color.fromARGB(0, 0, 0, 0);

    private final List<ScreenComponent> screenComponents;

    private SimpleRectComponent background;

    public Screen(String id, Location location, Quaternionf orientation, int width, int height) {
        this.id = id;
        this.location = location;
        this.orientation = new Quaternionf(orientation).normalize();
        this.width = width;
        this.height = height;

        this.maxReach = (double) (width/40 + height/40);

        this.screenComponents = new ArrayList<>();
        calculateVectors();
    }

    public Screen(Location location, Quaternionf orientation, int width, int height) {
        this(UUID.randomUUID().toString(), location, orientation, width, height);
    }

    private void calculateVectors(){
        this.right = new Vector3f(1, 0, 0).rotate(orientation);
        this.down = new Vector3f(0, -1, 0).rotate(orientation);
        this.normal = new Vector3f(0, 0, -1).rotate(orientation);
    }

    public void move(Location location){
        //ToDo: Teleport All Entities
        this.location = location;
    }

    public void setOrientation(Quaternionf orientation){
        this.orientation = new Quaternionf(orientation).normalize();
        calculateVectors();
    }

    public Quaternionf orientation(){
        return new Quaternionf(orientation);
    }

    public float width(){
        return width;
    }
    public float height(){
        return height;
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public String id() {
        return id;
    }
    public Location origin(){
        return location.clone();
    }

    public Vector3f right(){
        return new Vector3f(right);
    }
    public Vector3f down(){
        return new Vector3f(down);
    }
    public Vector3f normal(){
        return new Vector3f(normal);
    }

    @Override
    public Screen screen() {
        return this;
    }

    public void addComponent(ScreenComponent screenComponent) {
        screenComponents.add(screenComponent);
    }

    public boolean removeComponent(ScreenComponent screenComponent) {
        return screenComponents.remove(screenComponent);
    }

    public List<ScreenComponent> getScreenComponents(){
        return Collections.unmodifiableList(screenComponents);
    }

    public @Nullable ScreenComponent getComponent(String id){
        for (ScreenComponent screenComponent : screenComponents){
            if(screenComponent.id().equals(id)){
                return screenComponent;
            }
        }
        return null;
    }

    public void setColor(Color color){
        this.backgroundColor = color;
    }

    public Color getColor(){
        return backgroundColor;
    }


    public void spawn() {
        spawnBackground(this.backgroundColor);
        for(ScreenComponent screenComponent : screenComponents){
            screenComponent.spawn(this);
        }
    }
    public void update() {
        if(background != null) background.update(this);
        for(ScreenComponent screenComponent : screenComponents){
            screenComponent.update(this);
        }
    }

    public void despawn() {
        if(background != null) {
            background.despawn();
            background = null;
        }
        for (ScreenComponent screenComponent : screenComponents) {
            screenComponent.despawn();
        }
    }

    private void spawnBackground(Color color) {
        this.background = new SimpleRectComponent(0, 0, width, height, backgroundColor);
        background.spawn(this);

    }

    public InteractableComponent handleClick(Player player){
        Vector3f hit = raycastToPlane(player);
        if(hit == null)
            return null;

        Vector3f rel = hit.sub(location.toVector().toVector3f());
        float localX = rel.dot(right) / (float) TextDisplayMetrics.UNIT_SCALE;
        float localY = rel.dot(down)  / (float) TextDisplayMetrics.UNIT_SCALE;

        if(localX < 0 || localX > width) return null;
        if(localY < 0 || localY > height) return null;

        for (ScreenComponent screenComponent : screenComponents.reversed()){
            if(!(screenComponent instanceof InteractableComponent interactableComponent)) continue;

            float relX = localX - screenComponent.x();
            float relY = localY - screenComponent.y();

            if(relX >= 0 && relX <= screenComponent.width() && relY >= 0 && relY <= screenComponent.height()){
                interactableComponent.onClick(player, localX, localY);
                return interactableComponent;
            }
        }

        return null;
    }

    public Vector3f raycastToPlane(Player player){
        if (!player.getWorld().equals(location.getWorld())) {
            return null;
        }

        Vector3f rayOrigin = player.getEyeLocation().toVector().toVector3f();
        Vector3f rayDirection = player.getEyeLocation().getDirection().toVector3f();

        Vector3f planePoint = location.toVector().toVector3f();

        float denom = rayDirection.dot(normal);
        if (denom <= -PARALLEL_EPSILON) return null;

        float t = new Vector3f(planePoint).sub(rayOrigin).dot(normal) / denom;
        if (t < 0 || t > this.maxReach) {
            return null;
        }

        return new Vector3f(rayOrigin).add(new Vector3f(rayDirection).mul(t));
    }
}
