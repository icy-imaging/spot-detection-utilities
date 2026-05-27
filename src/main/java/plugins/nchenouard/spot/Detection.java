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

import fr.icy.common.reflect.ClassUtil;
import fr.icy.io.xml.XMLPersistent;
import fr.icy.io.xml.XMLUtil;
import fr.icy.model.overlay.Overlay;
import fr.icy.system.logging.IcyLogger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.*;
import java.lang.reflect.Constructor;

/**
 * Detection is the basic detection class. Extends Detection to create more complete Detection.
 *
 * @author Fabrice de Chaumont
 */

public class Detection extends Overlay implements Cloneable, XMLPersistent {
    @Override
    public Object clone() throws CloneNotSupportedException {
        final Detection clone = (Detection) super.clone();

        clone.x = x;
        clone.y = y;
        clone.z = z;
        clone.t = t;
        clone.detectionType = detectionType;
        clone.selected = selected;
        clone.enabled = enabled;
        clone.color = new Color(color.getRed(), color.getGreen(), color.getBlue());
        clone.originalColor = new Color(originalColor.getRGB());

        return clone;
    }

    /**
     * x position of detection.
     */
    protected double x;
    /**
     * y position of detection.
     */
    protected double y;
    /**
     * z position of detection.
     */
    protected double z;
    /**
     * t position of detection.
     */
    protected int t;
    /**
     * Default detection type
     */
    protected int detectionType = DETECTIONTYPE_REAL_DETECTION;
    /**
     * Selected
     */
    protected boolean selected = false;
    /**
     * Detection enabled/disable is the internal mechanism to filter track with TrackProcessor. At
     * the start of the TrackProcessor process, the enable tracks are all set to true. Any TSP can then
     * disable it.
     */
    protected boolean enabled = true;

    /**
     * This color is used each time the TrackProcessor start, as it calls the detection.reset() function.
     * This color is loaded when using an XML file.
     * While saving, the current color of the track (color property) is used.
     * So at loading, it will become the new originalColor.
     */
    protected Color originalColor = Color.blue;

    public final static int DETECTIONTYPE_REAL_DETECTION = 1;
    public final static int DETECTIONTYPE_VIRTUAL_DETECTION = 2;

    public Detection(final double x, final double y, final double z, final int t) {
        super("Detection", OverlayPriority.SHAPE_NORMAL);
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
        reset();
    }

    public Detection() {
        super("Detection", OverlayPriority.SHAPE_NORMAL);
        reset();
    }

    public int getT() {
        return t;
    }

    public void setT(final int t) {
        this.t = t;
    }

    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Detection [x:" + x + " y:" + y + " z:" + z + " t:" + t + "]";
    }

    Color color;

    public int getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(final int detectionType) {
        this.detectionType = detectionType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void reset() {
        this.color = originalColor;
        this.setEnabled(true);
    }

//    @Override
//    public void paint(Graphics2D g, Sequence sequence, IcyCanvas canvas) {
//        if (enabled)
//        {
//            if (canvas instanceof Canvas2D)
//            {
//                g.setStroke(new BasicStroke(0.2f));
//                Line2D line = new Line2D.Double(x - 2, y, x + 2, y);
//                g.draw(line);
//                Line2D line2 = new Line2D.Double(x, y + 2, x, y - 2);
//                g.draw(line2);
//                g.setStroke(new BasicStroke(1f));
//            }
//        }
//    }

    /**
     * @return true if load is successful.
     */
    @Override
    public boolean loadFromXML(final Node node) {
        final Element detectionElement = (Element) node;

        x = XMLUtil.getAttributeDoubleValue(detectionElement, "x", 0);
        y = XMLUtil.getAttributeDoubleValue(detectionElement, "y", 0);
        z = XMLUtil.getAttributeDoubleValue(detectionElement, "z", 0);
        t = XMLUtil.getAttributeIntValue(detectionElement, "t", 0);
        detectionType = XMLUtil.getAttributeIntValue(detectionElement, "type", DETECTIONTYPE_REAL_DETECTION);
        selected = XMLUtil.getAttributeBooleanValue(detectionElement, "selected", false);
        originalColor = new Color(XMLUtil.getAttributeIntValue(detectionElement, "color", Color.blue.getRGB()));

        return true;
    }

    /**
     * @return true if save is successful.
     */
    @Override
    public boolean saveToXML(final Node node) {
        final Element detectionElement = (Element) node;

        XMLUtil.setAttributeDoubleValue(detectionElement, "x", x);
        XMLUtil.setAttributeDoubleValue(detectionElement, "y", y);
        XMLUtil.setAttributeDoubleValue(detectionElement, "z", z);
        XMLUtil.setAttributeIntValue(detectionElement, "t", t);
        XMLUtil.setAttributeIntValue(detectionElement, "type", detectionType);
        XMLUtil.setAttributeIntValue(detectionElement, "color", color.getRGB());

        if (selected) {
            XMLUtil.setAttributeBooleanValue(detectionElement, "selected", true);
        }

        return true;
    }

    // Inspired from the ROI Persistance mechanism
    public static Detection createDetection(final String className) {
        Detection result = null;

        try {
            // search for the specified className
            final Class<?> clazz = ClassUtil.findClass(className);

            // class found
            if (clazz != null) {
                final Class<? extends Detection> detectionClazz = clazz.asSubclass(Detection.class);

                final Constructor<? extends Detection> constructor = detectionClazz.getConstructor();
                result = constructor.newInstance();

            }
        }
        catch (final Exception e) {
            IcyLogger.error(
                    Detection.class,
                    e,
                    "TrackManager.createDetection('" + className + "', ...)",
                    "The object is maybe not compatible (should have a default constructor with no arguments and extends Detection)"
            );

            IcyLogger.warn(Detection.class, "Loading as the default Detection object.");
            // Load as a default detection.
            result = new Detection();
        }

        return result;
    }
}
