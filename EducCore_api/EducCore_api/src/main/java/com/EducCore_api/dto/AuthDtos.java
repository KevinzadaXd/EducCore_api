package com.EducCore_api.dto;

import com.EducCore_api.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class AuthDtos {

    // ---- REQUEST DTOs ----

    @Schema(description = "Dados para cadastro de novo usuário")
    public record RegisterRequest(

        @Schema(description = "Nome completo", example = "João Silva")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String name,

        @Schema(description = "E-mail do usuário", example = "joao@email.com")
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @Schema(description = "Senha (mínimo 6 caracteres)", example = "senha123")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
        String password
    ) {}

    @Schema(description = "Dados para login")
    public record LoginRequest(

        @Schema(description = "E-mail do usuário", example = "joao@email.com")
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @Schema(description = "Senha", example = "senha123")
        @NotBlank(message = "Senha é obrigatória")
        String password
    ) {}

    @Schema(description = "Token para refresh")
    public record RefreshTokenRequest(

        @Schema(description = "Refresh token")
        @NotBlank(message = "Refresh token é obrigatório")
        String refreshToken
    ) {}

    // ---- RESPONSE DTOs ----

    @Schema(description = "Resposta de autenticação com tokens JWT")
    public record AuthResponse(

        @Schema(description = "Access token JWT")
        String accessToken,

        @Schema(description = "Refresh token")
        String refreshToken,

        @Schema(description = "Tipo do token", example = "Bearer")
        String tokenType,

        @Schema(description = "Expiração do access token em ms")
        long expiresIn,

        @Schema(description = "Dados do usuário autenticado")
        UserResponse user
    ) {
        public static AuthResponse of(String accessToken, String refreshToken, long expiresIn, UserResponse user) {
            return new AuthResponse(accessToken, refreshToken, "Bearer", expiresIn, user);
        }
    }

    @Schema(description = "Dados do usuário")
    public record UserResponse(

        Long id,
        String name,
        String email,
        Role role,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
    ) {}

    @Schema(description = "Mensagem genérica de resposta")
    public record MessageResponse(
        String message
    ) {}

    @Schema(description = "Resposta de erro")
    public record ErrorResponse(
        int status,
        String error,
        String message,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp
    ) {
        public static ErrorResponse of(int status, String error, String message) {
            return new ErrorResponse(status, error, message, LocalDateTime.now());
        }
    }
}
