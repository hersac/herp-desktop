package com.hersac.core.modules.bodegas.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bodegas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BodegaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bodega_id")
    private Long bodegaId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "esta_activa", nullable = false)
    private Boolean estaActiva = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "creada_por", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuarioEntity creadaPor;

    @OneToMany(mappedBy = "bodega")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "bodega"})
    private List<ItemEntity> items;

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
    }
}
