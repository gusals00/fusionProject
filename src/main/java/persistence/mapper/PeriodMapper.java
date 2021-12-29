package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.dto.PeriodDTO;

import java.util.List;

public interface PeriodMapper {

    @Select("SELECT * FROM PERIOD")
    @Results(id="periodResultSet",value={
            @Result(property = "periodId",column = "period_id"),
            @Result(property = "periodName",column = "period_name"),
            @Result(property = "openTime",column = "open_time"),
            @Result(property = "closeTime",column = "close_time")
    })
    List<PeriodDTO> findAllPeriod();

//    @Select("SELECT * FROM PERIOD WHERE period_id=#{periodId} and open_time<=now() and now()<=close_time")
//    @ResultMap("periodResultSet")
//    PeriodDTO getRightPeriod(@Param("periodId") int periodId);

    @Select("SELECT count(*) FROM PERIOD WHERE period_id=#{periodId} and open_time<=now() and now()<=close_time")
    boolean isAvailablePeriod(@Param("periodId") int periodId); //수강신청 혹은 강의계획서 변경이 가능한 날짜인지

    @Select("SELECT * FROM PERIOD WHERE period_name=#{periodName}")
    @ResultMap("periodResultSet")
    PeriodDTO findPeriodByPeriodName(@Param("periodName") String periodName);

    @UpdateProvider(type = persistence.mapper.PeriodSql.class, method = "updateOpenPeriod")
    int updateOpenTime(PeriodDTO periodDTO);

    @UpdateProvider(type = persistence.mapper.PeriodSql.class, method = "updateClosePeriod")
    int updateCloseTime(PeriodDTO periodDTO);

    @UpdateProvider(type = persistence.mapper.PeriodSql.class, method = "updateAllPeriod")
    int updateTime(PeriodDTO periodDTO);
}
