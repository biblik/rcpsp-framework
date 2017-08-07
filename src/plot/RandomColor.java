package plot;

import java.awt.Color;
import java.util.Random;

public class RandomColor
{

	public static Color getRandomColor()
	{

		String hex1 = null, hex2 = null, hex3 = null, hex4 = null, hex5 = null, hex6 = null;
		double sum = 0;
		while (sum < 128) {
			hex1 = getRandomHex();
			hex2 = getRandomHex();
			hex3 = getRandomHex();
			hex4 = getRandomHex();
			hex5 = getRandomHex();
			hex6 = getRandomHex();

			sum = 0.2126*(float)getIntValue(hex1, hex2) + 0.7152*(float)getIntValue(hex3, hex4) + 0.0722*(float)getIntValue(hex5, hex6);
		}

		return new Color(getIntValue(hex1, hex2),getIntValue(hex3, hex4),getIntValue(hex5, hex6));
	}

	private static String getRandomHex()
	{
		String[] hex = new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
		};
		return hex[new Random().nextInt(hex.length)];
	} 

	private static int getIntValue(String hex1, String hex2)
	{
		return getIntValue(hex1) * 16 + getIntValue(hex2);
	}

	private static int getIntValue(String hexNumber)
	{

		if (hexNumber.equals("0"))
			return 0;
		if (hexNumber.equals("1"))
			return 1;
		if (hexNumber.equals("2"))
			return 2;
		if (hexNumber.equals("3"))
			return 3;
		if (hexNumber.equals("4"))
			return 4;
		if (hexNumber.equals("5"))
			return 5;
		if (hexNumber.equals("6"))
			return 6;
		if (hexNumber.equals("7"))
			return 7;
		if (hexNumber.equals("8"))
			return 8;
		if (hexNumber.equals("9"))
			return 9;
		if (hexNumber.equals("A"))
			return 10;
		if (hexNumber.equals("B"))
			return 11;
		if (hexNumber.equals("C"))
			return 12;
		if (hexNumber.equals("D"))
			return 13;
		if (hexNumber.equals("E"))
			return 14;
		if (hexNumber.equals("F"))
			return 15;

		return -1;

	}
}
