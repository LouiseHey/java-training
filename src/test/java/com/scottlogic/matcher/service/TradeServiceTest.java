package com.scottlogic.matcher.service;

import com.scottlogic.matcher.data.TradeRepository;
import com.scottlogic.matcher.models.Trade;
import com.scottlogic.matcher.service.database.TradeDbService;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TradeServiceTest {
    private static TradeDbService tradeService;
    private static TradeRepository tradeRepository;

    @BeforeAll
    static void beforeAll() {
        tradeRepository = Mockito.mock(TradeRepository.class);
        tradeService = new TradeDbService(tradeRepository);
    }

    @Test
    void whenSaveNewOrder_orderReturned() {
        Trade trade = new Trade("buyOrderId", "sellOrderId", "buyUser", "sellUser", 10, 20);

        when(tradeRepository.insert(anyList())).thenAnswer(i -> i.getArguments()[0]);

        List<Trade> savedTrades = tradeService.saveNewTrades(List.of(trade));
        Assertions.assertEquals(1, savedTrades.size());
        Assertions.assertEquals(10, savedTrades.get(0).getPrice());
        Assertions.assertEquals(20, savedTrades.get(0).getQuantity());
        Assertions.assertEquals("buyOrderId", savedTrades.get(0).getBuyOrderId());
        Assertions.assertEquals("sellOrderId", savedTrades.get(0).getSellOrderId());
        Assertions.assertEquals("buyUser", savedTrades.get(0).getBuyUser());
        Assertions.assertEquals("sellUser", savedTrades.get(0).getSellUser());
    }
}
