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
        if (!isRedisRunning()) {
            System.out.println("🔴 Redis 서버가 꺼져 있습니다. 실행을 시도합니다...");
            startRedisServer();

            // Redis가 실행될 때까지 기다리기 (최대 10초 대기)
            int retryCount = 0;
            while (!isRedisRunning() && retryCount < 10) {
                try {
                    Thread.sleep(1000); // 1초 대기
                    retryCount++;
                    System.out.println("⏳ Redis 서버 실행 대기 중... (" + retryCount + "초)");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (isRedisRunning()) {
                System.out.println("✅ Redis 서버가 성공적으로 실행되었습니다.");
            } else {
                System.err.println("❌ Redis 서버 실행에 실패했습니다. 직접 실행해주세요.");
            }
        } else {
            System.out.println("✅ Redis 서버가 이미 실행 중입니다.");
        }
    }
//    Spring 종료시 Redis도 같이 종료
    @PreDestroy
    public void stopRedisServer() {
        if (redisProcess != null) {
            redisProcess.destroy(); // 🔥 실행된 Redis 프로세스 종료
            System.out.println("🛑 Redis 서버가 종료되었습니다.");
        } else {
            System.out.println("🔎 실행된 Redis 프로세스가 없습니다.");
            try {
                // 🔥 Windows에서 실행 중인 redis-server.exe 강제 종료
                Runtime.getRuntime().exec("taskkill /F /IM redis-server.exe");
                System.out.println("🛑 Redis 서버 프로세스를 강제 종료했습니다.");
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
            System.out.println("🚀 Redis 서버를 실행했습니다.");
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
