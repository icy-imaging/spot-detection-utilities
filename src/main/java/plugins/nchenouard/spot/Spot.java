package plugins.nchenouard.spot;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class Spot {
	
	public Point3D mass_center;
	public double minIntensity;
	public double maxIntensity;
	public double meanIntensity;
	
	/** List of point of the detection */
	public ArrayList<Point3D> point3DList = new ArrayList<Point3D>();
	
	public Spot()
	{
		mass_center = new Point3D();
	}

	public Spot( double x , double y , double z )
	{
		mass_center = new Point3D( x , y , z );
	}

	public Spot( double x , double y , double z , double minIntensity , double maxIntensity, double meanIntensity )
	{
		mass_center = new Point3D( x , y , z );
		this.minIntensity = minIntensity;
		this.maxIntensity = maxIntensity;
		this.meanIntensity = meanIntensity;
	}

	public Spot( double x , double y , double z , double minIntensity , double maxIntensity, double meanIntensity , ArrayList<Point3D> point3DList )
	{
		mass_center = new Point3D( x , y , z );
		this.minIntensity = minIntensity;
		this.maxIntensity = maxIntensity;
		this.meanIntensity = meanIntensity;
		this.point3DList = new ArrayList<Point3D>( point3DList );
	}
	
	@Override
	protected void finalize() throws Throwable
	{		
		mass_center = null;
		super.finalize();
	}
		
	public static Spot load(String line)
	{
		if (line.startsWith("detection"))
		{
			String[] tmpTab = line.split("\\[");
			tmpTab = tmpTab[1].split("]");
			line = tmpTab[0];
			String[] coordinates = line.split(",",3);
			if (coordinates.length>2)
			{
				NumberFormat nf = NumberFormat.getInstance();
				Spot s = new Spot();
				try {
					s.mass_center.x = nf.parse(coordinates[0]).intValue();
					s.mass_center.y = nf.parse(coordinates[1]).intValue();
					s.mass_center.z = nf.parse(coordinates[2]).intValue();
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
				return s;
			}
			return null;
		}
		else return null;
	}
	
	public void save(PrintStream printOut, int num)
	{
		printOut.print("detection{"+num+"} = [");
		printOut.print ( mass_center.x +"," );
		printOut.print ( mass_center.y +"," );
		printOut.print ( mass_center.z +"];" );
	}
}
