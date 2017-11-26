package com.example.shefaliupadhyaya.finalpathdetection;

public class MapperClass {
	public String DirectionInString(int dir){
		switch(dir){
			case 1:
				return "East";
			case 2:
				return "North East";
			case 3:
				return "North";
			case 4:
				return "North West";	
			case 5:
				return "West";	
			case 6:
				return "South West";
			case 7:
				return "South";
			case 8:
				return "South East";
		}
		return null;
	}
	public String NodeInString(int dir){
		switch(dir){
			case 0:
				return "main gate";
			case 4:
				return "reception";
			case 5:
				return "girls hostel";
			case 7:
				return "canteen";
		}
		return null;
	}
	public int NodeInInteger(String str){
		switch(str){
		case "main gate":
			return 0;
		case "reception":
			return 4;
		case "girls hostel":
			return 5;
		case "canteen":
			return 7;
		}
		return -1;
	}
}
