package plugins.nchenouard.spot;

import icy.plugin.abstract_.Plugin;
import icy.sequence.Sequence;

import java.util.TreeMap;
import java.util.Vector;


/** A class to manage detection results
 * 
 * @author nicolas chenouard and Fabrice de Chaumont
 *
 * */

public class DetectionResult extends Plugin
{
	private TreeMap<Integer, Vector<Spot>> results;
	private Sequence sourceSequence;
	private boolean numberOfDetectionShouldBeRecomputed = true;
	private int detectionNumber = 0;
	private String name = null;
	
	@Override
	public String toString() {
		if (name == null)
		{
			if (!results.isEmpty())
				return "Detection set from t:" + getFirstFrameTime() + " to t:" + getLastFrameTime() + " #detection(s): " + getNumberOfDetection();
			else
				return  "Detection set (empty)";
		}
		else
			return name;
	}
	
	public int getNumberOfDetection()
	{
		if ( numberOfDetectionShouldBeRecomputed )
		{
			computeNumberOfDetection();
		}
		return detectionNumber;
	}
	
	private void computeNumberOfDetection() {
		
		detectionNumber = 0;
		for ( int index : results.keySet() )
		{
			detectionNumber+=results.get( index ).size();
		}
		numberOfDetectionShouldBeRecomputed = false;

	}

	public Sequence getSequence()
	{
		return sourceSequence;
	}
	
	public void setSequence(Sequence seq)
	{
		this.sourceSequence = seq;
	}
	
	protected void finalize() throws Throwable {
		results.clear();
		super.finalize();
	}
	
	public DetectionResult()
	{
		results = new TreeMap<Integer, Vector<Spot>>();
		numberOfDetectionShouldBeRecomputed = true;
	}
	
	public DetectionResult(TreeMap<Integer, Vector<Spot>> results)
	{
		this.results = results;
		numberOfDetectionShouldBeRecomputed = true;
	}
	
	public void addDetection(int t, Spot s)
	{
		Vector<Spot> spots;
		if (results.containsKey(Integer.valueOf(t)))
			spots = results.get(Integer.valueOf(t));
		else
		{
			spots = new Vector<Spot>(1);
			results.put(Integer.valueOf(t), spots);
		}
		spots.add(s);
		numberOfDetectionShouldBeRecomputed = true;
	}
	
	public void setResult(int t, Vector<Spot> detections)
	{
		results.put(Integer.valueOf(t), new Vector<Spot>(detections) );
		numberOfDetectionShouldBeRecomputed = true;
	}
	
	/**
	 * 
	 * @return a COPY of the detection vector at time t
	 */
	public Vector<Spot> getDetectionsAtT(int t)
	{
		Vector<Spot> spots = results.get(t);
		if (spots == null){
			spots = new Vector<Spot>();
			results.put(Integer.valueOf(t), spots);
		}
		return spots;
	}
	
	/**
	 * 
	 * @return a Copy of the TreeMap.
	 */
	public TreeMap<Integer, Vector<Spot>> getResults()
	{
		return new TreeMap<Integer, Vector<Spot>>(results);
	}

	public DetectionResult copy()
	{
		TreeMap<Integer, Vector<Spot>> copy = new TreeMap<Integer, Vector<Spot>>();
		for ( int key : results.keySet() )
		{
			Vector<Spot> spotVectorClone = (Vector<Spot>) results.get( key ).clone();
			copy.put( key , spotVectorClone );
		}		
		DetectionResult dr = new DetectionResult(copy);
		dr.setName(name);
		return dr;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getFirstFrameTime()
	{
		return results.firstKey();
	}
	
	public int getLastFrameTime()
	{
		return results.lastKey();

	}
}
