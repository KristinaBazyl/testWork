package ru.gb.provider;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.model.User;

import java.util.Optional;

@Service
public class UserProvider {
    private final WebClient webClient;

    public UserProvider(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction) {
        webClient = WebClient.builder()
                .filter(loadBalancerExchangeFilterFunction)
                .build();
    }
    public Optional<User> getUser(Long id) {
        User userProvider = webClient.get()
                .uri("http://UserService/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class)
                .block();

        return Optional.ofNullable(userProvider);
    }
}
