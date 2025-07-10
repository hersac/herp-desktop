package com.hersac.core.modules.clientes.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "esta_activo", nullable = false)
    private boolean esta_activo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relaciones

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tercero_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private TerceroEntity tercero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private UsuarioEntity usuarioCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private UsuarioEntity usuarioActualizacion;

    @PrePersist
    public void prePersist() {
        this.esta_activo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
}
