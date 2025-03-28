package org.scit.project.user.config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary; // 🔥 추가
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import jakarta.annotation.PreDestroy;

@Configuration
public class RedisConfig {
	
	private static final String REDIS_HOST = "127.0.0.1";  // Redis 서버 주소
    private static final int REDIS_PORT = 6379;  // Redis 포트
    private static final String REDIS_EXEC_PATH = "C:\\Program Files\\Redis\\redis-server.exe";  // Redis 실행 파일 경로
    private Process redisProcess; //실행된 Redis 프로세스를 저장할 변수

    public RedisConfig() {
        checkAndStartRedis();
    }

    /**
     * Redis 서버 실행 여부 확인 및 필요 시 자동 실행
     */
    private void checkAndStartRedis() {
        if (isRedisRunning()) {
            return;
        }

        if (!isRedisRunning()) {
            startRedisServer();

            // Redis가 실행될 때까지 기다리기 (최대 10초 대기)
            int retryCount = 0;
            while (!isRedisRunning() && retryCount < 10) {
                try {
                    Thread.sleep(1000); // 1초 대기
                    retryCount++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } 
    }
//    Spring 종료시 Redis도 같이 종료
    @PreDestroy
    public void stopRedisServer() {
        if (redisProcess != null) {
            redisProcess.destroy(); // 🔥 실행된 Redis 프로세스 종료
        } else {
            try {
                // 🔥 Windows에서 실행 중인 redis-server.exe 강제 종료
                Runtime.getRuntime().exec("taskkill /F /IM redis-server.exe");
            } catch (IOException e) {
                System.err.println("❌ Redis 서버 강제 종료 실패: " + e.getMessage());
            }
        }
    }

    /**
     * Redis 실행 여부 확인
     */
    private boolean isRedisRunning() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(REDIS_HOST, REDIS_PORT), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Redis 서버 실행 (Windows 기준)
     */
    private void startRedisServer() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(REDIS_EXEC_PATH);
            redisProcess = processBuilder.start();
        } catch (IOException e) {
            System.err.println("❌ Redis 서버 실행 실패: " + e.getMessage());
        }
    }

    @Bean
    @Primary  // 🔥 기본 RedisTemplate로 설정
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
