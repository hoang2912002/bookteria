package com.hamet.api_gateway.configuration;

import javax.swing.Spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.hamet.api_gateway.repository.httpClient.IdentityClient;

@Configuration
public class WebClientConfiguration {
    
    /**
     * @Spring WebClient + HTTP Interface (Spring 6) để tạo client gọi REST service khác (thay cho FeignClient)
     * 1. Mục đích tổng thể
     *      👉 Tạo IdentityClient (interface) để gọi API của service Identity
     *      👉 Dùng WebClient làm HTTP engine
     *      👉 Không cần viết implementation thủ công
     *      IdentityClient (interface)
                ↓
            HttpServiceProxyFactory
                ↓
            WebClient
                ↓
            HTTP request → http://localhost:8080/identity
     * 2. Bean WebClient
     *      Tạo WebClient dùng chung baseUrl = URL gốc của service Identity
     *      WebClient đóng vai trò là như HTTP. Nó kế thừa toàn bộ sức mạnh của Reactor Netty (Non-blocking).
     */

    @Bean
    WebClient webClient(){
        return WebClient.builder()
            .baseUrl("http://localhost:8080/identity")
            .build();
    }

    @Bean
    // Việc tạo Http từ webclient là nói với Spring: hãy tạo implementation runtime cho interface này
    /**
     * [1]. Chính xác những gì xảy ra:
        1️⃣ Bạn truyền WebClient
        2️⃣ Spring wrap nó bằng WebClientAdapter
        3️⃣ HttpServiceProxyFactory:
     *      Đọc annotation @PostExchange
     *      Tạo JDK Dynamic Proxy
     *      Bind method → HTTP call
     *  4️⃣ Trả về object implement IdentityClient
     * 
     * [2]. Tóm lại
     *  API Gateway dùng Spring Cloud Gateway nên toàn bộ pipeline là Reactive / Non-blocking.
     *  Vì cần call sang Identity Service để introspect token mà vẫn giữ non-blocking, nên mình dùng Declarative HTTP Interface của Spring 6 thay cho OpenFeign.
     *  Do Declarative HTTP Interface không auto tạo proxy, mình phải cấu hình HttpServiceProxyFactory để bind WebClient làm HTTP engine và tạo dynamic proxy cho IdentityClient.
     *  Client này được sử dụng trong GlobalFilter với Ordered để đảm bảo auth chạy đúng thứ tự trong filter chain, và toàn bộ flow trả về Mono<Void> để không block event-loop.
     * 
     * [3]. Mẹo
     *  Dấu hiệu 1: Interface trả về Mono<T> hoặc Flux<T>. => WebClientAdapter trong Configuration vì đây là luồng Non-blocking.
     *  Dấu hiệu 2: Interface trả về kiểu dữ liệu trực tiếp dto Response. => RestClientAdapter (hoặc RestTemplateAdapter) vì đây là luồng Blocking.
     */

    IdentityClient identityClient(WebClient webClient){
        /**
         * Đầu tiên mục đích của đoạn này là biến interface Java (IdentityClient) thành một HTTP Client thực sự
         * 1. WebClientAdapter.create(webClient) – lớp “chuyển đổi”
         *      Đây là cái "cầu nối". Nó nói với Spring rằng: "Hãy dùng cái WebClient (Non-blocking) này để thực hiện các cuộc gọi được định nghĩa trong Interface". 
         * 2. HttpServiceProxyFactory:
         *      Đây là cái "khuôn đúc". Spring sẽ nhìn vào Interface IdentityClient, thấy các annotation như @PostExchange, và nó sẽ tự động viết code thực thi (Proxy) để gửi request đi.
         * 3. createClient(IdentityClient.class):
         *      Kết quả nhận được một đối tượng thực thụ (Bean). Khi gọi identityClient.introspect(), thực chất là đang gọi vào cái Proxy mà Spring vừa đúc ra này.
         */
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(
                WebClientAdapter.create(webClient)
            ).build();
        
        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }
}
