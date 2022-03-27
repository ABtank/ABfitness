package ru.abfitness.oapi.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static ru.abfitness.oapi.constants.SwaggerConstant.API_AUTH;
import static ru.abfitness.oapi.constants.SwaggerConstant.API_ROLE;
import static ru.abfitness.oapi.constants.SwaggerConstant.*;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class SwaggerConfig {
    private TypeResolver typeResolver;

    @Autowired
    public void setTypeResolver(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket apiDocket(){
        return new Docket(SWAGGER_2)
                .groupName(API_AUTH)
                .apiInfo(apiInfo())
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.regex(SECURE_PATH))
                .build()
                .securitySchemes(singletonList(apiKey()))
                .securityContexts(singletonList(securityContext()))
//                .additionalModels(typeResolver.resolve(DisciplineCreationDto.class))
                .tags(new Tag(API_AUTH, "Авторизация и регистрация пользователя."),
                        new Tag(API_ROLE, "Работа с Ролями."),
                        new Tag(API_USER, "Операции с пользователем.")
                );
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(API_TITLE, API_DESCRIPTION,API_VERSION,TERM_OF_SERVICE,contact()
                ,LICENSE,LICENSE_URL, Collections.emptyList());
    }

    private Contact contact() {
        return new Contact(CONTACT_NAME,CONTACT_URL,CONTACT_EMAIL);
    }

    private ApiKey apiKey(){
        return new ApiKey(SECURITY_REFERENCE,AUTHORIZATION, SecurityScheme.In.HEADER.name());
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(securityReferences()).build();
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope[] authorizationScopes = {new AuthorizationScope(AUTHORIZATION_SCOPE,AUTHORIZATION_DESCRIPTION)};
        return singletonList(new SecurityReference(SECURITY_REFERENCE,authorizationScopes));
    }

    @Bean
    SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("test-app-client-id")
                .clientSecret("test-app-client-secret")
                .realm("test-app-realm")
                .appName("test-app")
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .enableCsrfSupport(false)
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(true)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .showCommonExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }
}

