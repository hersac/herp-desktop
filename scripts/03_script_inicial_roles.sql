-- ROLES GENERALES
INSERT INTO roles (nombre, descripcion, esta_activo, created_at, updated_at, created_by, updated_by) VALUES
('SUPERADMIN',         'Acceso total a todas las funciones del sistema',                true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('ADMINISTRADOR',      'Gestión administrativa general del sistema',                   true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),

-- ROLES COMERCIALES
('JEFE VENTAS',        'Supervisa y gestiona las operaciones del área de ventas',      true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('VENDEDOR',           'Realiza cotizaciones, pedidos y seguimiento de clientes',      true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('JEFE COMPRAS',       'Encargado de gestionar y supervisar las compras',              true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('COMPRADOR',          'Ejecuta las compras y relacionamiento con proveedores',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('INVENTARISTA',       'Encargado de registrar y controlar inventario',                true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('LOGÍSTICA',          'Gestión de envíos, transporte y entregas',                     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),

-- ROLES FINANCIEROS
('CONTADOR',           'Gestión contable y financiera de la empresa',                  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('TESORERO',           'Responsable de la caja, bancos y flujo de efectivo',           true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('ANALISTA CXC',       'Gestión y seguimiento de cuentas por cobrar',                  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('ANALISTA CXP',       'Gestión y control de cuentas por pagar',                       true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('AUDITOR',            'Revisa y audita las operaciones financieras y contables',      true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),

-- OTROS ROLES COMUNES
('RRHH',               'Encargado de la gestión del personal',                         true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('SOPORTE TÉCNICO',    'Soporte al usuario y mantenimiento del sistema',               true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0),
('DESARROLLADOR',      'Encargado del desarrollo e implementación de software',        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0);
