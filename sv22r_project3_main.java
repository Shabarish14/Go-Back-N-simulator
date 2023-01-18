
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class GoBackNProtocol {

	public static void main(String arg[]) {
		int stopTime = 300;
		int pointerTime = 0;
		int ackment = 0;
		int indextime = 0;
		int delay = 3;
		int frameNumber = 0;
		int windowsize = 6;
		int windowmove = 0;
		String dataGood = "C:\\Users\\SHABARISH\\Desktop\\GoBackNProtocol\\DATA_GOOD";
		File inputFile = new File(dataGood);
		ArrayList<String> readInputData = ReadFile.readDatatxt(inputFile);
		String ackGood = "C:\\Users\\SHABARISH\\Desktop\\GoBackNProtocol\\ACK_GOOD";
		File ackinputFile = new File(ackGood);
		ArrayList<String> ack = ReadFile.readDatatxt(ackinputFile);
		int receivecount = 0;
		int lastreceiveFrame = 0;
		int indextimereceive = 0;
		int endframe = 0;
		int number = 0;
		boolean retransmission = false;
		int ackreceivecount = 0;
		boolean windowmovestatus = true;
		int acck = 0;
		
		MessagePrinter.frameSendinggotMessage(pointerTime, windowmove, windowsize, 0);
while (indextime < readInputData.size()) {
//		while (pointerTime < stopTime) {
			int chh = 0;

			// MessagePrinter.PrintMessage("_________________________________________________________________________");
			if (pointerTime > delay) {

				Sender CurrentackreceiveFrame = LocalStorage.sendingFrame.get(receivecount);

				if (CurrentackreceiveFrame.getFrameNo() != -1) {
					if (CurrentackreceiveFrame.getTransmissionStatus().equalsIgnoreCase("good")) {
						int currentlengthofframe = indextimereceive;
						String CurrentackTransmissionStatus = ReadFile
								.GetCurrentTransmissionStatus(currentlengthofframe, ack);
						if (acck == CurrentackreceiveFrame.getFrameNo()) {
							ackment = ackment + 1;
							acck = acck + 1;
						}

						int cframe = CurrentackreceiveFrame.getFrameNo();
						Receive receiveFrame = new Receive(cframe, CurrentackreceiveFrame.getWindowSize(),
								CurrentackTransmissionStatus, ackment);
						FrameReceiving.FrameAdd(receiveFrame);
						MessagePrinter.frameReceiveMessage(pointerTime, ackment, cframe, CurrentackTransmissionStatus);
						chh = chh + 1;
						// MessagePrinter.PrintMessage("Data"+CurrentackTransmissionStatus);
						indextimereceive = indextimereceive + 1;

					} else {
						int cframe = CurrentackreceiveFrame.getFrameNo();
						Receive receiveFrame = new Receive(-2, CurrentackreceiveFrame.getWindowSize(), "bad", ackment);
						FrameReceiving.FrameAdd(receiveFrame);
					}

				} else {
					int cframe = CurrentackreceiveFrame.getFrameNo();
					Receive receiveFrame = new Receive(cframe, CurrentackreceiveFrame.getWindowSize(), "bad", ackment);
					FrameReceiving.FrameAdd(receiveFrame);
				}

				receivecount = receivecount + 1;

			}
			if (pointerTime > windowsize) {

				Receive CurrentackreceiveFrame = LocalStorage.receivingFrame.get(ackreceivecount);

				int beforenum = number;
				boolean checkcon = false;
				if (beforenum == -2 && CurrentackreceiveFrame.getFrameNo() == -2) {
					checkcon = true;
				}
				number = CurrentackreceiveFrame.getFrameNo();
				

				if (CurrentackreceiveFrame.getFrameNo() >= 0 || checkcon) {

					if (lastreceiveFrame == CurrentackreceiveFrame.getFrameNo()) {

						if (CurrentackreceiveFrame.getTransmissionStatus().equalsIgnoreCase("bad")) {

							if (frameNumber == (windowmove + windowsize)) {
								number = 0;

							} else {
								number = -2;
							}

						} else {
							windowmove = CurrentackreceiveFrame.getAck();

						}
						lastreceiveFrame += 1;
						FrameInfo receiveFrame = new FrameInfo(CurrentackreceiveFrame.getFrameNo(),
								CurrentackreceiveFrame.getWindowSize(), CurrentackreceiveFrame.getAck());
						FrameSucess.FrameAdd(receiveFrame);

					}

					else if (retransmission == false && beforenum != 1) {
						retransmission = true;
						endframe = frameNumber - 1;
						frameNumber = lastreceiveFrame;
						// MessagePrinter.PrintMessage("Frame "+ frameNumber);
						number = 0;
					} else if (retransmission == false && beforenum == 1) {
						retransmission = true;
						endframe = frameNumber - 1;
						frameNumber = lastreceiveFrame + 1;

						number = 2;
					}

					else if (checkcon == true && retransmission) {
						number = 0;
					}

					if (CurrentackreceiveFrame.getTransmissionStatus().equalsIgnoreCase("good")) {
						// MessagePrinter.PrintMessage("Data"+CurrentackreceiveFrame.getFrameNo()+"...."+CurrentackreceiveFrame.getTransmissionStatus());
						windowmovestatus = true;
						MessagePrinter.frameSendinggotMessage(pointerTime, CurrentackreceiveFrame.getAck(), windowsize,
								CurrentackreceiveFrame.getAck());
						chh = chh + 1;
					}

					else {

						windowmovestatus = false;
					}
					if (retransmission == true && CurrentackreceiveFrame.getFrameNo() == frameNumber) {

						number = -2;

					}
				} else {
					if (retransmission == false && (windowmove + windowsize) < frameNumber
							&& CurrentackreceiveFrame.getFrameNo() == -1) {

						retransmission = true;
						endframe = frameNumber - 1;
						frameNumber = windowmove;
						// MessagePrinter.PrintMessage("Frame "+ frameNumber);
						number = 1;

					} else if (retransmission == false && CurrentackreceiveFrame.getFrameNo() == -2
							&& (windowmove + windowsize) == frameNumber) {
						number = 0;

					}

					else if (retransmission == true && CurrentackreceiveFrame.getFrameNo() == -2) {
						number = 0;
					}

				}

				ackreceivecount = ackreceivecount + 1;

			}

			if (number != -2 || chh == 0) {

				// MessagePrinter.PrintMessage("chh"+chh+"Data");
				String CurrentdataTransmissionStatus = ReadFile.GetCurrentTransmissionStatus(indextime, readInputData);

				int[] framewindow = { windowmove, windowmove + windowsize };
				String window = "window [" + windowmove + ", " + (windowmove + windowsize) + "]";
				// MessagePrinter.PrintMessage(frameNumber+"Data");

				if (retransmission) {
					MessagePrinter.frameReSendingMessage(pointerTime, window, frameNumber,
							CurrentdataTransmissionStatus);
					indextime = indextime + 1;
					Sender sendFrame = new Sender(frameNumber, framewindow, CurrentdataTransmissionStatus);
					FrameSending.FrameAdd(sendFrame);
					if (number == 1) {
						retransmission = false;

						frameNumber = endframe;
					} else {
						if (endframe == frameNumber) {
							retransmission = false;
							endframe = 0;

						}
					}
					frameNumber = frameNumber + 1;
				} else {
					if (chh == 0 && frameNumber > framewindow[1]) {
						
						Sender sendFrame = new Sender(-1, framewindow, "bad");
						FrameSending.FrameAdd(sendFrame);

					} else {
						
						MessagePrinter.frameSendingMessage(pointerTime, window, frameNumber,
								CurrentdataTransmissionStatus);
						indextime = indextime + 1;
						Sender sendFrame = new Sender(frameNumber, framewindow, CurrentdataTransmissionStatus);
						FrameSending.FrameAdd(sendFrame);
						frameNumber = frameNumber + 1;
					}
				}

			} else {
 
				int[] framewindow = { windowmove, windowmove + windowsize };
				Sender sendFrame = new Sender(-1, framewindow, "bad");
				FrameSending.FrameAdd(sendFrame);
				// MessagePrinter.PrintMessage(number+"Info");
			}
			// MessagePrinter.PrintMessage("_________________________________________________________________________");
			pointerTime = pointerTime + 1;

		}

	}

}
