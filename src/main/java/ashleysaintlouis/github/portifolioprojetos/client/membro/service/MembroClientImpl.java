package ashleysaintlouis.github.portifolioprojetos.client.membro.service;

import ashleysaintlouis.github.portifolioprojetos.client.membro.dto.MembroDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MembroClientImpl implements MembroClient {

    private final RestTemplate restTemplate;

    @Value("${api.membros.url:https://68dc1ec67cd1948060a9842b.mockapi.io/api/v1/membros}")
    private String baseUrl;

    public MembroClientImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public MembroDTO buscarPorId(String id) {
        return restTemplate.getForObject(baseUrl + "/" + id, MembroDTO.class);
    }

    @Override
    public MembroDTO criar(MembroDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MembroDTO> request = new HttpEntity<>(dto, headers);
        return restTemplate.postForObject(baseUrl, request, MembroDTO.class);
    }
}
