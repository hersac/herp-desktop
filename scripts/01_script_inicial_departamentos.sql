-- DEPARTAMENTOS BASE
INSERT INTO departamentos (nombre, descripcion, esta_activo) VALUES
('Superusuario',    'Acceso completo al sistema',                        true),
('Administración',  'Departamento administrativo general',              true),

-- DEPARTAMENTOS COMERCIALES
('Ventas',          'Departamento de ventas y atención al cliente',     true),
('Compras',         'Encargado de adquisiciones y proveedores',         true),
('Inventario',      'Gestión de stock y almacenes',                     true),
('Logística',       'Distribución y transporte de productos',           true),
('Comercial',       'Dirección comercial y gestión estratégica',        true),

-- DEPARTAMENTOS FINANCIEROS
('Contabilidad',    'Departamento financiero',                          true),
('Tesorería',       'Gestión de flujo de caja y bancos',                true),
('Cuentas por Cobrar', 'Gestión de ingresos y facturación a clientes', true),
('Cuentas por Pagar',  'Gestión de egresos y pagos a proveedores',     true),
('Auditoría',       'Revisión y control interno de operaciones',        true),

-- OTROS COMUNES
('Recursos Humanos', 'Gestión del personal y nómina',                   true),
('Sistemas',        'Administración de la infraestructura tecnológica', true);
