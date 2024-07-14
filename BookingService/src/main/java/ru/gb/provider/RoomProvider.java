package ru.gb.provider;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.model.Room;

import java.util.Optional;

@Service
public class RoomProvider {
    private final WebClient webClient;

    public RoomProvider(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction) {
        webClient = WebClient.builder()
                .filter(loadBalancerExchangeFilterFunction)
                .build();
    }
    public Optional<Room> getRoom(Long id) {
        Room roomProvider = webClient.get()
                .uri("http://RoomService/rooms/{id}", id)
                .retrieve()
                .bodyToMono(Room.class)
                .block();

        return Optional.ofNullable(roomProvider);
    }
}
