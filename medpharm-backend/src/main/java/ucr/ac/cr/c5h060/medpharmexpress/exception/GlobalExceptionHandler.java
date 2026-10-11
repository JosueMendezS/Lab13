package ucr.ac.cr.c5h060.medpharmexpress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("https://medpharm.local/errors/" + status.value()));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail noEncontrado(RecursoNoEncontradoException ex) {
        return problem(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ProblemDetail stock(StockInsuficienteException ex) {
        return problem(HttpStatus.CONFLICT, "Stock insuficiente", ex.getMessage());
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ProblemDetail estado(EstadoInvalidoException ex) {
        return problem(HttpStatus.CONFLICT, "Estado inválido", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacion(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage()).toList();
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Error de validación",
                "La solicitud contiene campos inválidos");
        pd.setProperty("errores", errores);
        return pd;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail credenciales(BadCredentialsException ex) {
        return problem(HttpStatus.UNAUTHORIZED, "No autenticado", "Usuario o contraseña incorrectos");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail denegado(AccessDeniedException ex) {
        return problem(HttpStatus.FORBIDDEN, "Acceso denegado", "No tiene permisos para esta operación");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail generico(Exception ex) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno",
                "Ocurrió un error inesperado");
    }
}