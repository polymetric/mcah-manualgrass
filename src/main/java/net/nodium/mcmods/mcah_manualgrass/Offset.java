package net.nodium.mcmods.mcah_manualgrass;

import net.minecraft.util.math.Vec3d;

public class Offset {
    public int x;
    public int y;
    public int z;

    public Offset(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("%12d %12d %12d", x, y, z);
    }

    public Vec3d toVec3d() {
//        double x = Utils.map(this.x, 0, 15, -0.25, 0.25);
//        double z = Utils.map(this.z, 0, 15, -0.25, 0.25);
//
//        double y = Utils.map(this.y, 0, 15, -0.2, 0);

        double x = ((this.x / 15F) - 0.5D) * 0.5D;
        double z = ((this.z / 15F) - 0.5D) * 0.5D;

        double y = ((this.y / 15F) - 1.0D) * 0.2D;

        return new Vec3d(x, y, z);
    }

    public Vec3d toVec3d_map() {
        double x = Utils.map(this.x, 0, 15, -0.25, 0.25);
        double z = Utils.map(this.z, 0, 15, -0.25, 0.25);

        double y = Utils.map(this.y, 0, 15, -0.2, 0);

//        double x = ((this.x / 15F) - 0.5D) * 0.5D;
//        double z = ((this.z / 15F) - 0.5D) * 0.5D;
//
//        double y = ((this.y / 15F) - 1.0D) * 0.2D;

        return new Vec3d(x, y, z);
    }

    public Offset clone() {
        return new Offset(x, y, z);
    }

    public void checkBounds() {
        x = Utils.wrap(x, 0, 15);
        y = Utils.wrap(y, 0, 15);
        z = Utils.wrap(z, 0, 15);
    }
}
