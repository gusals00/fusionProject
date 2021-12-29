package persistence.mapper;

import org.apache.ibatis.jdbc.SQL;
import persistence.dto.PeriodDTO;

public class PeriodSql {

    public String updateOpenPeriod(PeriodDTO periodDTO){
        SQL sql=new SQL(){{
            UPDATE("PERIOD");
            SET("open_time = #{openTime}");
            WHERE("period_id=#{periodId}");
        }};

        return sql.toString();
    }

    public String updateClosePeriod(PeriodDTO periodDTO){
        SQL sql=new SQL(){{
            UPDATE("PERIOD");
            SET("close_time = #{closeTime}");
            WHERE("period_id=#{periodId}");
        }};

        return sql.toString();
    }

    public String updateAllPeriod(PeriodDTO periodDTO){
        SQL sql = new SQL(){{
            UPDATE("PERIOD");
            SET("open_time = #{openTime}, close_time = #{closeTime}");
            WHERE("period_id = #{periodId}");
        }};

        return sql.toString();
    }
}
