package Database;

/**
 * Store all the methods that a linked list can implement
 * 
 * @author DB Team
 * @version 3/26/2018
 */

public class DataBase {

	// unique identifier of each object
	private int currentKey;

	// the detectable range of each object
	private final static double range = 5;

	// the list to store all the objects
	private LinkedList<GeomObject> objectList;

	// the list to store all paintable objects
	private LinkedList<PaintableObject> paintList;

	/**
	 * Constructor for the data base class
	 */
	public DataBase() {

		// initiate the key and range
		currentKey = 1;

		// initiate the object list and the paintList
		objectList = new LinkedList<GeomObject>();
		paintList = new LinkedList<PaintableObject>();
	}

	/**
	 * Insert a new object into the object list
	 * @param object the object to be inserted
	 */
	public void insert (GeomObject object) {

		// as long as the object is not null
		if (object != null) {

			// assign a key to the object and add it to the list
			object.setKey(currentKey);
			objectList.add(objectList.size(), object);
			currentKey++;
		}
	}

	/**
	 * Remove an object from the object list
	 * @param object the object to be deleted
	 */
	public void remove (GeomObject object) {

		// as long as the object is not null
		if (object != null) {

			// find the object in the list and delete it
			objectList.delete(getObjectIdx(object.getKey()));
		}
	}

	/**
	 * Get the index of the object in the object list.
	 * @return the index of the object
	 */
	private int getObjectIdx(int key) {
		// Initialize the index
		int idx = 0;

		// loop through the object list
		for (int i =0; i < objectList.size(); i++) {
			// find the object by its key
			if (objectList.get(i).getKey() == key) {
				return idx = i;
			}
		}
		return idx;
	}

	/**
	 * Search for any object within a certain range from the selected point
	 * @param x the x coordinate of the selected point
	 * @param y the y coordinate of the selected point
	 * @return any object that is close enough to the selected point
	 */
	public GeomObject search (double x, double y) {
		// x of the point on the left side
		double left=0;
		// x of the point on the right side
		double right=0;
		// y of the point on the top
		double up=0;
		/// y of the point at the bottom
		double down=0;
		// Initialize the points for points at different positions
		Point leftP = new Point(0, 0);
		Point rightP = new Point(0, 0);
		Point upP = new Point(0, 0);
		Point downP= new Point(0, 0);

		// Go through every element in the object list
		for (int i = 0; i < objectList.size(); i++) {
			// if the object is a point
			if (objectList.get(i).getType() == 1) {
				// Get the point from the list
				Point point = (Point) objectList.get(i);

				// if x is within a certain range of a, and y is within a certain range of b
				if (x >= point.getX() - range && x <= point.getX() + range) {
					if (y >= point.getY() - range && y <= point.getY() + range) {
						return point;
					}
				}
			}

			// if the object is a line
			else if (objectList.get(i).getType() == 2) {
				// Get the line
				Line line = (Line) objectList.get(i);
				// Get start point
				Point start = (Point) objectList.get(getObjectIdx(line.getStart()));			
				// Get end point
				Point end = (Point) objectList.get(getObjectIdx(line.getEnd()));
				
				// Match start and end points to the right position
				if(start.getY()<=end.getY()) {
					up=start.getY();
					down=end.getY();
					upP = start;
					downP =end;
				}
				else if(start.getY()>end.getY()) {
					up=end.getY();
					down=start.getY();
					upP = end;
					downP =start;
				}
				
				if(start.getX()<=end.getX()) {
					left = start.getX();
					right = end.getX();
					leftP=start;
					rightP=end;
				}
				else if(start.getX()>=end.getX()) {
					left = end.getX();
					right = start.getX();
					leftP=end;
					rightP=start;
				}
				
				// Calculate the cotangent between the line and the x-axis for later use
				double angleCotangent = (Math.abs(left - right)) // absolute value of x1-x2
						/(Math.abs(down - up) //absolute value of y1-y2
								);	

				// when the line is parallel to x-axis
				if (start.getY() == end.getY()) {
					// if the distance between the selected point and the line is within the range
					if (Math.abs(y - start.getY()) <= range) {
						if(x<=right+range && x>=left-range) {
							return line;
						}
					}
				}

				// when the line is parallel to y-axis
				else if (start.getX() == end.getX()) {
					// if the distance between the selected point and the line is within the range
					if (Math.abs(x - start.getX()) <= range) {
						if(y<=down+range && y>=up-range)
							return line;
					}
				}
				
				// If the left point is lower than the right point
				else if(leftP==downP) {
					// if the distance between the selected point and the line is within the range
					if (Math.abs(right - angleCotangent*(y-up) // this is the x value
							// of the intersection point with the line if we draw a line parallel to the x-axis at the selected point
							- x) <= range) {
						return line;
					}
				}
				
				// If the left point is higher than the right point
				else if (leftP==upP) {
					// if the distance between the selected point and the line is within the range
					if (Math.abs(right - angleCotangent*(y-up) // this is the x value
							// of the intersection point with the line if we draw a line parallel to the x-axis at the selected point
							-left-(right-x)) <= range) {
						return line;
					}
				}
			}

			// if the object is a polygon
			else if (objectList.get(i).getType() == 3) {

				// one side of the polygon
				Line segment;

				/*				// loop through the points array
				for (int g = 0; g < points.length; g ++) {

					// the index of the next point in the array
					int j = g + 1;

					// when we get to the last point in the array, the index of the next connecting point is the first one
					if (g == points.length - 1) {
						j = 0;
					}

					// each side of the polygon
					segment = new Line(points[g], points[j]);

					// check whether the selected point is close enough to each side of the polygon
					if (segment.isClose(a, b)) {
						return true;
					}
				}*/
			}
		}

		return null;
	}

	/**
	 * 
	 * @return a list of paintable objects
	 */
	public LinkedList<PaintableObject> paintList() {

		// loop through the object list
		for (int i = 0; i < objectList.size(); i++) {

			// if the object is a point
			if (objectList.get(i).getType() == 1) {
				Point point = (Point) objectList.get(i);
				PaintableObject pPoint = new PaintablePoint(point.getX(), point.getY());
				paintList.add(paintList.size(), pPoint);
			}

			// if the object is a line
			else if (objectList.get(i).getType() == 2) {
				Line line = (Line) objectList.get(i);
				Point start = (Point) objectList.get(getObjectIdx(line.getStart()));
				Point end = (Point) objectList.get(getObjectIdx(line.getEnd()));
				PaintableObject pLine = new PaintableLine(start.getX(), start.getY(), end.getX(), end.getY());
				paintList.add(paintList.size(), pLine);
			}

			// if the object is a polygon
			else if (objectList.get(i).getType() == 3) {
				PaintableObject polygon = new PaintablePolygon();
				paintList.add(paintList.size(), polygon);
			}
		}
		return paintList;
	}

	/**
	 * Get the size of the object list.
	 * @return size of the objectList
	 */
	public int size() {
		return objectList.size();
	}
	
	/**
	 * A method that checks if the list is empty.
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return objectList.isEmpty();
	}
	
	public static void main(String[] args) {
		DataBase list = new DataBase();
//		//System.out.println(list.size());
//		GeomObject point5 = new Point(93, 55);
//		GeomObject point6 = new Point(93, 21);
//
//		GeomObject line2 = new Line((Point)point5, (Point)point6);
//		list.insert(point5);
//		list.insert(point6);
//		System.out.println("point5 key"+point5.getKey());
//		System.out.println("point6 key"+point6.getKey());
//		list.insert(line2);
//		GeomObject lineAcquired = list.search(93, 30);
//		System.out.println("lineAcquired.getType()"+lineAcquired.getType());
		
		GeomObject point7 = new Point(40, 10);
		GeomObject point8 = new Point(70, 20);
		list.insert(point7);
		list.insert(point8);
		GeomObject line3 = new Line((Point)point7, (Point)point8);
		list.insert(line3);
		GeomObject lineAcquired = list.search(60, 50/3);
		System.out.println("lineAcquired.getType()"+lineAcquired.getType());
		
	}
}