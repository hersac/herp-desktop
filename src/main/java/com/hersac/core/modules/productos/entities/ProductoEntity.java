package com.hersac.core.modules.productos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "unidad_medida", nullable = false)
    private String unidadMedida;

    @Column(name = "precio_base", nullable = false)
    private double precioBase;

    @Column(name = "esta_activo", nullable = false)
    private boolean estadoActivo;

    // Relaciones

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("producto")
    private List<ItemEntity> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UsuarioEntity usuarioCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    private UsuarioEntity usuarioActualizacion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.estadoActivo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

}
