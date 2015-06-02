<?php
class SwedishSocialSecurityNumber {
       
        private $_socialSecurityNumber;
        private $_year;
        private $_month;
        private $_day;
        private $_code;
        private $_isOver100;
        
        public function __construct($socialSecurityNumber) {
                $this->_socialSecurityNumber = $socialSecurityNumber;
                $this->_isOver100 = false;
                
                if (!$this->hasTheRightPattern()) {
                        throw new \Exception("The SSN has to have the format YYNNDD-XXXX or YYMMDD+XXXX");
                }
                
                $this->_year = substr($this->_socialSecurityNumber, 0, 2);
                $this->_month = substr($this->_socialSecurityNumber, 2, 2);
                $this->_day = substr($this->_socialSecurityNumber, 4, 2);
                
                if (substr($this->_socialSecurityNumber, 6, 1) == "+") {
                        $this->_isOver100 = true;
                }
                
                $this->_code = substr($this->_socialSecurityNumber, 7, 4);
                
                if (!$this->isDateValid()) {
                        throw new \Exception("The date is invalid or is fictional");
                }
                
                if ($this->getCheckSum() != substr($this->_socialSecurityNumber, 10, 11)) {
                        throw new \Exception("The control number is not valid");
                }
                
        }
        public function getDay() {return $this->_day;}
        public function getMonth() {return $this->_month;}
        public function getYear() {return $this->_year;}
        public function getCode() {return $this->_code;}
        public function isOver100() {return $this->_isOver100;}
        public function getSocialSecurityNumber() {return $this->_socialSecurityNumber;}
        
        public function isFemale() {
                $num = intval(substr($this->_socialSecurityNumber, 9, 1));
                if (($num % 2) == 0) {
                        return true;
                }
                
                return false;
        }
        
        public function isMale() {
                return !$this->isFemale();
        }
        
        public function getCheckSum() {
                $temp = null;
                $variegated = 2;
                $list = [];
                
                for ($i = 0; $i < strlen($this->_socialSecurityNumber) - 1; $i++) {
                        if ($i == 6) {
                                continue;
                        }
                        
                        $temp = $this->_socialSecurityNumber[$i];
                        $temp = $temp * $variegated;
                        
                        $tempStr = strval($temp);
                        for ($j = 0; $j < strlen($tempStr); $j++) {
                                $list[] = intval($tempStr[$j]);
                        }
                        
                        $variegated = $variegated == 2 ? 1 : 2;
                }
                
                return (10 - (array_sum($list) % 10));
        }
        
        public function getFullYear() {
                $currentYear = date("Y");
                $retValue = null;
                
                if ($this->_year < 10) {
                        $retValue = substr($currentYear, 0, 2) . "0" . $this->_year;
                } else {
                        $retValue = substr($currentYear, 0, 2) . $this->_year;
                }
                
                if ($this->_isOver100) {
                        return $retValue - 100;
                }
                
                if ($retValue > $currentYear) {
                        return $retValue - 100;
                }
                
                return $retValue;
                
        }
        
        private function isDateValid() {
                $date = strtotime("{$this->getFullYear()}-{$this->_month}-{$this->_day}");
                $year = date('Y', $date);
                $month = date('m', $date);
                $day = date('d', $date);
                
                $currentDate = strtotime(date("Y-m-d"));
                
                if ($date > $currentDate) {
                        return false;
                }
                
                if ($this->getFullYear() == $year && $this->_month == $month && $this->_day == $day) {
                        return true;
                }
                
                return false;
        }
        
        private function hasTheRightPattern() {
                if (!preg_match("/^[0-9]{2}[0-1][0-9][0-3][0-9][-|+][0-9]{4}$/", $this->_socialSecurityNumber)) {
                        return false;
                }
                
                return true;
        }
}