# Servidor de Impresión TCP

Servidor de impresión distribuido con arquitectura cliente-servidor mediante sockets TCP. Soporta impresión en blanco y negro y color, con cifrado AES, verificación de integridad SHA-256, excepciones personalizadas y registro de cada impresión con su coste.

## Requisitos

- Java 17+

## Cómo ejecutar

**Doble clic en `Ejecutar.bat`** o desde terminal:
```cmd
.\Ejecutar.bat
```

El script mata procesos Java anteriores, compila todo el proyecto, lanza el servidor y ejecuta 4 clientes de prueba automáticamente.

### Manual (2 terminales)

**Terminal 1 — Servidor:**
```cmd
java -cp target/classes es.etg.dam.servidor.Servidor
```

**Terminal 2 — Cliente:**
```cmd
java -cp target/classes es.etg.dam.cliente.Cliente "BN 5"
java -cp target/classes es.etg.dam.cliente.Cliente "COLOR 3"
```

Formato del mensaje: `"TIPO HOJAS"` donde TIPO es `BN` o `COLOR` y HOJAS es un número entero.

### Compilar manualmente

```cmd
javac -d target/classes -sourcepath src/main/java src/main/java/es/etg/dam/util/*.java src/main/java/es/etg/dam/exception/*.java src/main/java/es/etg/dam/*.java src/main/java/es/etg/dam/servidor/*.java src/main/java/es/etg/dam/cliente/*.java
```

El orden importa: primero `util`, luego `exception`, luego las clases raíz, luego `servidor` y `cliente`.

## Estructura

```
src/main/java/es/etg/dam/
├── Conexion.java               → Envío y recepción segura (hash + cifrado)
├── Impresora.java              → Lógica de impresión y precios
├── Tinta.java                  → Estado de la tinta (región crítica)
├── ClienteHandler.java         → Hilo por cliente
├── servidor/
│   └── Servidor.java           → Acepta conexiones y lanza hilos
├── cliente/
│   └── Cliente.java            → Envía petición y muestra respuesta
├── util/
│   ├── HashUtil.java           → Genera hash SHA-256
│   ├── CifradoUtil.java        → Cifrado/descifrado AES
│   └── RegistroImpresion.java  → Escribe impresora.log
└── exception/
    ├── ClienteException.java        → Error general en el cliente
    ├── ServidorException.java       → Error general en el servidor
    ├── ImpresoraException.java      → Error en la lógica de impresión
    ├── GestionClienteException.java → Error procesando un cliente
    └── HashNoCoincideException.java → El hash recibido no coincide
```

## Flujo de datos

```
Cliente                            Servidor
  │                                   │
  ├─► 1. Construye mensaje            │
  │      "BN 5"                       │
  ├─► 2. Calcula hash SHA-256         │
  ├─► 3. Cifra mensaje (AES)          │
  ├─► 4. Envía hash + cifrado ──────► │
  │                                   ├─► 5. Descifra mensaje
  │                                   ├─► 6. Recalcula hash
  │                                   ├─► 7. Compara hashes
  │                                   ├─► 8. Parsea tipo y hojas
  │                                   ├─► 9. Consume tinta (synchronized)
  │                                   ├─► 10. Calcula coste
  │                                   ├─► 11. Escribe impresora.log
  │                                   ├─► 12. Cifra respuesta
  │ ◄──────────────────────────────── ├─► 13. Envía hash + cifrado
  ├─► 14. Descifra respuesta          │
  ├─► 15. Verifica hash               │
  └─► 16. Muestra resultado           │
```

## Seguridad

| Capa | Tecnología | Función |
|---|---|---|
| Cifrado | AES/ECB/PKCS5Padding 128 bits | El mensaje viaja cifrado, nadie puede leerlo en tránsito |
| Integridad | SHA-256 | Detecta si el mensaje fue alterado |

El cifrado se aplica exclusivamente en `Conexion.java`. El resto del proyecto no sabe que existe — llama a `enviar` y `recibir` sin preocuparse de cómo viaja el mensaje.

## Concurrencia

El servidor lanza un `Thread` por cada cliente que se conecta mediante `ClienteHandler`, que implementa `Runnable`. La tinta es un recurso compartido entre todos los hilos, por eso `consumirBN` y `consumirColor` son `synchronized` — forman la **región crítica** que evita que dos hilos consuman tinta a la vez y produzcan un estado inconsistente.

## Excepciones

| Excepción | Tipo | Dónde se lanza |
|---|---|---|
| `ClienteException` | checked | `Cliente` cuando falla la conexión o envío |
| `ServidorException` | checked | `Servidor` cuando falla el ServerSocket |
| `ImpresoraException` | checked | `Impresora` cuando el tipo no es válido |
| `GestionClienteException` | unchecked | `ClienteHandler` cuando falla al procesar |
| `HashNoCoincideException` | unchecked | `Conexion` cuando los hashes no coinciden |

Las excepciones `checked` (extienden `Exception`) obligan a ser capturadas o declaradas. Las `unchecked` (extienden `RuntimeException`) se propagan automáticamente.

## Precios

| Tipo | Precio por hoja |
|---|---|
| BN (Blanco y Negro) | 0,50 euros |
| COLOR | 1,00 euro |

## Tinta inicial

| Tipo | Hojas disponibles |
|---|---|
| BN | 50 |
| COLOR | 20 |

## Registro

Cada impresión queda guardada en `impresora.log` en la raíz del proyecto:

```
[2026-06-04T10:23:41] OK  | Tipo: BN    | Hojas:  5 | Coste: 2,50 euros | BN restantes: 45 | COLOR restantes: 20
[2026-06-04T10:23:52] KO  | Tipo: COLOR | Hojas: 25 | Motivo: sin tinta suficiente | BN restantes: 45 | COLOR restantes: 20
```

## Flujo de ejecución desde terminal

### Paso 1 — Compilar
```cmd
javac -d target/classes -sourcepath src/main/java src/main/java/es/etg/dam/util/*.java src/main/java/es/etg/dam/exception/*.java src/main/java/es/etg/dam/*.java src/main/java/es/etg/dam/servidor/*.java src/main/java/es/etg/dam/cliente/*.java
```

### Paso 2 — Terminal 1: arrancar el servidor
```cmd
java -cp target/classes es.etg.dam.servidor.Servidor
```
```
Servidor iniciado. Esperando clientes en puerto 8080...
```
El servidor queda bloqueado esperando conexiones.

### Paso 3 — Terminal 2: ejecutar el cliente
```cmd
java -cp target/classes es.etg.dam.cliente.Cliente "BN 5"
java -cp target/classes es.etg.dam.cliente.Cliente "COLOR 3"
```

Formatos válidos:
- `"BN 5"` → imprime 5 hojas en blanco y negro
- `"COLOR 3"` → imprime 3 hojas en color

### Qué ocurre internamente al ejecutar el cliente

```
[impresora.log]
[2026-06-04T10:23:41] OK | Tipo: BN | Hojas: 5 | Coste: 2,50 euros | BN restantes: 45 | COLOR restantes: 20
```