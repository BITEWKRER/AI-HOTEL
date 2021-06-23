package AI.hotel.service;


import AI.hotel.bean.ConsumeRecord;

import java.util.ArrayList;

public interface ConsumeRecordsService {

    ArrayList<ConsumeRecord> getAllNotPayOrders(String id_number);

    Boolean updateNotPayOrdersStatus(String id_number);

    Boolean deleteRecord(int id);

    Boolean addRecord(ConsumeRecord consumeRecord);
}
