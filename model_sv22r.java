import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
class ReadFile {
	public static ArrayList<String> readDatatxt(File file) {

		try {
			File filePath = new File(file.getPath());
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			ArrayList<String> records = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				
				records.add(line);
			}
			br.close();
			return records;
		} catch (IOException ie) {
			System.out.println(ie);
		} catch (Exception ie) {
			System.out.println(ie);
		}
		return null;

	}
	public static String GetCurrentTransmissionStatus(int index,ArrayList<String> readInputData)
	{
		String dataTransmission = "bad";
		String transmissionStatus = readInputData.get(index);
		if (transmissionStatus.equalsIgnoreCase("1")) {
			dataTransmission = "good";
		} 
		return dataTransmission;
	}
}


class MessagePrinter {
	

	public static void PrintMessage(String message)
	{
		System.out.println(message);
	}
	
	
	public static void frameSendingMessage(int pointerTime,String window, int frameNumber, String dataTransmission  )
	{
		String message = String.format("time : %d sender %s transmitting new frame %d, %s transmission",
				pointerTime, window, frameNumber, dataTransmission);

		MessagePrinter.PrintMessage(message);
	}
	
	public static void frameReceiveMessage(int pointerTime,int lastAck, int frameNumber, String dataTransmission  )
	{

		String mm = String.format(
				"time : %d receiver got frame %d, transmitting ACK%d, %s transmission", pointerTime,
				frameNumber, lastAck, dataTransmission);
		MessagePrinter.PrintMessage(mm);
		
	}
	
	public static void frameReSendingMessage(int pointerTime,String window, int frameNumber, String dataTransmission  )
	{
		String message = String.format("time : %d sender %s retransmitting old frame %d, %s transmission",
				pointerTime, window, frameNumber, dataTransmission);
		
		MessagePrinter.PrintMessage(message);
	}
	
	
	
	public static void frameSendinggotMessage(int pointerTime,int windowmove, int windowsize,int ack  )
	{
		String window = "window [" + windowmove + ", " + (windowmove + windowsize) + "]";
		String mm = String.format("time : %d sender got ACK%d, %s", pointerTime, ack, window);
		MessagePrinter.PrintMessage(mm);
		
	}
}



 class FrameInfo extends TransmissionBase{
	int Ack;

	public FrameInfo(int frameNo, int[] windowSize, int ack) {
		super(frameNo, windowSize);
		Ack = ack;
	}

	public int getAck() {
		return Ack;
	}

	public void setAck(int ack) {
		Ack = ack;
	}
	
}




 abstract class FrameSendingBase extends TransmissionBase  {
	String TransmissionStatus;

	public FrameSendingBase(int frameNo, int[] windowSize, String transmissionStatus) {
		super(frameNo, windowSize);
		TransmissionStatus = transmissionStatus;
	}

	public String getTransmissionStatus() {
		return TransmissionStatus;
	}

	public void setTransmissionStatus(String transmissionStatus) {
		TransmissionStatus = transmissionStatus;
	}

	

}
 class Receive extends FrameSendingBase {
	int Ack;

	public Receive(int frameNo, int[] windowSize, String transmissionStatus, int ack) {
		super(frameNo, windowSize, transmissionStatus);
		Ack = ack;
	}

	public int getAck() {
		return Ack;
	}

	public void setAck(int ack) {
		Ack = ack;
	}

}
 class Sender extends FrameSendingBase {

	public Sender(int frameNo, int[] windowSize, String transmissionStatus) {
		super(frameNo, windowSize, transmissionStatus);
	}

}
  class TransmissionBase {
	int FrameNo;
	int[] windowSize;

	public TransmissionBase(int frameNo, int[] windowSize) {
		super();
		FrameNo = frameNo;
		this.windowSize = windowSize;
	}

	public int getFrameNo() {
		return FrameNo;
	}

	public void setFrameNo(int frameNo) {
		FrameNo = frameNo;
	}

	public int[] getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int[] windowSize) {
		this.windowSize = windowSize;
	}

}

   class LocalStorage {
		
		public static ArrayList<Sender> sendingFrame=new ArrayList<Sender>();
		public static ArrayList<Receive> receivingFrame=new ArrayList<Receive>();
		public static ArrayList<FrameInfo> frameInfoList=new ArrayList<FrameInfo>();
		

	}
   
   class FrameReceiving {

		public static  void FrameAdd(Receive frame) {
			LocalStorage.receivingFrame.add(frame);
		}

	}
   class FrameSending {
		
		public static void FrameAdd(Sender frame) {
			LocalStorage.sendingFrame.add(frame);
		}

	}
   class FrameSucess {

		public static void FrameAdd(FrameInfo frame) {
			LocalStorage.frameInfoList.add(frame);
		}

	}


