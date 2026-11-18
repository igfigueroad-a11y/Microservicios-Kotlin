package cl.duoc.miregistro.admin_service.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean("siniestrosClient")
    public RestClient siniestrosClient(@Value("${siniestros.service.url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }

    @Bean("usuariosClient")
    public RestClient usuariosClient(@Value("${usuarios.service.url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}