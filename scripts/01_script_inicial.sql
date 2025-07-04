INSERT INTO departamentos (departamento_id, nombre, descripcion, esta_activo)
VALUES (1, 'Contabilidad', 'Departamento financiero', true);

INSERT INTO permisos (permiso_id, nombre, descripcion, esta_activo, created_at, updated_at, created_by, updated_by)
VALUES (1, 'VER USUARIOS', 'VER USUARIOS', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0);

INSERT INTO roles (rol_id, nombre, descripcion, esta_activo, created_at, updated_at, created_by, updated_by)
VALUES (1, 'SUPERADMIN', 'SUPERADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0);

INSERT INTO roles_permisos (rol_permiso_id, permiso_id, rol_id) 
VALUES (1, 1, 1);

INSERT INTO usuarios (usuario_id, nombre, correo, contrasena, esta_activo, departamento_id, rol_permiso_id, created_at, updated_at, created_by, updated_by)
VALUES (1, 'SUPERADMIN', 'admin', 'Heriberto1995**', true, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, null, null);