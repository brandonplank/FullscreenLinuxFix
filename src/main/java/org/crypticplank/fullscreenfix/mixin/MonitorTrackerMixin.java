package org.crypticplank.fullscreenfix.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.util.math.MathHelper.clamp;

@Mixin(MonitorTracker.class)
public class MonitorTrackerMixin {
    @Final @Shadow private Long2ObjectMap<Monitor> pointerToMonitorMap;

    @Shadow @Final private static Logger LOGGER;

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Nullable
    public Monitor getMonitor(Window window) {
        long l = GLFW.glfwGetWindowMonitor(window.getHandle());
        if (l != 0L) {
            return ((MonitorTracker)(Object)this).getMonitor(l);
        } else {
            int windowX = window.getX();
            int j = windowX + window.getWidth();
            int windowY = window.getY();
            int m = windowY + window.getHeight();
            int n = -1;
            Monitor finalMonitor = null;
            long o = GLFW.glfwGetPrimaryMonitor();
            LOGGER.info("[MonitorTrackerMixin] Selecting monitor - primary: {}, current monitors: {}", o, this.pointerToMonitorMap);
            for (Monitor monitor : this.pointerToMonitorMap.values()) {
                LOGGER.info("[MonitorTrackerMixin] {}", monitor.toString());
                int p = monitor.getViewportX();
                int q = p + monitor.getCurrentVideoMode().getWidth();
                int r = monitor.getViewportY();
                int s = r + monitor.getCurrentVideoMode().getHeight();
                int t = clamp(windowX, p, q);
                int u = clamp(j, p, q);
                int v = clamp(windowY, r, s);
                int w = clamp(m, r, s);
                int x = Math.max(0, u - t);
                int y = Math.max(0, w - v);
                int z = x * y;
                if (z > n) {
                    finalMonitor = monitor;
                    n = z;
                } else if (z == n && o == monitor.getHandle()) {
                    LOGGER.info("[MonitorTrackerMixin] Primary monitor {} is preferred to monitor {}", monitor, finalMonitor);
                    finalMonitor = monitor;
                }
            }
            if(this.pointerToMonitorMap.size() > 1) {
                for (Monitor monitor : this.pointerToMonitorMap.values()) {
                    if(monitor != finalMonitor){
                        return monitor;
                    }
                }
            }
            return finalMonitor;
        }
    }
}
