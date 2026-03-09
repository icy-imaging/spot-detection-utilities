/*
 * Copyright (c) 2010-2026. Institut Pasteur.
 *
 * This file is part of Icy.
 * Icy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Icy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Icy. If not, see <https://www.gnu.org/licenses/>.
 */

package plugins.nchenouard.spot;

import org.bioimageanalysis.icy.system.logging.IcyLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class Spot {
    public Point3D mass_center;
    public double minIntensity;
    public double maxIntensity;
    public double meanIntensity;

    /**
     * List of detection points
     */
    public ArrayList<Point3D> point3DList = new ArrayList<>();

    public Spot() {
        mass_center = new Point3D();
    }

    public Spot(final double x, final double y, final double z) {
        mass_center = new Point3D(x, y, z);
    }

    public Spot(final double x, final double y, final double z, final double minIntensity, final double maxIntensity, final double meanIntensity) {
        mass_center = new Point3D(x, y, z);
        this.minIntensity = minIntensity;
        this.maxIntensity = maxIntensity;
        this.meanIntensity = meanIntensity;
    }

    public Spot(final double x, final double y, final double z, final double minIntensity, final double maxIntensity, final double meanIntensity, final ArrayList<Point3D> point3DList) {
        mass_center = new Point3D(x, y, z);
        this.minIntensity = minIntensity;
        this.maxIntensity = maxIntensity;
        this.meanIntensity = meanIntensity;
        this.point3DList = new ArrayList<>(point3DList);
    }

    public static @Nullable Spot load(@NotNull String line) {
        if (line.startsWith("detection")) {
            String[] tmpTab = line.split("\\[");
            tmpTab = tmpTab[1].split("]");
            line = tmpTab[0];
            final String[] coordinates = line.split(",", 3);
            if (coordinates.length > 2) {
                final NumberFormat nf = NumberFormat.getInstance();
                final Spot s = new Spot();
                try {
                    s.mass_center.x = nf.parse(coordinates[0]).intValue();
                    s.mass_center.y = nf.parse(coordinates[1]).intValue();
                    s.mass_center.z = nf.parse(coordinates[2]).intValue();
                }
                catch (final ParseException e) {
                    IcyLogger.error(Spot.class, e);
                    return null;
                }
                return s;
            }
        }
        return null;
    }

    public void save(final @NotNull PrintStream printOut, final int num) {
        printOut.print("detection{" + num + "} = [");
        printOut.print(mass_center.x + ",");
        printOut.print(mass_center.y + ",");
        printOut.print(mass_center.z + "];");
    }

    @Override
    public String toString() {
        return "Spot{" + "mass_center=" + mass_center +
                ", minIntensity=" + minIntensity +
                ", maxIntensity=" + maxIntensity +
                ", meanIntensity=" + meanIntensity +
                ", point3DList=" + point3DList +
                '}';
    }
}
