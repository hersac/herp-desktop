package com.hersac.core.modules.terceros.entities.relations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipos_personas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoPersonaEntity {

    @Id
    @Column(name = "tipo_persona_id")
    private Integer tipoPersonaId;

    @Column(name = "nombre")
    private String nombre;

    @Override
    public String toString() {
        return nombre;
    }
}