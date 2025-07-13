package com.hersac.core.modules.compras.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compra_id")
    private Long compraId;

    @Column(name = "total_compra", nullable = false)
    private BigDecimal totalCompra;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relaciones

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProveedorEntity proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuarioEntity usuarioCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuarioEntity usuarioActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

}
