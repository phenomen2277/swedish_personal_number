import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwedishSocialSecurityNumber {
    private Integer _year;
    private Integer _month;
    private Integer _day;
    private String _code;
    
    private Pattern _tenDigitPattern;
    private Matcher _matcher;
    private boolean _isOver100;
    
    private static final String TEN_DIGIT_PATTERN = "^[0-9]{2}[0-1][0-9][0-3][0-9][-|+][0-9]{4}$";
    
    public SwedishSocialSecurityNumber(String socialSecurityNumber)
    throws IllegalArgumentException {
        socialSecurityNumber = socialSecurityNumber.trim();
        _tenDigitPattern = Pattern.compile(TEN_DIGIT_PATTERN);
        _isOver100 = false;
        
        if (!this.isTenDigit(socialSecurityNumber))
            throw new IllegalArgumentException("The format has to be YYMMDD-XXXX or YYMMDD+XXXX");
        
        try {
            _year = Integer.parseInt(socialSecurityNumber.substring(0, 2));
            _month = Integer.parseInt(socialSecurityNumber.substring(2, 4));
            _day = Integer.parseInt(socialSecurityNumber.substring(4, 6));
            _code = socialSecurityNumber.substring(7, 11);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error when parsing date parts from the SSN given");
        }
        
        if (socialSecurityNumber.charAt(6) == '+')
            _isOver100 = true;
        
        if(!this.isDateValid(this.getYear(), _month, _day))
            throw new IllegalArgumentException("The date given is not valid or is fictional");
        
        Integer checkSum = this.getCheckSum(socialSecurityNumber);
        String lastNumber = socialSecurityNumber.substring(socialSecurityNumber
            .length() - 1);
        
        if (!checkSum.toString().equals(lastNumber)) {
            throw new IllegalArgumentException("The control number is invalid");
        }
    }
    
    public int getYear() {
        int tempInt = 0;
        
        Integer Year = Calendar.getInstance().get(Calendar.YEAR);
        
        if (_year < 10)
            tempInt = Integer.parseInt(String.format("%s0%d", Year.toString()
                .substring(0, 2), _year));
        else
            tempInt = Integer.parseInt(String.format("%s%d", Year.toString()
                .substring(0, 2), _year));
        
        if (_isOver100)
            return tempInt - 100;
        if (tempInt > Year)
            return tempInt - 100;
        
        return tempInt;
        
    }
    
    public String getMonth() {
        DateFormatSymbols dfs = new DateFormatSymbols();
        return dfs.getMonths()[_month - 1];
    }
    
    public int getMonthNumber() {
        return _month;
    }
    
    public int getDay() {
        return _day;
    }
    
    public String getCode() {
        return _code;
    }
    
    private boolean isTenDigit(String number) {
        _matcher = _tenDigitPattern.matcher(number);
        return _matcher.matches();
    }
    
    
    private Integer getCheckSum(String number) {
        int variegated = 2;
        Integer temp = 0;
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < number.length() - 1; i++) {
            if (i == 6)
                continue;
            temp = Character.getNumericValue(number.charAt(i));
            
            temp = temp * variegated;
            
            for (int j = 0; j < temp.toString().length(); j++) {
                list.add(Character.getNumericValue(temp.toString().charAt(j)));
            }
            
            variegated = variegated == 2 ? 1 : 2;
        }
        
        int sum = 0;
        Iterator<Integer> iter = list.iterator();
        while (iter.hasNext()) {
            sum = sum + iter.next();
        }
        return (10 - (sum % 10));
    }
    
    private boolean isDateValid(int year, int month, int day){
        Calendar cal = null;
        try {
            cal = new GregorianCalendar();
            cal.setLenient(false);
            cal.set(year, month - 1, day);
            cal.getTime();
        } catch (Exception e) {        
            return false;
        }
        Date currentDate = new Date();
        if(cal.getTime().compareTo(currentDate) > 0) return false;
        return true;
    }
    
}