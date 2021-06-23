package AI.hotel.service.Impl;

import AI.hotel.bean.ConsumeRecord;
import AI.hotel.mapper.ConsumeRecordsMapper;
import AI.hotel.service.ConsumeRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class ConsumeRecordsServiceImpl implements ConsumeRecordsService {


    @Autowired
    private ConsumeRecordsMapper consumeRecordsMapper;

    @Override
    public ArrayList<ConsumeRecord> getAllNotPayOrders(String id_number) {
        return consumeRecordsMapper.getAllNotPayOrders(id_number);
    }

    @Override
    public Boolean updateNotPayOrdersStatus(String id_number) {
        return consumeRecordsMapper.updateNotPayOrdersStatus(id_number);
    }

    @Override
    public Boolean deleteRecord(int id) {
        return consumeRecordsMapper.deleteRecord(id);
    }

    @Override
    public Boolean addRecord(ConsumeRecord consumeRecord) {
        consumeRecord.setCreate_at(new Date());
        consumeRecord.setIs_pay('0');
        return consumeRecordsMapper.addRecord(consumeRecord);
    }


}
