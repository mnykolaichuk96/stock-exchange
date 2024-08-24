package pl.mnykolaichuk.trade.mapper;

import pl.mnykolaichuk.trade.dto.TransactionDto;
import pl.mnykolaichuk.trade.entity.Transaction;

public class TransactionMapper {
    public static Transaction mapToTransaction(TransactionDto transactionDto, Transaction transaction) {
        transaction.setBuyOfferId(transactionDto.getBuyOfferId());
        transaction.setSellOfferId(transactionDto.getSellOfferId());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setPrice(transactionDto.getPrice());
        transaction.setTimestamp(transactionDto.getTimestamp());

        return transaction;
    }
}
