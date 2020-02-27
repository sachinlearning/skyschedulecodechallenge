package com.skyschedule.run;

import java.util.Scanner;

import com.skyschedule.read.FileAction;
import com.skyschedule.runway.TrackRunway;

//The code starts from here
public class Start {

	private static TrackRunway trackRunway = null;
	private static FileAction fileAction = null;

	public static void main(String[] args) {
		init();
		Scanner in = null;
		int activityNumber = 0;
		System.out.println("Welcome");
		
		// providing menu options
		do {
			in = new Scanner(System.in);
			try {
				System.out.println("Select number according to the  activity you need to perform");
				System.out.println("1. For checking free runway currently");
				System.out.println("2. For allotting runway to the flights");
				System.out.println("3. For checking alloted time to runways");
				System.out.println("4. Exit");
				activityNumber = in.nextInt();

				switch (activityNumber) {
				case 1:
					System.out.println(trackRunway.checkAvailableRunway());
					break;
				case 2:
					System.out.println(fileAction.readExcelFile(activityNumber,trackRunway));
					break;
				case 3:
					System.out.println(trackRunway.writingAllotedRunWayData());
					break;
				case 4:
					System.out.println("Thank you");
					in.close();
					System.exit(0);
					break;
				default:
					System.out.println("Please select a value between 1 to 5");
					activityNumber = 1;
					break;
				}
			} catch (Exception e) {
				System.out.println("Kindly provide a number between 1 to 4 ");
				activityNumber = 1;
			}
		} while (activityNumber > 0 && activityNumber < 4);

	}

	//initializing the required values
	public static void init() {
		trackRunway = new TrackRunway();
		trackRunway.init();
		fileAction = new FileAction();
	}
}
