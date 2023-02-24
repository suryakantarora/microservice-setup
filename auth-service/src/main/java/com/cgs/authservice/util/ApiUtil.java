package com.cgs.authservice.util;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


@Service
public class ApiUtil {

	public String getDate() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String today_date = dateFormat.format(date);
		return today_date;
	}

	public String getDate(String format) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat(format);
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	public String currentDate(String pattern) {
		String date;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		date = dtf.format(now);
		return date.toUpperCase();
	}

	public String getTime() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	public String getLocaldatetime(String flag)
	{
		String datetime = null;
		String formatflag="";
		if(flag.equals("DT"))
		{
			formatflag = "ddMMyyHHmmss";
		}
		if(flag.equals("D"))
		{
			formatflag = "ddMMyy";
		}
		if(flag.equals("T"))
		{
			formatflag = "HHmmss";
		}
		DateFormat dateFormat = new SimpleDateFormat(formatflag);
		Date date = new Date();
		datetime = dateFormat.format(date); 
		return datetime;
	}

	public String cinnumberGeneratoer(String cinNo, int cinLen, String instId) {
		String CIN = "N", newcin = "N", cinmax = "X";
		char Alpha_chars[] = { 'Z', 'X', 'Y', 'W', 'V', 'I', 'J', 'L', 'U', 'H', 'O', 'M', 'N' };
		char[] copyTo = new char[2];
		Random random = new Random();
		int rannum;
		try {
			System.out.println("cin num  gen input --> "+cinNo  +" " + cinLen + " "+ instId);
			newcin = cinNumberformatter(cinLen, cinNo);
			System.out.println("newcin --> "+newcin);
			if (!(newcin.equals("N")) && !(newcin.equals("X"))) {
				cinmax = checkMaxorderrefnum(newcin, cinLen);
				if (!(cinmax.equals("X")) && !(cinmax.equals("N"))) {
					if (cinmax.equals("M")) {
						CIN = "M";
					} else {
						for (int i = 0; i <= 1; i++) {
							rannum = randomgenerator(1, 12, random);
							copyTo[i] = Alpha_chars[rannum];
						}
						CIN = instId + copyTo[0] + copyTo[1] + cinmax;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error While CIN Generation" + e.getMessage());
			CIN = "E";
		}
		return CIN;
	}

	public String cinNumberformatter(int cinlen, String cinno) {
		String cinvalue = "X";
		int newcinlen = 0;
		int curr_newcin_len = 0;
		int currcin_len = cinno.length();
		if (currcin_len == cinlen) {
			cinvalue = cinno;
		} else if (currcin_len < cinlen) {
			newcinlen = cinno.length();
			while (newcinlen != cinlen) {
				for (int j = 0; j < cinlen; j++) {
					cinno = "0" + cinno;
					curr_newcin_len = cinno.length();
					if (curr_newcin_len == cinlen) {
						break;
					}
				}
				newcinlen = cinno.length();
			}
			cinvalue = cinno;
		}
		return cinvalue;
	}

	public String checkMaxorderrefnum(String ref, int len) {
		String maxno = "X";
		String maxnum = "";
		for (int n = 0; n < len; n++) {
			maxnum = maxnum + "9";
		}
		long curr_refnum = Long.parseLong(ref);
		long maxnumber = Long.parseLong(maxnum);
		if (curr_refnum == maxnumber) {
			maxno = "M";
		} else {
			maxno = ref;
		}
		return maxno;
	}

	public int randomgenerator(int s, int e, Random ran) {
		long range = (long) e - (long) s + 1;
		long fraction = (long) (range * ran.nextDouble());
		int randomNumber = (int) (fraction + s);
		return randomNumber;
	}
	
	public String getDateTimeStamp()
	{
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_m_ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	
	
	public String genRandomNumber(int charLength  ) { 
		return String.valueOf(charLength < 1 ? 0 : new Random()
        .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
        + (int) Math.pow(10, charLength - 1));
	}
	public String refnum(){
		Calendar calendar = Calendar.getInstance();
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR); 
		int year =calendar.get(Calendar.YEAR);
		int time=calendar.get(Calendar.MINUTE);
		int hour=calendar.get(Calendar.HOUR);
		String years=Integer.toString(year);
		String dayOfYearcount=Integer.toString(dayOfYear);
		String subyr = years.substring(3,4);
		System.out.println("Substring-->"+subyr);
		System.out.println("Got date count of the year-->"+dayOfYear);
		
		System.out.println("Got day of the year-->"+year);
		System.out.println("Got todayday of the minute-->"+time);
		System.out.println("Got todayday of the hour-->"+hour);
		String ref=subyr+dayOfYearcount+hour;
		System.out.println("Got todayday of the hour-->"+ref);
		
		return ref; 
	}
	
	
	public String maskNumberstopup(String number){
		 int index = 0;
		 String mask= "####-xxxx-xxxx-x###";
		 System.out.println("Mobileno :" + number + " Mask :" + mask);
		    StringBuilder maskedNumber = new StringBuilder();
		    for (int i = 0; i < mask.length(); i++) {
		        char c = mask.charAt(i);
		        if (c == '#') {
		            maskedNumber.append(number.charAt(index));
		            index++;
		        } else if (c == 'x') {
		            maskedNumber.append(c);
		            index++;
		        } else {
		            maskedNumber.append(c);
		        }
		    }  
		    return maskedNumber.toString();
	}
	
	
	public String getDateTimeStampsms()
	{
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	public String seqnum(){
		String datepattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datepattern);
		String date = simpleDateFormat.format(new Date());
		System.out.println("Got complete date-->"+date);
		String years=date.substring(0,4);
		String month=date.substring(5,7);
		String dates=date.substring(8,10);
		System.out.println("Got Year-->"+years);
		System.out.println("Got Month-->"+month);
		System.out.println("Got date-->"+dates);
		String timepattern = "HH:mm:ss";
		SimpleDateFormat simpleDateFormate = new SimpleDateFormat(timepattern);
		String timefor = simpleDateFormate.format(new Date());
		System.out.println("Got Time Format-->"+timefor);
		String hours=timefor.substring(0,2);
		String times=timefor.substring(3,5);
		String secs=timefor.substring(6,8);
		
		System.out.println("Got todayday of the hour-->"+hours);
		System.out.println("Got todayday of the minute-->"+times);
		System.out.println("Got date count of the year-->"+secs);
		String sequence=years+month+dates+hours+times+secs;
		
		System.out.println("Got SEQUENCE->"+sequence);
		
		
		
		return sequence; 
	}

	
	public String generatedCheckDigit(String cardno) {
		int checkdigit = generateCheckDigit(cardno);
		String chkdigit = Integer.toString(checkdigit);
		return chkdigit;
	}

	public int generateCheckDigit(String s) {
		int digit = 10 - getCheckDigit(s, true) % 10;
		if (digit == 10) {
			digit = 0;
		}
		return digit;
	}

	private int getCheckDigit(String s, boolean eventposition) {
		int sum = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(s.substring(i, i + 1));
			if (eventposition) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			eventposition = !eventposition;
		}
		return sum;
	}

	public String orderreferenceno(String refnum, int ref_len) {
		String ref_num = "X";
		System.out.println("############### Refnum Recived is " + refnum + "");
		System.out.println("Refnum Len " + ref_len);
		int curr_len = refnum.length();
		if (curr_len == ref_len) {
			ref_num = refnum;
		} else if (curr_len < ref_len) {
			int refnum_len = refnum.length();
			System.out.println(" The Len of Exsist Ref num is " + refnum_len);
			int newlength;
			while (refnum_len != ref_len) {
				System.out.println("Inside While Loop " + ref_len);
				for (int j = 0; j < ref_len; j++) {
					refnum = "0" + refnum;
					newlength = refnum.length();
					if (ref_len == newlength) {
						break;
					}
				}
				System.out.println(" Ref Num Generated is " + refnum);

				refnum_len = refnum.length();
				System.out.println("refnum_len===InSide While Loop > " + refnum_len);
			}
			ref_num = refnum;
		}
		return ref_num;
	}
	
	public String getChnbreakupvalues(String prodcardtype, String attchbranch, String cardtype, String subprod,
			String productcode, String branch, String apptypeattached, String apptypevalue) {
		String breakupvalu = "";
		System.out.println("prodcardtype " + prodcardtype + "\nattchbranch " + attchbranch + "\ncardtype  " + cardtype
				+ "\nsunprod " + subprod + "\nproductcode " + productcode + "\n branch " + branch);
		if (apptypeattached != null) {
			if (apptypeattached.equals("Y")) {
				breakupvalu = breakupvalu + apptypevalue;
			}
		}
		if (prodcardtype.equals("C")) {
			breakupvalu = breakupvalu + cardtype;
			System.out.println("breakupvalu  inside C -> " + breakupvalu);
		} else if (prodcardtype.equals("P")) {
			breakupvalu = breakupvalu + subprod;
			System.out.println("breakupvalu  inside P -> " + breakupvalu);
		}
		if (attchbranch.equals("Y")) {
			breakupvalu = breakupvalu + branch;
			System.out.println("breakupvalu  inside Y -> " + breakupvalu);
		}
		System.out.println("breakupvalu   -> " + breakupvalu);
		return breakupvalu;
	}

}