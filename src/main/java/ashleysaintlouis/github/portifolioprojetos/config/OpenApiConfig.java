package ashleysaintlouis.github.portifolioprojetos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Portfólio de Projetos")
                        .version("1.0.0")
                        .description("Sistema para gerenciar o portfólio de projetos de uma empresa")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("ashley.saintlouis@gmail.com")));
    }
}
