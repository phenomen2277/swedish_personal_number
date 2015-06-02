require 'date.rb'

class SwedishSocialSecurityNumber
        attr_reader :socialSecurityNumber, :year, :month, :day, :code, :isOver100
        
        def initialize(socialSecurityNumber)
                @socialSecurityNumber = socialSecurityNumber
                raise ArgumentError, "The format has to be YYMMDD-XXXX or YYMMDD+XXXX" unless right_pattern?
                
                @isOver100 = false;
                @year = @socialSecurityNumber[0,2].to_i
                @month = @socialSecurityNumber[2,2].to_i
                @day = @socialSecurityNumber[4,2].to_i
                @isOver100 = true if @socialSecurityNumber[6,1] == "+"
                @code = @socialSecurityNumber[7,4].to_i
                
                raise ArgumentError, "The date is invalid or fictional" unless date_valid?
                raise ArgumentError, "The control number is invalid" unless check_sum == @socialSecurityNumber[10,1].to_i
        end
        
        def check_sum
                variegated = 2;
                temp = 0;
                list = Array.new
                
                0.upto(@socialSecurityNumber.length - 2) { |position|
                        next if position == 6
                        temp = @socialSecurityNumber[position].to_i
                        temp = temp * variegated
                        temp.to_s.split("").each { |c|
                                list.push(c.to_i)
                        }  
                        
                        variegated = variegated == 2 ? 1 : 2
                }
                
                sum = 0
                list.each { |i|
                        sum = sum + i
                }              
                
                return (10 - (sum % 10))
        end
        
        def full_year
                current_year = Time.new.year
                temp_str = ""
                if @year < 10
                        temp_str = current_year.to_s[0,2] + "0" + @year.to_s
                else
                        temp_str = current_year.to_s[0,2] + @year.to_s
                end
                
                return temp_str.to_i - 100 if @isOver100
                return temp_str.to_i - 100 if temp_str.to_i > @year
                
                return temp_str.to_i
        end
        
        def female?
                (@socialSecurityNumber[9,1].to_i % 2) == 0
        end
        
        def male?
                !female?
        end
        
        private
        def right_pattern?
                !/^[0-9]{2}[0-1][0-9][0-3][0-9][-|+][0-9]{4}$/.match(@socialSecurityNumber).nil?
        end
        
        def date_valid?
                d = nil
                begin
                        d = Date.new(full_year, @month, @day)
                rescue
                        return false
                end
                
                current_date = Time.new
                return false if d > current_date.to_date
                true
        end
end