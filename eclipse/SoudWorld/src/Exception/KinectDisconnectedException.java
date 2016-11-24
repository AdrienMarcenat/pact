package Exception;

public class KinectDisconnectedException extends Exception
{
	static final long serialVersionUID = 201503101423L ;
	
	public KinectDisconnectedException()
	{
		super("Kinect disconnected");
	}
}
