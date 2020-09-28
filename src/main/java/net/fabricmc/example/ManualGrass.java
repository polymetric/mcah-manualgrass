package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.FernBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManualGrass implements ModInitializer {
    public static final int AXIS_POS_X = 0;
    public static final int AXIS_POS_Y = 1;
    public static final int AXIS_POS_Z = 2;
    public static final int AXIS_NEG_X = 3;
    public static final int AXIS_NEG_Y = 4;
    public static final int AXIS_NEG_Z = 5;

    public static int axis = 0;

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private static KeyBinding toggleDir;
    private static KeyBinding resetOffsets;
    private static HashMap<Position, Offset> offsetMap = new HashMap<>();
    private static ArrayList<Position> keys = new ArrayList<>();

    public static boolean accessing = false;

    @Override
    public void onInitialize() {
        toggleDir = new KeyBinding("key.manualgrass.toggleDir", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.manualgrass");
        resetOffsets = new KeyBinding("key.manualgrass.resetOffsets", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_L, "category.manualgrass");

        KeyBindingHelper.registerKeyBinding(toggleDir);
        KeyBindingHelper.registerKeyBinding(resetOffsets);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleDir.wasPressed()) {
                axis = (axis + 1) % 6;
            }
            if (resetOffsets.wasPressed()) {
                resetOffsets();
            }
        });
    }

    private static Offset getOffset(Position pos) {
        if (offsetMap.get(pos) == null) {
            return new Offset(7, 7, 7);
        }

        while(accessing) { }

        accessing = true;

        Offset o = offsetMap.get(pos);
        Offset output = o.clone();

        accessing = false;
        return output;
    }

    private static void putOffset(Position pos, Offset offset) {
        while(accessing) { }

        accessing = true;

        offsetMap.put(pos, offset);

        accessing = false;
    }

    public static void incrementOffset(Position pos) {
        Offset offset = getOffset(pos);

		switch (axis) {
			case AXIS_POS_X:
				offset.x += 1;
				break;
			case AXIS_POS_Y:
                offset.y += 1;
                break;
			case AXIS_POS_Z:
                offset.z += 1;
                break;
			case AXIS_NEG_X:
                offset.x -= 1;
                break;
			case AXIS_NEG_Y:
                offset.y -= 1;
                break;
			case AXIS_NEG_Z:
                offset.z -= 1;
                break;
		}

		offset.checkBounds();

        putOffset(pos, offset);
    }

    private static void resetOffsets() {
        while (accessing) {

        }
        offsetMap = new HashMap<>();
        reloadChunks();
    }

    public static Vec3d mcGetOffsetPos(BlockPos pos) {
        Offset offset = ManualGrass.getOffset(new Position(pos.getX(), pos.getY(), pos.getZ()));
        return offset.toVec3d();
    }

    public static void reloadChunks() {
        mc.worldRenderer.reload();
    }

    private static void writeOffsetsToFile() {
        Iterator iter = offsetMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry) iter.next();
            Position pos = (Position) item.getKey();
            Offset offs = (Offset) item.getValue();
            System.out.printf("%s %s\n", pos, offs);
        }
    }
}
