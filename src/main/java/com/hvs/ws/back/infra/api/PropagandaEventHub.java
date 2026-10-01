package com.hvs.ws.back.infra.api;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Pub/sub em memória por SSE: o player ao vivo se inscreve em
 * {@code GET /api/v1/propaganda/events} e recebe {@code atualizado} sempre que
 * a grade criar, editar ou remover propaganda — evita reconsultar a lista.
 * Assumido instância única do backend (sem broker); com mais de uma instância
 * seria preciso um canal externo.
 */
@Component
public class PropagandaEventHub {

    private final List<SseEmitter> inscritos = new CopyOnWriteArrayList<>();

    /** Abre a conexão de um inscrito. Sem timeout: o EventSource renova sozinho. */
    public SseEmitter inscrever() {

        final var emitter = new SseEmitter(0L);
        emitter.onCompletion(() -> inscritos.remove(emitter));
        emitter.onTimeout(() -> inscritos.remove(emitter));
        emitter.onError(erro -> inscritos.remove(emitter));
        inscritos.add(emitter);
        enviar(emitter, "conectado");
        return emitter;
    }

    /** Avisa todos os inscritos de que a lista de propaganda mudou. */
    public void publicar() {

        inscritos.forEach(emitter -> enviar(emitter, "atualizado"));
    }

    /** Keep-alive: proxy/servidor encerram conexão HTTP ociosa. */
    @Scheduled(fixedRate = 25000)
    public void heartbeat() {

        inscritos.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().comment("ping"));
            } catch (IOException | IllegalStateException e) {
                inscritos.remove(emitter);
            }
        });
    }

    private void enviar(final SseEmitter emitter, final String evento) {

        try {
            emitter.send(SseEmitter.event()
                    .name(evento)
                    .data(String.valueOf(System.currentTimeMillis())));
        } catch (IOException | IllegalStateException e) {
            inscritos.remove(emitter);
        }
    }
}
