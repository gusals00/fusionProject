package service;

import persistence.dao.PeriodDAO;
import persistence.dto.PeriodDTO;

public class PeriodService {
    private final PeriodDAO periodDAO;

    public PeriodService(PeriodDAO periodDAO){
        this.periodDAO = periodDAO;
    }

    public boolean isAvailableRegister(int period){// 현재 시간이 해당 기간에 포함되는지 확인
        return periodDAO.isAvailableRegister(period);
    }

    public synchronized int updateAllTime(PeriodDTO periodDTO){//시작시간,종료시간 변경
        return periodDAO.updateAllTime(periodDTO);
    }
}
