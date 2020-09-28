package net.nodium.mcmods.mcah_manualgrass;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Temp {
    private static HashMap<Position, Offset> offsetMap = new HashMap<>();

    public static void main(String[] args) {
        Position pos1 = new Position(0, 0, 0);
        Position pos2 = new Position(5, 0, 0);

//        getOffset(pos1).x += 100;
//
//        getOffset(pos2);
//
//        writeOffsetsToFile();

//        for (int i = -32; i < 32; i++) {
//            System.out.printf("%12d %12d\n", i, Utils.wrap(i, 0, 15));
//        }

//        System.out.println(calcOffsetPos(0, 64, 0));
//        System.out.println(calcOffsetPos(1, 64, 0));
//        System.out.println(calcOffsetPos(0, 64, 1));

//        System.out.println(calcOffsetPos(0, 64, 0).toVec3d());
        System.out.println(calcOffsetPos(0, 64, 0).toVec3d_map());
    }

    public static Offset getOffset(Position pos) {
        if (offsetMap.get(pos) == null) {
            offsetMap.put(pos, new Offset(0, 0, 0));
        }
        return offsetMap.get(pos);
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

    public static Offset calcOffsetPos(int x, int y, int z) {
        return calcOffsetPos(new Position(x, y, z));
    }

    public static Offset calcOffsetPos(Position pos) {
        long l1 = (long) (pos.x * 0x2fc20f) ^ (long) pos.y * 0x6ebfff5L ^ (long) pos.z;
        l1 = l1 * l1 * 0x285b825L + l1 * 11L;

        Offset o = new Offset(0, 0, 0);

        o.x += l1 >> 16 & 15L;
        o.y += l1 >> 20 & 15L;
        o.z += l1 >> 24 & 15L;

        return o;
    }
}
