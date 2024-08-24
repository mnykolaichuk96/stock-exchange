package pl.mnykolaichuk.trade.service;

import pl.mnykolaichuk.trade.dto.TransactionDto;

public interface ITradeService {
    void saveTransactionToDBAsync(TransactionDto transactionDto);
}
