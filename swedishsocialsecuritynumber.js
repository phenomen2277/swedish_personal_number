function SwedishSocialSecurityNumber(socialSecurityNumber){
        var _isOver100 = false;
        var _socialSecurityNumber = socialSecurityNumber;
        
        
        if(!_hasTheRightPattern())
                throw new Error('The SSN has to have the format YYMMDD-XXXX or YYMMDD+XXXX');
        
        var _year = parseInt(_socialSecurityNumber.substring(0,2));
        var _month = parseInt(_socialSecurityNumber.substring(2,4));
        var _day = parseInt(_socialSecurityNumber.substring(4,6));
        var _code = parseInt(_socialSecurityNumber.substring(7,11));
        
        if(_socialSecurityNumber.substring(6, 7) == '+') _isOver100 = true;
        
        if(!_isDateValid(_getFullYear(), _month, _day))
                throw new Error('The given date is not valid or is fictional');
        
        if(_getCheckSum() != _socialSecurityNumber.substring(10,11))
                throw new Error("The control number is invalid");
        
        this.socialSecurityNumber = function() {
                return _socialSecurityNumber;
        };
        
        function _getFullYear(){
                var currentYear = new Date().getFullYear();
                var ret = 0;
                
                if(_year < 10){
                        ret = currentYear.toString().substring(0,2) + "0" + _year.toString();
                } else {
                        ret = currentYear.toString().substring(0,2) +  _year.toString();
                }
                
                if(_isOver100) return parseInt(ret) - 100;
                if( parseInt(ret) > currentYear ) return parseInt(ret) - 100;
                
                return(parseInt(ret));
        }
        
        function _getCheckSum(){
                var variegated = 2;
                var temp = 0;
                var list = [];
                
                for(i = 0; i < _socialSecurityNumber.length - 1; i++){
                        if( i == 6) continue;
                        
                        temp = parseInt(_socialSecurityNumber[i]);
                        temp = temp * variegated;
                        tempStr = temp.toString();
                        for(j = 0; j < tempStr.length; j++) {
                                list.push(parseInt(tempStr[j]));
                        }
                        
                        variegated = variegated == 2 ? 1 : 2;
                        temp = 0;
                }
                
                var sum = 0;
                for(i = 0; i < list.length; i++) sum = sum + list[i];
                       
                        return(10 - (sum % 10));
        }
        
        function _hasTheRightPattern(){
                var pattern = /^[0-9]{2}[0-1][0-9][0-3][0-9][-|+][0-9]{4}$/
                return pattern.test(_socialSecurityNumber);
        }
        
        function _isDateValid(){
                var d = new Date(_getFullYear(), _month - 1, _day);
                if(isNaN(d)) return false;
                
                var currentDate = new Date();
                if(d > currentDate) return false;
                if(d.getFullYear() == _getFullYear() && (d.getMonth() + 1) == _month && d.getDate() == _day) return true;
                return false;
        }
        
        this.getFullYear = function() {
                return _getFullYear();
        };
        
        this.isFemale = function() {
                if((parseInt(this.socialSecurityNumber().substring(9, 10)) % 2) === 0) return true;
                return false;
        };
        
        this.isMale = function() {
                return !this.isFemale();
        };
        
        this.getYear = function() {
                return _year;
        };
        
        this.getMonth = function(){
                return _month;
        };
        
        this.getDay = function() {
                return _day;
        };
        
        this.getCode = function() {
                return _code;
        };
}