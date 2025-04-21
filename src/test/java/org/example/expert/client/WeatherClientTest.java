package org.example.expert.client;

import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.common.exception.ServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherClientTest {

    private WeatherClient weatherClient;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        //mock 객체 생성
        restTemplate = mock(RestTemplate.class);

        //mock 처리하여 RestTemplate을 리턴하게 처리
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        when(builder.build()).thenReturn(restTemplate);
        weatherClient = new WeatherClient(builder);
    }

    @Test
    void 응답상태가_OK가_아닌_경우_예외발생() {
        // given
        ResponseEntity<WeatherDto[]> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);
        when(restTemplate.getForEntity(any(), eq(WeatherDto[].class))).thenReturn(responseEntity);

        // when & then
        ServerException ex = assertThrows(ServerException.class, () -> weatherClient.getTodayWeather());
        assertTrue(ex.getMessage().contains("날씨 데이터를 가져오는데 실패했습니다"));
    }

    @Test
    void 응답_바디가_null_또는_빈배열이면_예외발생() {
        // null 일때
        when(restTemplate.getForEntity(any(), eq(WeatherDto[].class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        ServerException ex1 = assertThrows(ServerException.class, () -> weatherClient.getTodayWeather());
        assertEquals("날씨 데이터가 없습니다.", ex1.getMessage());

        // 빈배열 일때
        when(restTemplate.getForEntity(any(), eq(WeatherDto[].class)))
                .thenReturn(new ResponseEntity<>(new WeatherDto[0], HttpStatus.OK));

        ServerException ex2 = assertThrows(ServerException.class, () -> weatherClient.getTodayWeather());
        assertEquals("날씨 데이터가 없습니다.", ex2.getMessage());
    }

}
// 문제 의식 WeatherClient관련 오류 처리를 하기로 함
//또한 일반적으로 제대로 작동하지않거나 없을때를 처리하기로 함
//
// 해결방안
// 기존의 방식을 보면 보통 HttpStaus또는 비어잇을때오류 발생 처리
//
// Httpstatus를 통해 처리
//응답바디가 null이거나 빈배열이 입력 됫을시 분할해서 처리
//
// 외부api를 가져와서 하는것같은데 그부분을 좀더 찾아 봣어야햇나 하는생각이든다
//아니면 경로를 찾는 방법이있는지 궁금하다.
//
// 전후 데이터 Class 100% Method 25% line 9% Branch 0%
//
//