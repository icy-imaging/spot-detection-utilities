/*
 * Copyright 2010, 2011 Institut Pasteur.
 * 
 * This file is part of ICY.
 * 
 * ICY is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ICY is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ICY. If not, see <http://www.gnu.org/licenses/>.
 */
package plugins.nchenouard.spot;

import javax.swing.JPanel;

/**
 * @deprecated detection editor not ready yet
 * @author Fabrice de Chaumont
 *
 */
public abstract class DetectionEditor
{

    private JPanel panel;
    //protected TrackPool trackPool = null;
    protected Detection currentDetection = null;

    public DetectionEditor()
    {
        panel = new JPanel();
    }

    public JPanel getPanel()
    {
        return panel;
    }

    public abstract Detection createDetection(double x, double y, double z, int t);

    /**
     * called by the trackManager to set a new detection when the user select a detection
     * 
     * @param detection
     */
    public final void setDetection(Detection detection)
    {
        currentDetection = detection;
        currentDetectionAsChanged();
    }

    /**
     * Send the detection to the heriting class. Should display data about it. Then use
     * trackPool.fire to update TrackEditor Interface.
     */
    protected abstract void currentDetectionAsChanged();

    /** The trackPool is set by the trackEditor constructor */
//    public final void setTrackPool(TrackPool trackPool)
//    {
//        this.trackPool = trackPool;
//    }

}
