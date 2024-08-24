package pl.mnykolaichuk.trade.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.mnykolaichuk.trade.dto.TransactionDto;
import pl.mnykolaichuk.trade.entity.Transaction;
import pl.mnykolaichuk.trade.mapper.TransactionMapper;
import pl.mnykolaichuk.trade.repository.TransactionRepository;
import pl.mnykolaichuk.trade.service.ITradeService;

@Service
@AllArgsConstructor
public class TradeService implements ITradeService {

    private TransactionRepository transactionRepository;
    @Override
    @Async
    public void saveTransactionToDBAsync(TransactionDto transactionDto) {

        Transaction transaction = TransactionMapper.mapToTransaction(transactionDto, new Transaction());

        transactionRepository.save(transaction);

    }
}
