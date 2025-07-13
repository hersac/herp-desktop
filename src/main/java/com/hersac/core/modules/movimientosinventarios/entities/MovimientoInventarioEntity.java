package com.hersac.core.modules.movimientosinventarios.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hersac.core.modules.bodegas.entities.BodegaEntity;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.movimientosinventarios.constants.TipoReferencia;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimientoInventarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_inventario_id")
    private Long movimientoInventarioId;

    @Column(name = "tipo_movimiento", nullable = false)
    private String tipoMovimiento;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "referencia_id", nullable = false)
    private Long referenciaId;

    @Column(name = "referencia_tipo", nullable = false)
    private int referenciaTipo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relaciones

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BodegaEntity bodega;

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
