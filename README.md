# Proyecto de Programación II

## Sistema de gestión para Restaurante La Doble

Aplicación de escritorio desarrollada en Java Swing. La interfaz usa FlatLaf y los formularios `.form` de la capa de presentación se pueden abrir y editar con el GUI Builder de Apache NetBeans.

## Estructura del proyecto

```text
Restaurante_LaDoble/
├── README.md
├── build.xml
├── manifest.mf
├── nbproject/
├── src/
│   └── restaurante/
│       ├── Main.java
│       ├── datos/
│       │   ├── CatalogoMenu.java
│       │   └── package-info.java
│       ├── entidades/
│       │   ├── Cliente.java
│       │   ├── Cocinero.java
│       │   ├── DetallePedido.java
│       │   ├── Empleado.java
│       │   ├── EstadoPedido.java
│       │   ├── Mesero.java
│       │   ├── Pedido.java
│       │   ├── Persona.java
│       │   ├── Plato.java
│       │   ├── ProveedorAlimentos.java
│       │   ├── Restaurante.java
│       │   └── package-info.java
│       ├── presentacion/
│       │   ├── BotonSemaforo.java
│       │   ├── PanelInicio.java
│       │   ├── PanelInicio.form
│       │   ├── PanelMenu.java
│       │   ├── PanelMenu.form
│       │   ├── PanelPedidos.java
│       │   ├── PanelPedidos.form
│       │   ├── Tema.java
│       │   ├── VentanaPrincipal.java
│       │   └── VentanaPrincipal.form
│       ├── servicios/
│       │   └── package-info.java
│       └── themes/
│           └── FlatDarkLaf.properties
└── test/
```

## Paquetes principales

- **`entidades`** contiene los modelos del restaurante: platos, pedidos, detalles, clientes y personal. Cada `Plato` incluye código, nombre, categoría, descripción y precio.
- **`datos`** contiene `CatalogoMenu`, el catálogo compartido en memoria por las pantallas Menú y Pedidos. Los cambios de platos hechos desde Menú aparecen en Pedidos al volver a esa pantalla.
- **`presentacion`** contiene la ventana principal y los paneles Swing. Los archivos `.form` corresponden a los formularios editables de NetBeans.
- **`servicios`** queda reservado para la lógica de negocio y operaciones de datos que se agreguen después.
- **`themes`** contiene los ajustes de FlatLaf.

## Catálogo inicial

El catálogo inicia con tres hamburguesas: **La Doble**, especialidad de la casa, **Clásica** y **BBQ Bacon**. También incluye ejemplos de combos, bebidas y extras para mostrar las demás categorías del punto de venta. El catálogo actual vive en memoria; todavía no se guarda en una base de datos ni en archivos.
