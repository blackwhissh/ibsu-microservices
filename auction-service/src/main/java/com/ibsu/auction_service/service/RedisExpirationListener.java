package com.ibsu.auction_service.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisExpirationListener extends KeyExpirationEventMessageListener {

    private final AuctionService auctionService;

    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer, AuctionService auctionService) {
        super(listenerContainer);
        this.auctionService = auctionService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith("end_auction:")) {
            Long auctionId = Long.parseLong(expiredKey.split(":")[1]);
            auctionService.completeAuction(auctionId);
        }
    }
}
