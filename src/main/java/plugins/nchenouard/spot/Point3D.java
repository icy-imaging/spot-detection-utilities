package plugins.nchenouard.spot;

import java.awt.geom.Point2D;


public class Point3D {

	@Override
	public String toString() {

		return "Point3D["+x+","+y+","+z+"]";
	}

	public double x,y,z;
	
	public Point3D(){}
	
	public Point3D( double x, double y , double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D( double x, double y)
	{
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	public Point3D( double[] coord)
	{
		this.x = coord[0];
		this.y = coord[1];
		if (coord.length>2)
			this.z = coord[2];
		else this.z = 0;
	}
	
	public Point2D asPoint2D()
	{
		return new Point2D.Double( x , y );
	}
	

}
