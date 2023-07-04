/*
 * Copyright (c) 2010-2023. Institut Pasteur.
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

import icy.plugin.abstract_.Plugin;
import icy.sequence.Sequence;

import java.util.TreeMap;
import java.util.Vector;


/**
 * A class to manage detection results
 *
 * @author nicolas chenouard and Fabrice de Chaumont
 */

public class DetectionResult extends Plugin {
    private final TreeMap<Integer, Vector<Spot>> results;
    private Sequence sourceSequence;
    private boolean numberOfDetectionShouldBeRecomputed;
    private int detectionNumber = 0;
    private String name = null;

    @Override
    public String toString() {
        if (name == null) {
            if (!results.isEmpty())
                return "Detection set from t:" + getFirstFrameTime() + " to t:" + getLastFrameTime() + " #detection(s): " + getNumberOfDetection();
            else
                return "Detection set (empty)";
        }
        else
            return name;
    }

    public int getNumberOfDetection() {
        if (numberOfDetectionShouldBeRecomputed) {
            computeNumberOfDetection();
        }
        return detectionNumber;
    }

    private void computeNumberOfDetection() {

        detectionNumber = 0;
        for (final int index : results.keySet()) {
            detectionNumber += results.get(index).size();
        }
        numberOfDetectionShouldBeRecomputed = false;

    }

    public Sequence getSequence() {
        return sourceSequence;
    }

    public void setSequence(final Sequence seq) {
        this.sourceSequence = seq;
    }

    @Override
    protected void finalize() throws Throwable {
        results.clear();
        super.finalize();
    }

    public DetectionResult() {
        results = new TreeMap<>();
        numberOfDetectionShouldBeRecomputed = true;
    }

    public DetectionResult(final TreeMap<Integer, Vector<Spot>> results) {
        this.results = results;
        numberOfDetectionShouldBeRecomputed = true;
    }

    public void addDetection(final int t, final Spot s) {
        final Vector<Spot> spots;
        if (results.containsKey(t))
            spots = results.get(t);
        else {
            spots = new Vector<>(1);
            results.put(t, spots);
        }
        spots.add(s);
        numberOfDetectionShouldBeRecomputed = true;
    }

    public void setResult(final int t, final Vector<Spot> detections) {
        results.put(t, new Vector<>(detections));
        numberOfDetectionShouldBeRecomputed = true;
    }

    /**
     * @return a COPY of the detection vector at time t
     */
    public Vector<Spot> getDetectionsAtT(final int t) {
        return results.computeIfAbsent(t, k -> new Vector<>());
    }

    /**
     * @return a Copy of the TreeMap.
     */
    public TreeMap<Integer, Vector<Spot>> getResults() {
        return new TreeMap<>(results);
    }

    public DetectionResult copy() {
        final TreeMap<Integer, Vector<Spot>> copy = new TreeMap<>();
        for (final int key : results.keySet()) {
            @SuppressWarnings("unchecked") final Vector<Spot> spotVectorClone = (Vector<Spot>) results.get(key).clone();
            copy.put(key, spotVectorClone);
        }
        final DetectionResult dr = new DetectionResult(copy);
        dr.setName(name);
        return dr;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getFirstFrameTime() {
        return results.firstKey();
    }

    public int getLastFrameTime() {
        return results.lastKey();
    }
}
