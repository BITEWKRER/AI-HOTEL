package AI.hotel.service.Impl;


import AI.hotel.bean.DataResult;
import AI.hotel.mapper.DataDealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataDealServiceImpl {

    @Autowired
    DataDealMapper dealMapper;


    public List<DataResult> getMonRate() {
        List<DataResult> dataResultList = dealMapper.getDataResult();
        Integer sum = dealMapper.getMonCount();
        if (sum == 0) return new ArrayList<>();
        for (DataResult data : dataResultList) {
            double temp = (double) data.getCount() / sum;
            data.setRate(temp);
        }
        return dataResultList;

    }

    public List<DataResult> getRecent7Data() {
        return dealMapper.getRecent7Data();
    }

    public Integer getCountReserve() {
        return dealMapper.getCountReserve();
    }
    public Integer getCountCheckIn(){
        return dealMapper.getCountCheckIn();
    }
    public Integer getLeisure(){
        return dealMapper.getLeisure();
    }
    public Integer getClear(){
        return dealMapper.getClear();
    }
    public Integer getRepair(){
        return dealMapper.getRepair();
    }
    public Integer getDayMoney(){
        return dealMapper.getDayMoney();
    }
    public Integer getWeekMoney(){
        return dealMapper.getWeekMoney();
    }
    public Integer getMonMoney(){
        return dealMapper.getMonthMoney();
    }






}
