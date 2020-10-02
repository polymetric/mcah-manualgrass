package net.nodium.mcmods.mcah_manualgrass;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class ManualGrass implements ModInitializer {
    private static final String OFFSETS_PATH = "offsets.txt";

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
    private static KeyBinding writeFile;
    private static KeyBinding readFile;
    private static HashMap<Position, Offset> offsetMap = new HashMap<>();

    public static boolean accessing = false;

    @Override
    public void onInitialize() {
        toggleDir = new KeyBinding("key.manualgrass.toggleDir", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.manualgrass");
        resetOffsets = new KeyBinding("key.manualgrass.resetOffsets", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_L, "category.manualgrass");
        writeFile = new KeyBinding("key.manualgrass.writeFile", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.manualgrass");
        readFile = new KeyBinding("key.manualgrass.readFile", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.manualgrass");

        KeyBindingHelper.registerKeyBinding(toggleDir);
        KeyBindingHelper.registerKeyBinding(resetOffsets);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleDir.wasPressed()) {
                axis = (axis + 1) % 6;
            }
            if (resetOffsets.wasPressed()) {
                resetOffsets();
                sendPlayerChatMessage("offsets reset");
            }
            if (writeFile.wasPressed()) {
                writeOffsetsToFile();
            }
            if (readFile.wasPressed()) {
                readOffsetsFromFile();
            }
        });
    }

    private static Offset getOffset(Position pos) {
        if (offsetMap.get(pos) == null) {
            return new Offset(7, 7, 7);
        }

        while (accessing) {
        }

        accessing = true;

        Offset o = offsetMap.get(pos);
        Offset output = o.clone();

        accessing = false;
        return output;
    }

    private static void putOffset(Position pos, Offset offset) {
        while (accessing) {
        }

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
        try {
            File file = new File(OFFSETS_PATH);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            while (iter.hasNext()) {
                Map.Entry item = (Map.Entry) iter.next();
                Position pos = (Position) item.getKey();
                Offset offs = (Offset) item.getValue();
                String output = String.format("%s %s\n", pos, offs);
                writer.append(output);
            }

            writer.flush();
            writer.close();

            sendPlayerChatMessage("wrote grass offsets to file");
        } catch (Exception e) {
            sendPlayerChatError("oof could not write to file");
            e.printStackTrace();
        }
    }

    private static void readOffsetsFromFile() {
        resetOffsets();

        try {
            File file = new File(OFFSETS_PATH);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<String> split = new ArrayList<String>(Arrays.asList(line.split(" ")));
                split.removeIf(item ->
                        item == null
                        || item.equals("")
                        || item.equals(" ")
                        || item.equals("\n")
                );
                Position pos = new Position(
                        Integer.parseInt(split.get(0)),
                        Integer.parseInt(split.get(1)),
                        Integer.parseInt(split.get(2))
                );
                Offset offs = new Offset(
                        Integer.parseInt(split.get(3)),
                        Integer.parseInt(split.get(4)),
                        Integer.parseInt(split.get(5))
                );

                putOffset(pos, offs);
            }

            sendPlayerChatMessage("read offsets from file");
        } catch (Exception e) {
            sendPlayerChatError("oof could not read file");
            e.printStackTrace();
        }
    }

    private static void sendPlayerChatMessage(String message) {
        mc.inGameHud.getChatHud().addMessage(new TranslatableText(message));
    }

    private static void sendPlayerChatError(String message) {
        mc.inGameHud.getChatHud().addMessage(new TranslatableText(message).formatted(Formatting.RED));
    }
}
