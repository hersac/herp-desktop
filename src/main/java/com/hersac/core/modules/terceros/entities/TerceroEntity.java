package com.hersac.core.modules.terceros.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hersac.core.modules.terceros.entities.relations.TipoPersonaEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "terceros")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TerceroEntity {
    @Id
    @Column(name = "tercero_id", nullable = false)
    private String terceroId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "esta_activo", nullable = false)
    private Boolean estaActivo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime fechaActualizacion;

    //Relaciones

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_persona_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private TipoPersonaEntity tipoPersona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private UsuarioEntity usuarioCreador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private UsuarioEntity usuarioActualizador;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    // Métodos utilitarios para estado como String
    public String getEstado() {
        return Boolean.TRUE.equals(estaActivo) ? "Activo" : "Inactivo";
    }

    public void setEstado(String estado) {
        this.estaActivo = "Activo".equalsIgnoreCase(estado);
    }
}